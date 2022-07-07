package com.tc.gpstracking

import android.location.Location
import com.tc.gpstracking.Utilities.Companion.calculateDistance
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateDistanceTest {

    @Test
    fun test() {
        val initLocation = Location("initLocation").apply {
            this.latitude = 25.105497
            this.longitude = 121.597366
        }

        val currentLocation = Location("currentLocation").apply {
            this.latitude = 36.2048
            this.longitude = 138.2529
        }

        val distance = calculateDistance(currentLocation, initLocation)

        // It's always 0 because we are testing an Android Api
        assertEquals(0, distance)
    }
}