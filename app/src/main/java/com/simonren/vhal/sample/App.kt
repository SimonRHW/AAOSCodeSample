package com.simonren.vhal.sample

import android.app.Application
import com.simonren.vhal.sample.car.CarProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var carProvider: CarProvider

    override fun onCreate() {
        super.onCreate()
    }
}