package com.simonren.vhal.sample

import android.car.VehiclePropertyIds
import com.simonren.vhal.sample.car.VehicleProperty

/**
 * @author Simon
 * @desc
 */


inline fun <reified T> VehicleProperty<T>.name(): String {
    return VehiclePropertyIds.toString(id)
}

inline fun <reified T> VehicleProperty<T>.desc(): String {
    return "desc :${this.value}"
}