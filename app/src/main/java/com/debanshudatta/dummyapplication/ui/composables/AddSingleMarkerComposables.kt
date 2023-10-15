package com.debanshudatta.dummyapplication.ui.composables

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.debanshudatta.dummyapplication.R
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation

@OptIn(MapboxExperimental::class)
@Composable
internal fun AddSingleMarkerComposable(point: Point,resources:Resources){
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
            true
        }
    )
}