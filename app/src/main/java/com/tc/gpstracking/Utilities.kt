package com.tc.gpstracking

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import java.util.*

class Utilities {

    companion object {
        fun calculateDistance(startLocation: Location, endLocation: Location): Int {
            return startLocation.distanceTo(endLocation).toInt()
        }

        fun getAddress(
            content: Context,
            location: Location,
            geoCoderMaxResult: Int
        ): List<Address> {
            val geoCoder = Geocoder(content, Locale.ENGLISH)
            return geoCoder.getFromLocation(
                location.latitude,
                location.longitude,
                geoCoderMaxResult
            )
        }
    }

}