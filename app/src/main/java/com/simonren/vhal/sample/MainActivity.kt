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
import androidx.compose.ui.tooling.preview.Preview
import com.simonren.vhal.sample.car.CarProviderImpl
import com.simonren.vhal.sample.car.VehicleAppFocusManager
import com.simonren.vhal.sample.car.VehiclePropertyManager
import com.simonren.vhal.sample.car.VehicleUxManager
import com.simonren.vhal.sample.ui.theme.VHALSampleTheme

class MainActivity : ComponentActivity() {

    lateinit var vehicleAppFocusManager: VehicleAppFocusManager
    override fun onCreate(savedInstanceState: Bundle?) {
        CarProviderImpl(this).initCar {
            VehiclePropertyManager(it)
            VehicleUxManager(it)
//            vehicleAppFocusManager = VehicleAppFocusManager(it)
        }
        super.onCreate(savedInstanceState)
        setContent {
            VHALSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Greeting("Automotive")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
//        vehicleAppFocusManager.onStart()
    }

    override fun onStop() {
        super.onStop()
//        vehicleAppFocusManager.onStop()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VHALSampleTheme {
        Greeting("Automotive")
    }
}