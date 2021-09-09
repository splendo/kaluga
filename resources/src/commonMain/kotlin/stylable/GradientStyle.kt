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

import com.splendo.kaluga.resources.Color

sealed class GradientStyle(val colorPoints: List<ColorPoint>) {
    data class ColorPoint(val color: Color, val offset: Float)
    data class CenterPoint(val x: Float, val y: Float)

    class Linear(colorPoints: List<ColorPoint>, val orientation: Orientation = Orientation.LEFT_RIGHT) : GradientStyle(colorPoints) {
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
    }

    class Radial(
        colorPoints: List<ColorPoint>,
        val radius: Float,
        val centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)
    ) : GradientStyle(colorPoints)

    class Angular(
        colorPoints: List<ColorPoint>,
        val centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)
    ) : GradientStyle(colorPoints)
}

val List<Color>.colorPoints: List<GradientStyle.ColorPoint> get() {
    if (size < 2) {
        throw Error("Gradient must have at least two colours")
    }
    val stepSize = 1.0f/(size.toFloat() - 1.0f)
    return mapIndexed { index, color -> GradientStyle.ColorPoint(color, index * stepSize) }
}