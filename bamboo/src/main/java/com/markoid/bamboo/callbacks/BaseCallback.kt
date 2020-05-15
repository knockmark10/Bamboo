package com.markoid.bamboo.callbacks

import android.location.Location

interface BaseCallback {
    fun onLocationServicesDisabled()
    fun onPermissionsAreMissing()
    fun onLocationHasChanged(location: Location)
    fun onLocationHasChangedError(exception: Exception)
}