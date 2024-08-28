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
import com.splendo.kaluga.resources.stylable.GradientStyle.Linear.Orientation
import kotlin.jvm.JvmName

/**
 * A style for applying to a Gradient color
 * @property colorPoints the list of [ColorPoint] on the gradient
 */
sealed class GradientStyle(val colorPoints: List<ColorPoint>) {

    /**
     * A point on a [GradientStyle] at which a given [KalugaColor] should be visible
     * @property color the [KalugaColor] at the point
     * @property offset the offset from the start of the gradient at which the point is. Should range between `0.0` and `1.0`
     */
    data class ColorPoint(val color: KalugaColor, val offset: Float)

    /**
     * A point on a [GradientStyle] at which the center of the gradient is located
     * @property x the x coordinate of the center. Should range between `0.0` and `1.0`
     * @property y the y coordinate of the center. Should range between `0.0` and `1.0`
     */
    data class CenterPoint(val x: Float, val y: Float)

    /**
     * A [GradientStyle] that moves linearly in a given [Orientation]
     * @property orientation the [Orientation] of the gradient
     */
    class Linear private constructor(colorPoints: List<ColorPoint>, val orientation: Orientation) : GradientStyle(colorPoints) {

        /**
         * Direction in which the gradient moves
         */
        enum class Orientation {
            /**
             * Gradient starts in the bottom-left corner and ends in the top-right corner
             */
            BOTTOM_LEFT_TOP_RIGHT,

            /**
             * Gradient starts at the bottom and end at the top
             */
            BOTTOM_TOP,

            /**
             * Gradient starts in the top-right corner and ends in the bottom-left corner
             */
            TOP_RIGHT_BOTTOM_LEFT,

            /**
             * Gradient starts at the left side and end at the right side
             */
            LEFT_RIGHT,

            /**
             * Gradient starts at the right side and end at the left side
             */
            RIGHT_LEFT,

            /**
             * Gradient starts in the top-left corner and ends in the bottom-right corner
             */
            TOP_LEFT_BOTTOM_RIGHT,

            /**
             * Gradient starts at the top and end at the bottom
             */
            TOP_BOTTOM,

            /**
             * Gradient starts in the bottom-right corner and ends in the top-left corner
             */
            BOTTOM_RIGHT_TOP_LEFT,
        }

        companion object {

            /**
             * Creates a [Linear] gradient style using a list of [KalugaColor] and an [Orientation]
             * @param colors the list of [KalugaColor] on the gradient. Will be spread evenly across the gradient
             * @param orientation the [Orientation] of the gradient
             * @return the [Linear] gradient
             * @throws [ColorPointRangeError] if [colors] size is smaller than `2`
             */
            @JvmName("fromColors")
            operator fun invoke(colors: List<KalugaColor>, orientation: Orientation = Orientation.LEFT_RIGHT) = Linear(colors.colorPoints, orientation)

            /**
             * Creates a [Linear] gradient style using a list of [ColorPoint] and an [Orientation]
             * @param colorPoints the list of [ColorPoint] on the gradient
             * @param orientation the [Orientation] of the gradient
             * @return the [Linear] gradient
             */
            @JvmName("fromColorPoints")
            operator fun invoke(colorPoints: List<ColorPoint>, orientation: Orientation = Orientation.LEFT_RIGHT) = Linear(colorPoints, orientation)
        }
    }

    /**
     * A [GradientStyle] that radiates out from a [CenterPoint]
     * @property radius the radius in `scalable pixels` at which the gradient stops
     * @property centerPoint the [CenterPoint] from which the gradient radiates out
     */
    class Radial private constructor(colorPoints: List<ColorPoint>, val radius: Float, val centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) : GradientStyle(colorPoints) {
        companion object {

            /**
             * Creates a [Radial] gradient style using a list of [KalugaColor], a range, and a [CenterPoint]
             * @param colors the list of [KalugaColor] on the gradient. Will be spread evenly across the gradient
             * @param radius the radius in `scalable pixels` at which the gradient stops
             * @param centerPoint the [CenterPoint] from which the gradient radiates out
             * @return the [Radial] gradient
             * @throws [ColorPointRangeError] if [colors] size is smaller than `2`
             */
            @JvmName("fromColors")
            operator fun invoke(colors: List<KalugaColor>, radius: Float, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Radial(colors.colorPoints, radius, centerPoint)

            /**
             * Creates a [Radial] gradient style using a list of [ColorPoint], a range, and a [CenterPoint]
             * @param colorPoints the list of [ColorPoint] on the gradient
             * @param radius the radius in `scalable pixels` at which the gradient stops
             * @param centerPoint the [CenterPoint] from which the gradient radiates out
             * @return the [Radial] gradient
             */
            @JvmName("fromColorPoints")
            operator fun invoke(colorPoints: List<ColorPoint>, radius: Float, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Radial(colorPoints, radius, centerPoint)
        }
    }

    /**
     * A [GradientStyle] that rotates around a [CenterPoint]
     * @property centerPoint the [CenterPoint] from which the gradient radiates out
     */
    class Angular private constructor(colorPoints: List<ColorPoint>, val centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) : GradientStyle(colorPoints) {
        companion object {

            /**
             * Creates an [Angular] gradient style using a list of [KalugaColor] and a [CenterPoint]
             * @param colors the list of [KalugaColor] on the gradient. Will be spread evenly across the gradient
             * @param centerPoint the [CenterPoint] from which the gradient radiates out
             * @return the [Angular] gradient
             * @throws [ColorPointRangeError] if [colors] size is smaller than `2`
             */
            @JvmName("fromColors")
            operator fun invoke(colors: List<KalugaColor>, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Angular(colors.colorPoints, centerPoint)

            /**
             * Creates an [Angular] gradient style using a list of [ColorPoint] and a [CenterPoint]
             * @param colorPoints the list of [ColorPoint] on the gradient
             * @param centerPoint the [CenterPoint] from which the gradient radiates out
             * @return the [Angular] gradient
             */
            @JvmName("fromColorPoints")
            operator fun invoke(colorPoints: List<ColorPoint>, centerPoint: CenterPoint = CenterPoint(0.5f, 0.5f)) = Angular(colorPoints, centerPoint)
        }
    }
}

/**
 * An [Error] thrown if the size of a list of [GradientStyle.ColorPoint] is too small
 */
object ColorPointRangeError : Error("Gradient must have at least two colours")

/**
 * Converts a list of [KalugaColor] to a list of [GradientStyle.ColorPoint] spread evenly across the spectrum
 * @throws [ColorPointRangeError] if the list size is smaller than `2`
 */
val List<KalugaColor>.colorPoints: List<GradientStyle.ColorPoint> get() {
    if (size < 2) {
        throw ColorPointRangeError
    }
    val stepSize = 1.0f / (size.toFloat() - 1.0f)
    return mapIndexed { index, color -> GradientStyle.ColorPoint(color, index * stepSize) }
}
