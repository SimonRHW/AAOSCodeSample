package com.simonren.vhal.sample

import androidx.lifecycle.ViewModel
import com.simonren.vhal.sample.car.VehicleDrivingStateManager
import com.simonren.vhal.sample.car.VehiclePropertyManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Simon
 * @desc
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val vehicleDrivingStateManager: VehicleDrivingStateManager,
    private val vehiclePropertyManager: VehiclePropertyManager,
) : ViewModel() {

}