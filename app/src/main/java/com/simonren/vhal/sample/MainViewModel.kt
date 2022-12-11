package com.simonren.vhal.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simonren.vhal.sample.car.VehicleDrivingStateManager
import com.simonren.vhal.sample.car.VehicleProperty
import com.simonren.vhal.sample.car.VehiclePropertyManager
import com.simonren.vhal.sample.car.VehicleUxRestrictionsManager
import com.simonren.vhal.sample.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Simon
 * @desc
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val vehicleDrivingStateManager: VehicleDrivingStateManager,
    private val vehiclePropertyManager: VehiclePropertyManager,
    private val vehicleUxRestrictionsManager: VehicleUxRestrictionsManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState.Success(
        VehicleState(
            emptyList(),
            VehicleDrivingStateManager.DrivingState.Unknown,
            VehicleUxRestrictionsManager.UXRestrictionMode(),
        )
    ))

    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        subscribeState()
    }

    private fun subscribeState() {
        viewModelScope.launch {
            val vehiclePropertyFlow = vehiclePropertyManager.vehiclePropertyFlow()
            val drivingStateFlow = vehicleDrivingStateManager.drivingStateFlow()
            val uxRestrictionsFlow = vehicleUxRestrictionsManager.uxRestrictionsFlow()
            combine(
                vehiclePropertyFlow,
                drivingStateFlow,
                uxRestrictionsFlow)
            { vehicleProperty, drivingState, uxRestriction ->
                Logger.info("vehicleProperty:$vehicleProperty,drivingState:$drivingState,uxRestrictionL$uxRestriction")
                VehicleState(vehicleProperty, drivingState, uxRestriction)
            }.collect {
                _uiState.emit(MainActivityUiState.Success(it))
            }
        }
    }
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: VehicleState) : MainActivityUiState
}

data class VehicleState(
    val vehicleProperty: List<VehicleProperty>,
    val drivingState: VehicleDrivingStateManager.DrivingState,
    val uxRestrictionMode: VehicleUxRestrictionsManager.UXRestrictionMode,
)
