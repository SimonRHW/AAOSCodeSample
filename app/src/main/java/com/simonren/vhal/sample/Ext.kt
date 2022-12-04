package com.simonren.vhal.sample

import android.car.VehiclePropertyIds
import com.simonren.vhal.sample.car.VehicleProperty

/**
 * @author Simon
 * @desc
 */


fun VehicleProperty.name(): String {
    return VehiclePropertyIds.toString(id)
}

fun VehicleProperty.desc(): String {
    return "desc :${value}"
}