package com.simonren.vhal.sample.car

import android.car.Car

/**
 * @author Simon
 * @desc
 */
interface CarProvider {

    fun providerCar(): Car?

    fun registerCarLifecycle(carAccessOwner: CarAccessOwner)

    interface CarAccessOwner {
        fun available(car: Car)
        fun unavailable()
    }
}