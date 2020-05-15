package com.markoid.bamboo.managers

import android.content.Context
import android.location.Location
import com.markoid.bamboo.callbacks.BambooCallback
import com.markoid.bamboo.callbacks.GoogleCallback
import com.markoid.bamboo.callbacks.HuaweiCallback
import com.markoid.bamboo.entities.SettingsOptions
import com.markoid.bamboo.di.BambooInjector.providesApiChecker
import com.markoid.bamboo.di.BambooInjector.providesGoogleManager
import com.markoid.bamboo.di.BambooInjector.providesHuaweiManager
import com.markoid.bamboo.utils.ApiChecker

class Bamboo
private constructor(
    private val mGoogleManager: GoogleManager,
    private val mHuaweiManager: HuaweiManager,
    private val mApiChecker: ApiChecker
) : HuaweiCallback, GoogleCallback {

    private var mListener: BambooCallback? = null

    init {
        this.registerListeners()
    }

    fun registerListener(listener: BambooCallback) {
        this.mListener = listener
    }

    fun startLocationUpdates() {
        if (this.mApiChecker.isHuaweiDevice()) {
            this.mHuaweiManager.startLocationUpdates()
        } else {
            this.mGoogleManager.startLocationUpdates()
        }
    }

    fun stopLocationUpdates() {
        if (this.mApiChecker.isHuaweiDevice()) {
            this.mHuaweiManager.stopLocationUpdates()
        } else {
            this.mGoogleManager.stopLocationUpdates()
        }
    }

    fun getLastLocation() {
        if (this.mApiChecker.isHuaweiDevice()) {
            this.mHuaweiManager.getLastLocation()
        } else {
            this.mGoogleManager.getLastLocation()
        }
    }

    private fun registerListeners() {
        this.mGoogleManager.registerListener(this)
        this.mHuaweiManager.registerListener(this)
    }

    override fun onLocationServicesDisabled() {
        this.mListener?.onLocationServicesDisabled()
    }

    override fun onPermissionsAreMissing() {
        this.mListener?.onPermissionsAreMissing()
    }

    override fun onLocationHasChanged(location: Location) {
        this.mListener?.onLocationHasChanged(location)
    }

    override fun onLocationHasChangedError(exception: Exception) {
        this.mListener?.onLocationHasChangedError(exception)
    }

    object Builder {

        private var updateInterval: Long = (15 * 1000).toLong()

        private var fastestInterval: Long = (10 * 1000).toLong()

        private var useLooper: Boolean = false

        fun setUpdateInterval(milliseconds: Long): Builder {
            this.updateInterval = milliseconds
            return this
        }

        fun setFastestInterval(milliseconds: Long): Builder {
            this.fastestInterval = milliseconds
            return this
        }

        fun useLooper(useLooper: Boolean): Builder {
            this.useLooper = useLooper
            return this
        }

        fun build(context: Context): Bamboo = Bamboo(
            providesGoogleManager(context, encapsulateOptions()),
            providesHuaweiManager(context, encapsulateOptions()),
            providesApiChecker(context)
        )

        private fun encapsulateOptions(): SettingsOptions =
            SettingsOptions(this.updateInterval, this.fastestInterval, this.useLooper)

    }

}