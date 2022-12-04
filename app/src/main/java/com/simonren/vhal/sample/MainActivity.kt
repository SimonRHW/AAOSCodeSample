package com.simonren.vhal.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.simonren.vhal.sample.car.*
import com.simonren.vhal.sample.ui.theme.AAOSTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var vehicleAppFocusManager: VehicleAppFocusManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAOSTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    ShowVehicleProperty(VehicleProperty(286261505, "286261505"))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vehicleAppFocusManager.onStart()
    }

    override fun onStop() {
        super.onStop()
        vehicleAppFocusManager.onStop()
    }
}

@Composable
fun ShowVehicleProperty(item: VehicleProperty) {
    Row(modifier = Modifier.padding(8.dp)) {
        Column() {
            Text(
                text = item.name(),
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(
                    text = item.desc(),
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}
