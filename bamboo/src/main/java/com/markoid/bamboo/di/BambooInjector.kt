package com.markoid.bamboo.di

import android.content.Context
import com.markoid.bamboo.entities.SettingsOptions
import com.markoid.bamboo.managers.GoogleManager
import com.markoid.bamboo.managers.HuaweiManager
import com.markoid.bamboo.utils.ApiChecker

object BambooInjector {

    fun providesGoogleManager(context: Context, options: SettingsOptions): GoogleManager =
        GoogleManager(
            context,
            options,
            providesApiChecker(context),
            GoogleInjector.providesFusedClient(context),
            GoogleInjector.providesSettingsClient(context)
        )

    fun providesHuaweiManager(context: Context, options: SettingsOptions): HuaweiManager =
        HuaweiManager(
            context,
            options,
            providesApiChecker(context),
            HuaweiInjector.providesFusedClient(context),
            HuaweiInjector.providesLocationSettings(context)
        )

    fun providesApiChecker(context: Context): ApiChecker =
        ApiChecker(context)

}