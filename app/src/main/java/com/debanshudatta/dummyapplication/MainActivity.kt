package com.debanshudatta.dummyapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.debanshudatta.dummyapplication.ui.composables.AddSingleMarkerComposable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import dev.shreyaspatil.permissionflow.compose.rememberPermissionFlowRequestLauncher
import dev.shreyaspatil.permissionflow.compose.rememberPermissionState
import kotlinx.coroutines.launch

internal class MainActivity : ComponentActivity() {
    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionList = listOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val permissionLauncher = rememberPermissionFlowRequestLauncher()
            val state by rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
            var currentLocation: Point? by remember { mutableStateOf(null) }
            val mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    center(Point.fromLngLat(0.0, 0.0))
                    zoom(1.0)
                    pitch(0.0)
                }
            }

            LaunchedEffect(state){
                coroutineScope.launch {
                    if(state.isGranted) {
                        currentLocation = LocationService.getCurrentLocation(context)
                        val mapAnimationOptions =
                            MapAnimationOptions.Builder().duration(1500L).build()
                        mapViewportState.flyTo(
                            CameraOptions.Builder()
                                .center(currentLocation)
                                .zoom(12.0)
                                .build(),
                            mapAnimationOptions
                        )
                    }
                }
            }

            Column {
                if (state.isGranted) {
                    //TODO: adding search section
                } else {
                    Button(onClick = { permissionLauncher.launch(permissionList.toTypedArray()) }) {
                        Text("Request Permissions")
                    }
                }
                MainMapViewComposable(mapViewportState, currentLocation)
            }
        }
    }

    @Composable
    @OptIn(MapboxExperimental::class)
    private fun MainMapViewComposable(
        mapViewportState: MapViewportState,
        currentLocation: Point?
    ) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            mapInitOptionsFactory = { context ->
                MapInitOptions(
                    context = context,
                    styleUri = Style.TRAFFIC_DAY,
                    cameraOptions = CameraOptions.Builder()
                        .center(Point.fromLngLat(24.9384, 60.1699))
                        .zoom(12.0)
                        .build()
                )
            }
        ) {
            currentLocation?.let { AddSingleMarkerComposable(it, resources) }
        }
    }
}
