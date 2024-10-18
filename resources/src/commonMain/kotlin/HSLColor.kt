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

package com.splendo.kaluga.resources

import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

/**
 * A color using hue, saturation, and lightness color spaces
 * @property hue the hue channel in a range between `0.0` and `1.0`
 * @property saturation the saturation channel in a range between `0.0` and `1.0`
 * @property lightness the lightness channel in a range between `0.0` and `1.0`
 * @property alpha the alpha channel in a range between `0.0` and `1.0`
 */
@Serializable
data class HSLColor(val hue: Double, val saturation: Double, val lightness: Double, val alpha: Double = 1.0)

/**
 * Gets the [HSLColor] equivalent to this [KalugaColor]
 */
val KalugaColor.hsl: HSLColor get() {
    val max = max(red, max(green, blue))
    val min = min(red, min(green, blue))
    val lightness = (max + min) / 2.0
    val delta = max - min
    return if (delta == 0.0) {
        HSLColor(
            0.0,
            0.0,
            lightness,
        )
    } else {
        val saturation = if (lightness > 0.5) delta / (2.0 - max - min) else delta / (max + min)
        val hue = when (max) {
            red -> (green - blue) / delta + if (green < blue) 6.0 else 0.0
            green -> (blue - red) / delta + 2.0
            else -> (red - green) / delta + 4.0
        }
        HSLColor(
            hue / 6.0,
            saturation,
            lightness,
            alpha,
        )
    }
}

/**
 * Gets the [KalugaColor] equivalent to this [HSLColor]
 */
val HSLColor.color: KalugaColor get() {
    return if (saturation == 0.0) {
        colorFrom(lightness, lightness, lightness)
    } else {
        val q = if (lightness < 0.5) lightness * (1 + saturation) else lightness + saturation - (lightness * saturation)
        val p = (2.0 * lightness) - q
        fun hue2Rgb(p: Double, q: Double, t: Double): Double {
            val valueT = when {
                t < 0 -> t + 1.0
                t > 1 -> t - 1.0
                else -> t
            }

            return when {
                valueT < (1.0 / 6.0) -> p + ((q - p) * 6.0 * valueT)
                valueT < 0.5 -> q
                valueT < (2.0 / 3.0) -> p + ((q - p) * ((2.0 / 3.0) - valueT) * 6.0)
                else -> p
            }
        }
        colorFrom(
            hue2Rgb(p, q, hue + (1.0 / 3.0)),
            hue2Rgb(p, q, hue),
            hue2Rgb(p, q, hue - (1.0 / 3.0)),
            alpha,
        )
    }
}

/**
 * Increases the lightness of a [KalugaColor] by this factor.
 * @param value the amount by which to increase the lightness. Should range between `0.0` and `1.0`
 */
fun KalugaColor.lightenBy(value: Double): KalugaColor = hsl.let {
    return it.copy(lightness = ((1.0 - it.lightness) * value) + it.lightness).color
}

/**
 * Decreases the lightness of a [KalugaColor] by this factor.
 * @param value the amount by which to decrease the lightness. Should range between `0.0` and `1.0`
 */
fun KalugaColor.darkenBy(value: Double): KalugaColor = hsl.let {
    return it.copy(lightness = (it.lightness - (it.lightness) * value)).color
}
