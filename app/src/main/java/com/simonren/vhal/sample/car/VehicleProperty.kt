package com.simonren.vhal.sample.car

/**
 * @author Simon
 * @desc
 */
data class VehicleProperty<T>(
    val id: Int,
    val value: T,
)
