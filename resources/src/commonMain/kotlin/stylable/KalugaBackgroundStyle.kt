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

/**
 * Style to apply to the background of a view
 * @property fillStyle the [FillStyle] to apply to fill the body of the view
 * @property strokeStyle the [StrokeStyle] to apply to the border of the view
 * @property shape the [Shape] to apply to the view
 */
data class KalugaBackgroundStyle(val fillStyle: FillStyle, val strokeStyle: StrokeStyle = StrokeStyle.None, val shape: Shape = Shape.Rectangle()) {

    /**
     * The style with which the background of a view will be filled
     */
    sealed class FillStyle {

        /**
         * A [FillStyle] where a solid color is used
         * @property color the [KalugaColor] filling the whole background
         */
        data class Solid(val color: KalugaColor) : FillStyle()

        /**
         * A [FillStyle] where a gradient color is used
         * @property gradientStyle the [GradientStyle] to use for the gradient filling the whole background
         */
        data class Gradient(val gradientStyle: GradientStyle) : FillStyle()
    }

    /**
     * The shape applied to the [KalugaBackgroundStyle] of a view
     */
    sealed class Shape {

        /**
         * A rectangular [Shape]
         * @property cornerRadiusX the radius in `scalable pixels` applied to the x-dimension of the corner
         * @property cornerRadiusY the radius in `scalable pixels` applied to the y-dimension of the corner
         * @property roundedCorners the set of [Corner] that are rounded according to [cornerRadiusX] and [cornerRadiusY]
         */
        data class Rectangle(val cornerRadiusX: Float, val cornerRadiusY: Float, val roundedCorners: Set<Corner> = Corner.values().toSet()) : Shape() {

            /**
             * The corners of a [Rectangle] that can be rounded
             */
            enum class Corner {

                /**
                 * The top-left corner
                 */
                TOP_LEFT,

                /**
                 * The top-right corner
                 */
                TOP_RIGHT,

                /**
                 * The bottom-left corner
                 */
                BOTTOM_LEFT,

                /**
                 * The bottom-right corner
                 */
                BOTTOM_RIGHT,
            }

            /**
             * Constructor
             * @param cornerRadius the radius in `scalable pixels` to apply to the corners
             * @param roundedCorners the set of [Corner] that are rounded according to [cornerRadius]
             */
            constructor(cornerRadius: Float = 0.0f, roundedCorners: Set<Corner> = Corner.values().toSet()) : this(cornerRadius, cornerRadius, roundedCorners)
        }

        /**
         * An oval [Shape] matching the width and height of a background view
         */
        data object Oval : Shape()
    }

    /**
     * The style with which the border of the background of a view will be shown
     */
    sealed class StrokeStyle {

        /**
         * A [StrokeStyle] where no stroke is applied
         */
        data object None : StrokeStyle()

        /**
         * A [StrokeStyle] with a solid color
         * @param width the width of the stroke in `scalable pixels`
         * @param color the [KalugaColor] of the stroke
         */
        data class Stroke(val width: Float, val color: KalugaColor) : StrokeStyle()
    }
}
