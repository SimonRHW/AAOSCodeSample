package com.simonren.vhal.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simonren.vhal.sample.car.VehicleUxRestrictionsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Simon

 * @desc 安全驾驶限制
 */

@HiltViewModel
class UXRestrictionViewModel @Inject constructor(
    private val vehicleUxRestrictionsManager: VehicleUxRestrictionsManager,
) : ViewModel() {

    private val _uxRestrictionMode = MutableLiveData<VehicleUxRestrictionsManager.UXRestrictionMode>()
    fun uxRestrictionMode(): LiveData<VehicleUxRestrictionsManager.UXRestrictionMode> = _uxRestrictionMode

    init {
        vehicleUxRestrictionsManager.currentUXRestrictionMode.let {
            _uxRestrictionMode.postValue(it)
        }
    }

}