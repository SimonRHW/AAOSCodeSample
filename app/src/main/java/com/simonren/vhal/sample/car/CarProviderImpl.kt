package com.simonren.vhal.sample.car

import android.car.Car
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.simonren.vhal.sample.util.Logger

/**
 * @author Simon
 * @desc Car创建与声明周期管理
 */


class CarProviderImpl(
    private val context: Context,
) : CarProvider {

    private var mCar: Car? = null
    private val carAccessOwners = mutableListOf<CarProvider.CarAccessOwner>()

    /**
     * 0 : disconnected
     * 1 : connecting
     * 2 : connected
     */
    private var mConnectionState = 0

    override fun connectCar(readyAction: CarReady) {
        if (mCar != null && mConnectionState == 2) {
            readyAction.invoke(mCar!!)
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                mCar = Car.createCar(context, object : ServiceConnection {

                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        Logger.info("onServiceConnected name:$name, service:$service")
                        mConnectionState = 2
                        readyAction.invoke(mCar!!)
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        Logger.info("onServiceConnected name:$name")
                        //access to car service should stop until car service is ready
                        stopCarServiceAccess()
                        mConnectionState = 0
                    }
                }
                )
                mConnectionState = 1
                try {
                    mCar?.connect()
                } catch (e: IllegalStateException) {
                    Logger.warning(e)
                }
            } else {
                mCar = Car.createCar(context, null, 0
                ) { car, ready ->
                    //ready When {@code true, car service is ready and all accesses are ok.
                    //Otherwise car service has crashed or killed and will be restarted.
                    Logger.info("createCar ready $ready, mConnectionState:$mConnectionState")
                    if (ready) {
                        mConnectionState = 2
                        mCar = car
                        readyAction.invoke(mCar!!)
                    } else {
                        mConnectionState = 0
                        //access to car service should stop until car service is ready
                        stopCarServiceAccess()
                    }
                }
            }
        }
    }

    override fun registerCarLifecycle(carAccessOwner: CarProvider.CarAccessOwner) {
        carAccessOwners.add(carAccessOwner)
    }

    private fun stopCarServiceAccess() {
        carAccessOwners.forEach {
            it.unavailable()
        }
    }
}