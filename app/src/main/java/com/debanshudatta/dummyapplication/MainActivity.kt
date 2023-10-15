package com.debanshudatta.dummyapplication

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.debanshudatta.dummyapplication.ui.theme.DummyApplicationTheme
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation

class MainActivity : ComponentActivity() {
    @OptIn(MapboxExperimental::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapInitOptionsFactory = { context ->
                    MapInitOptions(
                        context = context,
                        styleUri = Style.LIGHT,
                        cameraOptions = CameraOptions.Builder()
                            .center(Point.fromLngLat(24.9384, 60.1699))
                            .zoom(12.0)
                            .build()
                    )
                }
            ){
                AddPointer(Point.fromLngLat(24.9384, 60.1699))
            }
        }
    }

    @OptIn(MapboxExperimental::class)
    @Composable
    fun AddPointer(point:Point){
        val drawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.marker,
            null
        )
        val bitmap = drawable!!.toBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        PointAnnotation(
            iconImageBitmap = bitmap,
            iconSize = 0.5,
            point = point,
            onClick = {
                Toast.makeText(
                    this,
                    "Clicked on Circle Annotation: $it",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
        )
    }
}
