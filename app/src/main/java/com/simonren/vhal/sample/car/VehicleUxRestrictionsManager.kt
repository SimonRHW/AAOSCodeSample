package com.simonren.vhal.sample.car

import android.car.Car
import android.car.drivingstate.CarUxRestrictions
import android.car.drivingstate.CarUxRestrictionsManager
import com.simonren.vhal.sample.util.Logger

/**
 * @author Simon
 * @desc 驾驶模式信息读取与监听
 */

class VehicleUxRestrictionsManager(
    private val carProvider: CarProvider,
) :VehicleManager{

    private var mCarUxRestrictionsManager: CarUxRestrictionsManager? = null
    private var mCurrentUxRestrictions: CarUxRestrictions? = null
    var currentUXRestrictionMode: UXRestrictionMode? = null

    //todo publish ux
    init {
        carProvider.connectCar { car ->
            Logger.info("VehicleUxManager init")
            carUxRestrictions(car)
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
            mCurrentUxRestrictions = it
            handleUxRestrictionsChanged(it)
        }
        return mCarUxRestrictionsManager?.currentCarUxRestrictions?.activeRestrictions ?: 0
    }

    private fun handleUxRestrictionsChanged(carUxRestrictions: CarUxRestrictions) {
        Logger.info("handleUxRestrictionsChanged value ${carUxRestrictions.activeRestrictions}")
        currentUXRestrictionMode = UXRestrictionMode(carUxRestrictions.activeRestrictions)
        currentUXRestrictionMode?.let {
            // TODO:publishMode(it)
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

}