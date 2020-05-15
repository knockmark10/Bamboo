package com.markoid.bamboo.di

import android.content.Context
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.SettingsClient

object HuaweiInjector {

    fun providesFusedClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun providesLocationSettings(context: Context): SettingsClient =
        LocationServices.getSettingsClient(context)

}