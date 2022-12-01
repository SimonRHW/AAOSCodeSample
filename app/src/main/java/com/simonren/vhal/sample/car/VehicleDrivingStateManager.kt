package com.simonren.vhal.sample.car

import com.simonren.vhal.sample.util.Logger

//import android.car.drivingstate.CarDrivingStateEvent
//import android.car.drivingstate.CarDrivingStateManager

/**
 * @author Simon
 * @desc 行驶状态信息读取与监听,需要 signature|privileged 权限，三方应用不能使用
 */

class VehicleDrivingStateManager(
    private val carProvider: CarProvider,
) {

    init {
        carProvider.providerCar()?.let { car ->
//            carDrivingState(car)
            Logger.info("VehicleDrivingStateManager init carDrivingState")
        } ?: let {
            Logger.warning("car not ready")
        }
    }

//    private var mDrivingStateManager: CarDrivingStateManager? = null

//
//    /**
//     *  requires android.car.permission.CAR_DRIVING_STATE   signature|privileged
//     */
//    private fun carDrivingState(connectedCar: Car) {
//        try {
//            mDrivingStateManager =
//                connectedCar.getCarManager(Car.CAR_DRIVING_STATE_SERVICE) as CarDrivingStateManager?
//            val currentCarDrivingState = mDrivingStateManager?.currentCarDrivingState
//            Logger.info("carDrivingState currentState : ${currentCarDrivingState?.eventValue ?: -1}")
//            mDrivingStateManager?.registerListener { carDrivingStateEvent ->
//                handleDrivingStateChange(carDrivingStateEvent)
//            }
//        } catch (e: Exception) {
//            Logger.warning("carDrivingState fail ${e.message}")
//        }
//    }
//
//    private fun handleDrivingStateChange(carDrivingStateEvent: CarDrivingStateEvent?) {
//        Logger.info("handleDrivingStateChange value ${carDrivingStateEvent?.eventValue ?: -1}")
//        carDrivingStateEvent?.run {
//            when (this.eventValue) {
//                CarDrivingStateEvent.DRIVING_STATE_PARKED -> {
//
//                }
//                CarDrivingStateEvent.DRIVING_STATE_IDLING -> {
//
//                }
//                CarDrivingStateEvent.DRIVING_STATE_MOVING -> {
//
//                }
//                else -> {
//
//                }
//            }
//        }
//    }

    interface DrivingState {
        fun moving()
        fun parked()
        fun idling()
        fun unknown()
    }

}