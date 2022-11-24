/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.resources.stylable

import com.splendo.kaluga.resources.KalugaColor
import kotlin.jvm.JvmName

sealed class GradientStyle(val colorPoints: List<ColorPoint>) {
    data class ColorPoint(val color: KalugaColor, val offset: Float)
    data class CenterPoint(val x: Float, val y: Float)

    class Linear private constructor (colorPoints: List<ColorPoint>, val orientation: Orientation) : GradientStyle(colorPoints) {
        enum class Orientation {
            BOTTOM_LEFT_TOP_RIGHT,
            BOTTOM_TOP,
            TOP_RIGHT_BOTTOM_LEFT,
            LEFT_RIGHT,
            RIGHT_LEFT,
            TOP_LEFT_BOTTOM_RIGHT,
            TOP_BOTTOM,
            BOTTOM_RIGHT_TOP_LEFT
        }

        companion object {
            @JvmName("fromColors")
            operator fun invoke(colors: List<KalugaColor>, orientation: Orientation = Orientation.LEFT_RIGHT) = Linear(colors.colorPoints, orientation)
            @JvmName("fromColorPoints")
            operator fun invoke(colorPoints: List<ColorPoint>, orientation: Orientation = Orientation.LEFT_RIGHT) = Linear(colorPoints, orientation)
        }
    }

    class Radial(
        colorPoints: List<ColorPoint>,
        val radius: Float,
        val centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)
    ) : GradientStyle(colorPoints) {
        companion object {
            @JvmName("fromColors")
            operator fun invoke(colors: List<KalugaColor>, radius: Float, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Radial(colors.colorPoints, radius, centerPoint)
            @JvmName("fromColorPoints")
            operator fun invoke(colorPoints: List<ColorPoint>, radius: Float, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Radial(colorPoints, radius, centerPoint)
        }
    }

    class Angular(
        colorPoints: List<ColorPoint>,
        val centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)
    ) : GradientStyle(colorPoints) {
        companion object {
            @JvmName("fromColors")
            operator fun invoke(colors: List<KalugaColor>, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Angular(colors.colorPoints, centerPoint)
            @JvmName("fromColorPoints")
            operator fun invoke(colorPoints: List<ColorPoint>, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Angular(colorPoints, centerPoint)
        }
    }
}

val List<KalugaColor>.colorPoints: List<GradientStyle.ColorPoint> get() {
    if (size < 2) {
        throw Error("Gradient must have at least two colours")
    }
    val stepSize = 1.0f / (size.toFloat() - 1.0f)
    return mapIndexed { index, color -> GradientStyle.ColorPoint(color, index * stepSize) }
}
