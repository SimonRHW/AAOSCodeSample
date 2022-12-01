package com.simonren.vhal.sample.car

import android.car.Car
import android.car.CarInfoManager
import android.car.VehiclePropertyIds
import android.car.hardware.property.CarPropertyManager
import android.util.ArraySet
import com.simonren.vhal.sample.util.Logger

/**
 * @author Simon
 * @desc 车辆的基本信息读取与监听
 */

class VehiclePropertyManager(
    private var carProvider: CarProvider,
) {

    init {
        carProvider.providerCar()?.let { car ->
            registerCarPropertyListener(car)
            Logger.info("VehiclePropertyManager init registerCarPropertyListener")
        } ?: let {
            Logger.warning("car not ready")
        }
    }

    private fun registerCarPropertyListener(car: Car) {
        try {
            val carInfoManager = car.getCarManager(Car.INFO_SERVICE) as CarInfoManager
            Logger.info("model :${carInfoManager.model}")
            Logger.info("manufacturer:${carInfoManager.manufacturer}")
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

}