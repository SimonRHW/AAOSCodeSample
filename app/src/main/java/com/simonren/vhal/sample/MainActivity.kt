package com.simonren.vhal.sample

import android.car.VehiclePropertyIds
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.simonren.vhal.sample.car.VehicleAppFocusManager
import com.simonren.vhal.sample.car.VehicleProperty
import com.simonren.vhal.sample.ui.theme.AAOSTheme
import com.simonren.vhal.sample.util.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var vehicleAppFocusManager: VehicleAppFocusManager

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)
        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                        Logger.info("uiState:$uiState")
                    }
                    .collect()
            }
        }
        setContent {
            AAOSTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    ShowVehicleProperty(VehicleProperty(VehiclePropertyIds.INFO_MODEL, "Speedy Model"))
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
