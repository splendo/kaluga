/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.resources.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.stylable.BackgroundStyle
import com.splendo.kaluga.resources.stylable.GradientStyle

fun Modifier.backgroundStyle(backgroundStyle: BackgroundStyle) = background(
    backgroundStyle.fillStyle.brush,
    backgroundStyle.shape.shape
).border(backgroundStyle.strokeStyle.borderStroke, backgroundStyle.shape.shape)

val BackgroundStyle.FillStyle.brush: Brush get() = when (this) {
    is BackgroundStyle.FillStyle.Solid -> SolidColor(color.composable)
    is BackgroundStyle.FillStyle.Gradient -> gradientStyle.brush
}

val BackgroundStyle.Shape.shape: Shape get() = when (this) {
    is BackgroundStyle.Shape.Rectangle -> RoundedCornerRadiusShape(
        if (roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT)) CornerRadiusSize(cornerRadiusX.dp, cornerRadiusY.dp) else CornerRadiusSize(0.dp, 0.dp),
        if (roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT)) CornerRadiusSize(cornerRadiusX.dp, cornerRadiusY.dp) else CornerRadiusSize(0.dp, 0.dp),
        if (roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT)) CornerRadiusSize(cornerRadiusX.dp, cornerRadiusY.dp) else CornerRadiusSize(0.dp, 0.dp),
        if (roundedCorners.contains(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT)) CornerRadiusSize(cornerRadiusX.dp, cornerRadiusY.dp) else CornerRadiusSize(0.dp, 0.dp)
    )
    is BackgroundStyle.Shape.Oval -> androidx.compose.foundation.shape.CircleShape
}

val BackgroundStyle.StrokeStyle.borderStroke: BorderStroke get() = when (this) {
    is BackgroundStyle.StrokeStyle.None -> BorderStroke(0.0f.dp, Color.Transparent)
    is BackgroundStyle.StrokeStyle.Stroke -> BorderStroke(width.dp, color.composable)
}

@Preview
@Composable
fun PreviewBackgroundStyle() {
    Box(
        modifier = Modifier.backgroundStyle(
            BackgroundStyle(
                BackgroundStyle.FillStyle.Gradient(
                    GradientStyle.Radial(
                        listOf(
                            GradientStyle.ColorPoint(DefaultColors.darkBlue, 0.2f),
                            GradientStyle.ColorPoint(DefaultColors.blue, 0.4f),
                            GradientStyle.ColorPoint(DefaultColors.aliceBlue, 0.6f)
                        ),
                        50.0f,
                        GradientStyle.CenterPoint(0.2f, 0.3f)
                    )
                ),
                BackgroundStyle.StrokeStyle.Stroke(1.0f, DefaultColors.black),
                BackgroundStyle.Shape.Rectangle(4.0f, 2.0f, setOf(BackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT, BackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT))
            )
        )
    ) {
        Text("Test")
    }
}
