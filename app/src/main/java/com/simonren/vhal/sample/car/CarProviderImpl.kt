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

typealias CarReady = (CarProvider) -> Unit

class CarProviderImpl(
    private val context: Context,
) : CarProvider {

    private var mCar: Car? = null
    private val carAccessOwners = mutableListOf<CarProvider.CarAccessOwner>()

    fun initCar(action: CarReady) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            mCar = Car.createCar(context, object : ServiceConnection {

                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    Logger.info("onServiceConnected name:$name, service:$service")
                    action.invoke(this@CarProviderImpl)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    Logger.info("onServiceConnected name:$name")
                    //access to car service should stop until car service is ready
                    stopCarServiceAccess()
                }
            }
            )
            mCar?.connect()
        } else {
            mCar = Car.createCar(context, null, 0
            ) { car, ready ->
                //ready When {@code true, car service is ready and all accesses are ok.
                //Otherwise car service has crashed or killed and will be restarted.
                Logger.info("initCar ready $ready")
                if (ready) {
                    mCar = car
                    action.invoke(this@CarProviderImpl)
                } else {
                    //access to car service should stop until car service is ready
                    stopCarServiceAccess()
                }
            }
            if (mCar == null) {
                Logger.warning("initCar car connection error")
            }
        }
    }

    /**
     *  //access to car service should stop until car service is ready
     * @return Car?
     */
    override fun providerCar(): Car? {
        return mCar
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