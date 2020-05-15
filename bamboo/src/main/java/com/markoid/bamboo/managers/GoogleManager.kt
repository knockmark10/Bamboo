package com.markoid.bamboo.managers

import android.content.Context
import com.google.android.gms.location.*
import com.markoid.bamboo.callbacks.GoogleCallback
import com.markoid.bamboo.entities.SettingsOptions
import com.markoid.bamboo.utils.ApiChecker

class GoogleManager(
    private val mContext: Context,
    private val mOptions: SettingsOptions,
    private val mApiChecker: ApiChecker,
    private val mFusedClient: FusedLocationProviderClient,
    private val mSettings: SettingsClient
) : LocationCallback() {

    private var mListener: GoogleCallback? = null

    private val mLocationRequest: LocationRequest by lazy {
        LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = mOptions.updateInterval
            fastestInterval = mOptions.fastestInterval
        }
    }

    /**
     * Register listener to communicate.
     */
    fun registerListener(listener: GoogleCallback) {
        this.mListener = listener
    }

    /**
     * Request location updates to track user's location.
     */
    fun startLocationUpdates() {
        if (validate()) {
            val locationSettingsRequest = buildLocationSettingsRequest()
            this.mSettings.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener { setupLocation() }
                .addOnFailureListener { this.mListener?.onLocationServicesDisabled() }
        }
    }

    /**
     * Stop receiving location updates (turn off gps use).
     */
    fun stopLocationUpdates() {
        this.mFusedClient.removeLocationUpdates(this)
    }

    /**
     * Request user's last known location
     */
    fun getLastLocation() {
        if (validate()) {
            val locationClient = LocationServices.getFusedLocationProviderClient(mContext)
            locationClient.lastLocation
                .addOnSuccessListener { mListener?.onLocationHasChanged(it) }
                .addOnFailureListener { mListener?.onLocationHasChangedError(it) }
        }
    }

    private fun buildLocationSettingsRequest(): LocationSettingsRequest {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(this.mLocationRequest)
        return builder.build()
    }

    private fun notify(output: (listener: GoogleCallback) -> Unit): Boolean {
        this.mListener?.let(output)
        return false
    }

    /**
     * Validate if services are up and running, and permissions have been granted.
     */
    private fun validate(): Boolean = with(this.mApiChecker) {
        when {
            !this.areLocationServicesEnabled() -> notify { it.onLocationServicesDisabled() }
            !this.isLocationPermissionGranted() -> notify { it.onPermissionsAreMissing() }
            else -> true
        }
    }

    private fun setupLocation() {
        this.mFusedClient.requestLocationUpdates(this.mLocationRequest, this, this.mOptions.looper)
    }

    override fun onLocationResult(result: LocationResult?) {
        super.onLocationResult(result)
        result?.lastLocation?.let { this.mListener?.onLocationHasChanged(it) }
    }

}