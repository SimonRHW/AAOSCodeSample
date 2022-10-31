package com.simonren.vhal.sample

import android.car.Car
import android.car.CarInfoManager
import android.car.VehiclePropertyIds
import android.car.drivingstate.CarDrivingStateEvent
import android.car.drivingstate.CarDrivingStateManager
import android.car.drivingstate.CarUxRestrictions
import android.car.drivingstate.CarUxRestrictionsManager
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import android.util.ArraySet

/**
 * @author Simon
 * @desc 驾驶模式、行驶状态、车辆的基本信息读取与监听
 */

class VehicleManager(
    private val context: Context,
) {

    private var mCar: Car? = null
    private var mCarUxRestrictionsManager: CarUxRestrictionsManager? = null
    private var mDrivingStateManager: CarDrivingStateManager? = null
    private var mCurrentUxRestrictions: CarUxRestrictions? = null
    var mUXRestrictionMode: UXRestrictionMode? = null

    fun initCar() {
        mCar = Car.createCar(
            context, null, 0
        ) { car, ready ->
            //ready When {@code true, car service is ready and all accesses are ok.
            //Otherwise car service has crashed or killed and will be restarted.
            Logger.info("VehicleManager initCar ready $ready")
            if (ready) {
                registerCarPropertyListener(car)
                val carUxRestrictions = carUxRestrictions(car)
                Logger.info("VehicleManager initCar carUxRestrictions $carUxRestrictions")
                carDrivingState(car)
            } else {
                //access to car service should stop until car service is ready
            }
        }
        if (mCar == null) {
            Logger.warning("initCar car connection error")
        }
    }

    private fun registerCarPropertyListener(car: Car) {
        try {
            val carInfoManager = car.getCarManager(Car.INFO_SERVICE) as CarInfoManager
            Logger.info("model :${carInfoManager.model}")
            Logger.info("manufacturer:${carInfoManager.manufacturer}")
            Logger.info("modelYearInInteger:${carInfoManager.modelYearInInteger}")
            val carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
            val propertyList = carPropertyManager.getPropertyList(ArraySet<Int>().apply {
                add(VehiclePropertyIds.INFO_DRIVER_SEAT)
                add(VehiclePropertyIds.INFO_FUEL_TYPE)
                add(VehiclePropertyIds.INFO_MAKE)
                add(VehiclePropertyIds.INFO_VIN)
                add(VehiclePropertyIds.INFO_MODEL)
                add(VehiclePropertyIds.INFO_MODEL_YEAR)
                add(VehiclePropertyIds.CURRENT_GEAR)
            })
            Logger.info("propertyList :${propertyList.size}")
            propertyList?.forEach {
                val pid = it.propertyId
                Logger.info("property:${VehiclePropertyIds.toString(it.propertyId)}")
                var areaId = 0
                if (it.areaIds.isNotEmpty()) {
                    areaId = it.areaIds[0]
                }
                try {
                    val value = carPropertyManager.getProperty<Any>(pid, areaId)
                    Logger.info("value :${value}")
                } catch (e: Exception) {
                    Logger.warning(e)
                }
            }
        } catch (e: Exception) {
            Logger.warning("registerCarPropertyListener :${e.message}")
        }
    }

    private fun carUxRestrictions(connectedCar: Car): Int {
        try {
            mCarUxRestrictionsManager =
                connectedCar.getCarManager(Car.CAR_UX_RESTRICTION_SERVICE) as CarUxRestrictionsManager?
            mCarUxRestrictionsManager?.registerListener { carUxRestrictions ->
                mCurrentUxRestrictions = carUxRestrictions
                handleUxRestrictionsChanged(carUxRestrictions)
            }
        } catch (e: Exception) {
            Logger.warning("carUxRestrictions fail ${e.message}")
        }
        mCarUxRestrictionsManager?.currentCarUxRestrictions?.let {
            handleUxRestrictionsChanged(it)
        }
        return mCarUxRestrictionsManager?.currentCarUxRestrictions?.activeRestrictions ?: 0
    }

    private fun handleUxRestrictionsChanged(carUxRestrictions: CarUxRestrictions) {
        Logger.info("handleUxRestrictionsChanged value ${carUxRestrictions.activeRestrictions}")
        mUXRestrictionMode = UXRestrictionMode(carUxRestrictions.activeRestrictions)
        mUXRestrictionMode?.let {
            // TODO:  publishMode(it)
        }
    }

    class UXRestrictionMode(val currentMode: Int) {

        /**
         * @see CarUxRestrictions
         * @param blockMode Int  UX_RESTRICTIONS_LIMIT_CONTENT,UX_RESTRICTIONS_NO_VIDEO
         * @return Boolean true 表示对应模式受到限制
         */
        fun blockPageWithMode(blockMode: Int): Boolean {
            return if (currentMode == CarUxRestrictions.UX_RESTRICTIONS_BASELINE) {
                false
            } else {
                blockMode and currentMode != 0
            }
        }

        /**
         * 判断是否限制视频播放
         * @return Boolean true 表示不能播放视频
         */
        fun blockVideo(): Boolean {
            return blockPageWithMode(CarUxRestrictions.UX_RESTRICTIONS_NO_VIDEO)
        }
    }

    /**
     *  requires android.car.permission.CAR_DRIVING_STATE   signature|privileged
     */
    private fun carDrivingState(connectedCar: Car) {
        try {
            mDrivingStateManager =
                connectedCar.getCarManager(Car.CAR_DRIVING_STATE_SERVICE) as CarDrivingStateManager?
            val currentCarDrivingState = mDrivingStateManager?.currentCarDrivingState
            Logger.info("carDrivingState currentState : ${currentCarDrivingState?.eventValue ?: -1}")
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
            when (this.eventValue) {
                CarDrivingStateEvent.DRIVING_STATE_PARKED -> {

                }
                CarDrivingStateEvent.DRIVING_STATE_IDLING -> {

                }
                CarDrivingStateEvent.DRIVING_STATE_MOVING -> {

                }
                else -> {

                }
            }
        }
    }

    interface DrivingState {
        fun moving()
        fun parked()
        fun idling()
        fun unknown()
    }

}