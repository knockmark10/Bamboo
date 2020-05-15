package com.markoid.bamboo.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient

object GoogleInjector {

    fun providesFusedClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun providesSettingsClient(context: Context): SettingsClient =
        LocationServices.getSettingsClient(context)

}