package com.simonren.vhal.sample.di

import android.content.Context
import com.simonren.vhal.sample.car.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Simon
 * @desc
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun carProvider(
        @ApplicationContext context: Context,
    ): CarProvider {
        return CarProviderImpl(context)
    }

    @Singleton
    @Provides
    fun vehicleUxRestrictionsManager(
        carProvider: CarProvider,
    ): VehicleUxRestrictionsManager {
        return VehicleUxRestrictionsManager(carProvider)
    }

    @Singleton
    @Provides
    fun vehiclePropertyManager(
        carProvider: CarProvider,
    ): VehiclePropertyManager {
        return VehiclePropertyManager(carProvider)
    }

    @Singleton
    @Provides
    fun vehicleDrivingStateManager(
        carProvider: CarProvider,
    ): VehicleDrivingStateManager {
        return VehicleDrivingStateManager(carProvider)
    }

}