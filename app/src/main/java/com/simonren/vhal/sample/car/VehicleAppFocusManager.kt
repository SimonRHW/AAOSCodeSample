package com.simonren.vhal.sample.car

import android.car.Car
import android.car.CarAppFocusManager
import com.simonren.vhal.sample.util.Logger

/**
 * @author Simon
 * @desc 车机系统中应用焦点的管理和互斥操作
 * 和界面声明周期绑定
 */
class VehicleAppFocusManager(
    private val carProvider: CarProvider,
) {

    private var mCarAppFocusManager: CarAppFocusManager? = null

    init {
        carProvider.connectCar { car ->
            mCarAppFocusManager = car.getCarManager(Car.APP_FOCUS_SERVICE) as CarAppFocusManager
            Logger.info("VehicleAppFocusManager init")
        }
    }

    private val mFocusListener = CarAppFocusManager.OnAppFocusChangedListener { appType, active ->

        /**
         * Application focus has changed. Note that {@link CarAppFocusManager} instance
         * causing the change will not get this notification.
         *
         * <p>Note that this call can happen for app focus grant, release, and ownership change.
         *
         * @param appType appType where the focus change has happened.
         * @param active {@code true} if there is an active owner for the focus.
         */
        Logger.info("onAppFocusChanged appType $appType ,active = $active")
        if (mCarAppFocusManager?.isOwningFocus(
                mOwnershipListener, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION,
            ) != true
        ) {
            mCarAppFocusManager?.requestAppFocus(CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION,
                mOwnershipListener)
        }
    }

    private val mOwnershipListener = object : CarAppFocusManager.OnAppFocusOwnershipCallback {

        /**
         * 获得焦点的应用才可以启动与焦点相关的动作。
         * @param appType  {@link CarAppFocusManager#APP_FOCUS_TYPE_NAVIGATION}
         */
        override fun onAppFocusOwnershipGranted(appType: Int) {
            Logger.info("onAppFocusOwnershipGranted appType $appType")
        }

        /**
         * 失去焦点所有权，失去焦点应该停止与焦点相关的动作
         * @param appType  {@link CarAppFocusManager#APP_FOCUS_TYPE_NAVIGATION}
         */
        override fun onAppFocusOwnershipLost(appType: Int) {
            Logger.info("onAppFocusOwnershipLost appType $appType")
        }

    }

    fun onStart() {
        if (mCarAppFocusManager?.isOwningFocus(mOwnershipListener,
                CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION) == true
        ) {
            Logger.info("already owning focus")
        } else {
            Logger.info("request focus and add focusListener")
            mCarAppFocusManager?.requestAppFocus(CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION,
                mOwnershipListener)
            mCarAppFocusManager?.addFocusListener(mFocusListener, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION)
        }

    }

    fun onStop() {
        Logger.info("abandon focus and remove focusListener")
        mCarAppFocusManager?.removeFocusListener(mFocusListener, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION)
        mCarAppFocusManager?.abandonAppFocus(mOwnershipListener, CarAppFocusManager.APP_FOCUS_TYPE_NAVIGATION)
    }
}