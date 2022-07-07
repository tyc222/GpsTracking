package com.tc.gpstracking

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.tc.gpstracking.Utilities.Companion.calculateDistance
import com.tc.gpstracking.Utilities.Companion.getAddress

class MainActivity : AppCompatActivity() {

    private lateinit var lat: TextView
    private lateinit var lon: TextView
    private lateinit var address: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLocationPermission()
        init()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun init() {
        lat = findViewById(R.id.lat)
        lon = findViewById(R.id.lon)
        address = findViewById(R.id.address)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            lastLocation = location
            lastLocation?.let {
                updateUI(it)
            }
        }

        locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener {
            checkLocationPermission()
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (lastLocation == null) updateUI(location)
                    else {
                        lastLocation?.let {
                            val distance = calculateDistance(it, location)
                            if (distance > METERS_TO_DISPLAY_TOAST) showToast(distance.toString())
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(location: Location) {
        lat.text = location.latitude.toString()
        lon.text = location.longitude.toString()
        val lastAddress = getAddress(this, location, GEO_CODER_MAX_RESULT)
        address.text = lastAddress[0].getAddressLine(0)
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this,
            "You have moved $msg metres from your previous position",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("Please click accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun startLocationUpdates() {
        checkLocationPermission()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000 * 5
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000 * 2
        private const val GEO_CODER_MAX_RESULT: Int = 1
        private const val METERS_TO_DISPLAY_TOAST: Int = 10
    }
}