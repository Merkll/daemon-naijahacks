package com.example.watcher.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*

class LocationService(private val context: Context, private val activity: Activity) {
    private var mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    private lateinit var locationCallback: (longitude: String, latitude: String) -> Unit

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getCurrentLocation (callback: (longitude: String, latitude: String) -> Unit) {
        locationCallback = callback
        getLastLocation()
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        when(isLocationEnabled()) {
            true -> {
                mFusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
                    val location: Location? = task.result
                    if (location == null) requestNewLocationData()
                    else locationFound(location)
                }
            }

            else -> {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) = locationFound(locationResult.lastLocation)
    }

    private fun locationFound(location: Location) {
        val latitude = location.latitude.toString()
        val longitude = location.longitude.toString()
        Log.i("Location", "$latitude $longitude")
        if (locationCallback != null) locationCallback(latitude, longitude)
    }
    companion object {
        fun getLocationOnGoogleMap(lat: String, long: String): String = "https://www.google.com/maps/place/$lat, $long"
        fun getEmergencyMessage(): String {
            return "I need your assistance right now. Attach is my current location. Thanks"
        }
    }
}