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

package com.splendo.kaluga.resources.stylable

import com.splendo.kaluga.resources.KalugaColor

data class KalugaBackgroundStyle(
    val fillStyle: FillStyle,
    val strokeStyle: StrokeStyle = StrokeStyle.None,
    val shape: Shape = Shape.Rectangle()
) {

    sealed class FillStyle {
        data class Solid(val color: KalugaColor) : FillStyle()
        data class Gradient(val gradientStyle: GradientStyle) : FillStyle()
    }

    sealed class Shape {
        data class Rectangle(
            val cornerRadiusX: Float,
            val cornerRadiusY: Float,
            val roundedCorners: Set<Corner> = Corner.values().toSet()
        ) : Shape() {

            enum class Corner {
                TOP_LEFT,
                TOP_RIGHT,
                BOTTOM_LEFT,
                BOTTOM_RIGHT
            }

            constructor(cornerRadius: Float = 0.0f, roundedCorners: Set<Corner> = Corner.values().toSet()) : this(cornerRadius, cornerRadius, roundedCorners)
        }
        object Oval : Shape()
    }

    sealed class StrokeStyle {
        object None : StrokeStyle()
        data class Stroke(val width: Float, val color: KalugaColor) : StrokeStyle()
    }
}
