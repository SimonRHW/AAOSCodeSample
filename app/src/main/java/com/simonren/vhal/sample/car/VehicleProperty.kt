package com.simonren.vhal.sample.car

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * @author Simon
 * @desc
 */
@Parcelize
data class VehicleProperty(
    val id: Int,
    val value: String,
) : Parcelable
