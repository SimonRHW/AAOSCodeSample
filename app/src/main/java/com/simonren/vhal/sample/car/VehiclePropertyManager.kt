package com.simonren.vhal.sample.car

import android.car.Car
import android.car.CarInfoManager
import android.car.VehiclePropertyIds
import android.car.hardware.property.CarPropertyManager
import android.util.ArraySet
import com.simonren.vhal.sample.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Simon
 * @desc 车辆的基本信息读取与监听
 */

class VehiclePropertyManager(
    private var carProvider: CarProvider,
) : VehicleManager {

    private val carPropertyList = mutableListOf<VehicleProperty>()
    private var carPropertyFlow = MutableStateFlow<List<VehicleProperty>>(emptyList())

    init {
        carProvider.connectCar { car ->
            registerCarPropertyListener(car)
            Logger.info("VehiclePropertyManager init")
        }
    }

    fun vehiclePropertyFlow(): StateFlow<List<VehicleProperty>> {
        return carPropertyFlow.asStateFlow()
    }

    fun getProperty(id: Int): VehicleProperty {
        return carPropertyList.first {
            it.id == id
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
                    carPropertyList.add(VehicleProperty(pid, "$value"))
                } catch (e: Exception) {
                    Logger.warning(e)
                }
            }
            carPropertyFlow.tryEmit(carPropertyList.toList())
        } catch (e: Exception) {
            Logger.warning("registerCarPropertyListener :${e.message}")
        }
    }

}