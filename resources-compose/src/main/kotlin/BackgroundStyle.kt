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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.stylable.GradientStyle
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle

/**
 * Creates a modifier for applying a [KalugaBackgroundStyle] to the background of a view
 * @param backgroundStyle the [KalugaBackgroundStyle] to modify with
 * @return a [Modifier] that adds a background according to the [KalugaBackgroundStyle]
 */
fun Modifier.backgroundStyle(backgroundStyle: KalugaBackgroundStyle) = background(
    backgroundStyle.fillStyle.brush,
    backgroundStyle.shape.shape,
).border(backgroundStyle.strokeStyle.borderStroke, backgroundStyle.shape.shape)

/**
 * Gets the [Brush] of a [KalugaBackgroundStyle.FillStyle]
 */
val KalugaBackgroundStyle.FillStyle.brush: Brush get() = when (this) {
    is KalugaBackgroundStyle.FillStyle.Solid -> SolidColor(color.composable)
    is KalugaBackgroundStyle.FillStyle.Gradient -> gradientStyle.brush
}

/**
 * Gets the [Shape] of a [KalugaBackgroundStyle.Shape]
 */
val KalugaBackgroundStyle.Shape.shape: Shape get() = when (this) {
    is KalugaBackgroundStyle.Shape.Rectangle -> {
        val cornerRadiusSize = CornerRadiusSize(cornerRadiusX.dp, cornerRadiusY.dp)
        val cornerRadiusZero = CornerRadiusSize(0.dp, 0.dp)
        RoundedCornerRadiusShape(
            if (roundedCorners.contains(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_LEFT)) {
                cornerRadiusSize
            } else {
                cornerRadiusZero
            },
            if (roundedCorners.contains(KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT)) {
                cornerRadiusSize
            } else {
                cornerRadiusZero
            },
            if (
                roundedCorners.contains(KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_RIGHT)
            ) {
                cornerRadiusSize
            } else {
                cornerRadiusZero
            },
            if (
                roundedCorners.contains(KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT)
            ) {
                cornerRadiusSize
            } else {
                cornerRadiusZero
            },
        )
    }
    is KalugaBackgroundStyle.Shape.Oval -> androidx.compose.foundation.shape.CircleShape
}

val KalugaBackgroundStyle.StrokeStyle.borderStroke: BorderStroke get() = when (this) {
    is KalugaBackgroundStyle.StrokeStyle.None -> BorderStroke(0.0f.dp, Color.Transparent)
    is KalugaBackgroundStyle.StrokeStyle.Stroke -> BorderStroke(width.dp, color.composable)
}

@Preview
@Composable
fun PreviewBackgroundStyle() {
    Box(
        modifier = Modifier.backgroundStyle(
            KalugaBackgroundStyle(
                KalugaBackgroundStyle.FillStyle.Gradient(
                    GradientStyle.Radial(
                        listOf(
                            GradientStyle.ColorPoint(DefaultColors.darkBlue, 0.2f),
                            GradientStyle.ColorPoint(DefaultColors.blue, 0.4f),
                            GradientStyle.ColorPoint(DefaultColors.aliceBlue, 0.6f),
                        ),
                        50.0f,
                        GradientStyle.CenterPoint(0.2f, 0.3f),
                    ),
                ),
                KalugaBackgroundStyle.StrokeStyle.Stroke(1.0f, DefaultColors.black),
                KalugaBackgroundStyle.Shape.Rectangle(
                    4.0f,
                    2.0f,
                    setOf(
                        KalugaBackgroundStyle.Shape.Rectangle.Corner.BOTTOM_LEFT,
                        KalugaBackgroundStyle.Shape.Rectangle.Corner.TOP_RIGHT,
                    ),
                ),
            ),
        ),
    ) {
        Text("Test")
    }
}
