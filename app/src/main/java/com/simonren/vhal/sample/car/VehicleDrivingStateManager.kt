package com.simonren.vhal.sample.car

import android.car.Car
import android.car.drivingstate.CarDrivingStateEvent
import android.car.drivingstate.CarDrivingStateManager
import com.simonren.vhal.sample.util.Logger


/**
 * @author Simon
 * @desc 行驶状态信息读取与监听,需要 signature|privileged 权限，三方应用不能使用
 */

class VehicleDrivingStateManager(
    private val carProvider: CarProvider,
) : VehicleManager {

    init {
        carProvider.connectCar { car ->
            Logger.info("VehicleDrivingStateManager init")
            carDrivingState(car)
        }
    }

    private var mDrivingStateManager: CarDrivingStateManager? = null
    private var mCurrentCarDrivingState: CarDrivingStateEvent? = null
    var currentState: DrivingState = DrivingState.Unknown

    /**
     *  requires android.car.permission.CAR_DRIVING_STATE   signature|privileged
     */
    private fun carDrivingState(connectedCar: Car) {
        try {
            mDrivingStateManager =
                connectedCar.getCarManager(Car.CAR_DRIVING_STATE_SERVICE) as CarDrivingStateManager?
            mCurrentCarDrivingState = mDrivingStateManager?.currentCarDrivingState
            Logger.info("carDrivingState currentState : ${mCurrentCarDrivingState?.eventValue ?: -1}")
            mDrivingStateManager?.registerListener { carDrivingStateEvent ->
                handleDrivingStateChange(carDrivingStateEvent)
            }
        } catch (e: Exception) {
            Logger.warning("carDrivingState fail ${e.message}")
        }
    }

    private fun handleDrivingStateChange(carDrivingStateEvent: CarDrivingStateEvent?) {
        Logger.info("handleDrivingStateChange value ${carDrivingStateEvent?.eventValue ?: -1}")
        carDrivingStateEvent?.run {
            currentState = when (this.eventValue) {
                CarDrivingStateEvent.DRIVING_STATE_PARKED -> {
                    DrivingState.Parked
                }
                CarDrivingStateEvent.DRIVING_STATE_IDLING -> {
                    DrivingState.Idling
                }
                CarDrivingStateEvent.DRIVING_STATE_MOVING -> {
                    DrivingState.Moving
                }
                else -> {
                    DrivingState.Unknown
                }
            }
        }
    }

    sealed class DrivingState {
        object Moving : DrivingState()
        object Parked : DrivingState()
        object Idling : DrivingState()
        object Unknown : DrivingState()
    }

}