package com.simonren.vhal.sample.di

import android.app.Activity
import android.car.CarAppFocusManager
import android.view.Window
import com.simonren.vhal.sample.car.CarProvider
import com.simonren.vhal.sample.car.VehicleAppFocusManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * @author Simon
 * @desc
 */

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun providesWindow(activity: Activity): Window {
        return activity.window
    }

    @Provides
    fun vehicleAppFocusManager(
        activity: Activity,
        carProvider: CarProvider,
    ): VehicleAppFocusManager {
        return VehicleAppFocusManager(carProvider)
    }

}