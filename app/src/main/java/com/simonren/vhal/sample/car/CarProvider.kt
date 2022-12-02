package com.simonren.vhal.sample.car

import android.car.Car

/**
 * @author Simon
 * @desc
 */

typealias CarReady = (Car) -> Unit

interface CarProvider {

    /**
     * access to car service should stop until car service is ready
     * @param readyAction car service ready todo something
     */
    fun connectCar(readyAction: CarReady)

    fun registerCarLifecycle(carAccessOwner: CarAccessOwner)

    interface CarAccessOwner {
        fun unavailable()
    }
}