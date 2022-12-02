package com.simonren.vhal.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.simonren.vhal.sample.car.*
import com.simonren.vhal.sample.ui.theme.AAOSTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var carProvider: CarProvider

    @Inject
    lateinit var vehicleDrivingStateManager: VehicleDrivingStateManager

    @Inject
    lateinit var vehiclePropertyManager: VehiclePropertyManager

    @Inject
    lateinit var vehicleUxRestrictionsManager: VehicleUxRestrictionsManager

    lateinit var vehicleAppFocusManager: VehicleAppFocusManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAOSTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    ShowVehicleProperty(VehicleProperty(286261505,"286261505"))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vehicleUxRestrictionsManager.currentUXRestrictionMode
        vehicleDrivingStateManager.currentState
        vehicleAppFocusManager = VehicleAppFocusManager(carProvider)
        vehicleAppFocusManager.onStart()
    }

    override fun onStop() {
        super.onStop()
        vehicleAppFocusManager.onStop()
    }
}

@Composable
fun ShowVehicleProperty(item: VehicleProperty<String>) {
    Text(text = item.name() + ":" + item.desc())
}
