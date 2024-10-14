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

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Blend Modes as defined by https://helpx.adobe.com/photoshop/using/blending-modes.html
 */
sealed class BlendMode {

    /**
     * Blends two [KalugaColor] into a new [KalugaColor] using this [BlendMode]
     * @param backdrop the [KalugaColor] at the base layer
     * @param source the [KalugaColor] at the top layer
     * @return a [KalugaColor] that is the result of [backdrop] and [source] being blended using this Blend mode
     */
    abstract fun blendColor(backdrop: KalugaColor, source: KalugaColor): KalugaColor

    /**
     * A [BlendMode] where each color channel can be blended independently
     */
    sealed class SeparableBlendMode : BlendMode() {
        override fun blendColor(backdrop: KalugaColor, source: KalugaColor): KalugaColor = colorFrom(
            blendColorChannel(backdrop.red, source.red),
            blendColorChannel(backdrop.green, source.green),
            blendColorChannel(backdrop.blue, source.blue),
        )

        /**
         * Blends the color of a single channel
         * @param backdrop the value for the color in the channel at the base layer
         * @param source the value for the color in the channel at the top layer
         * @return the value for the color in the channel blended using this Blend mode
         */
        abstract fun blendColorChannel(backdrop: Double, source: Double): Double
    }

    /**
     * A [BlendMode] where the color channels cannot be blended by changing the Lumination and Saturation
     */
    sealed class NonSeparableBlendMode : BlendMode() {
        protected data class UnboundColor(val red: Double, val blue: Double, val green: Double) {
            val lumination: Double get() = 0.3 * red + 0.59 * green + 0.11 * blue
            fun setLumination(lumination: Double): UnboundColor {
                val delta = lumination - this.lumination
                return UnboundColor(red + delta, green + delta, blue + delta)
            }

            val clip: KalugaColor
                get() {
                    val lumination = this.lumination
                    val min = minOf(red, green, blue)
                    val max = maxOf(red, green, blue)
                    return when {
                        min < 0.0 -> colorFrom(
                            lumination + (((red - lumination) * lumination) / (lumination - min)),
                            lumination + (((green - lumination) * lumination) / (lumination - min)),
                            lumination + (((blue - lumination) * lumination) / (lumination - min)),
                        )
                        max > 1.0 -> colorFrom(
                            lumination + (((red - lumination) * (1.0 - lumination)) / (max - lumination)),
                            lumination + (((green - lumination) * (1.0 - lumination)) / (max - lumination)),
                            lumination + (((blue - lumination) * (1.0 - lumination)) / (max - lumination)),
                        )
                        else -> colorFrom(red, green, blue)
                    }
                }
        }

        private val KalugaColor.unbounded get() = UnboundColor(red, green, blue)
        protected val KalugaColor.lumination get() = unbounded.lumination
        protected val KalugaColor.saturation: Double
            get() = maxOf(red, green, blue) - minOf(
                red,
                green,
                blue,
            )

        protected fun KalugaColor.setLumination(lumination: Double): KalugaColor = unbounded.setLumination(lumination).clip

        protected fun KalugaColor.setSaturation(saturation: Double): KalugaColor {
            val keyRed = "red"
            val keyGreen = "green"
            val keyBlue = "blue"
            val channels = mapOf(keyRed to red, keyGreen to green, keyBlue to blue).toList()
                .sortedBy { it.second }.toMutableList()
            if (channels[2].second > channels[0].second) {
                val newMiddle =
                    (((channels[1].second - channels[0].second) * saturation) / (channels[2].second - channels[0].second))
                channels[1] = channels[1].copy(second = newMiddle)
                channels[2] = channels[2].copy(second = saturation)
            } else {
                channels[1] = channels[1].copy(second = 0.0)
                channels[2] = channels[2].copy(second = 0.0)
            }
            channels[0] = channels[0].copy(second = 0.0)
            val channelsMap = channels.toMap()
            return colorFrom(channelsMap[keyRed]!!, channelsMap[keyGreen]!!, channelsMap[keyBlue]!!)
        }
    }

    /**
     * A [SeparableBlendMode] where only the top layer is used
     */
    data object Normal : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = source
    }

    /**
     * A [SeparableBlendMode] where the top layer is multiplied with the base layer
     */
    data object Multiply : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = backdrop * source
    }

    /**
     * A [SeparableBlendMode] where the colors are inverted, multiplied and then inverted again
     */
    data object Screen : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = backdrop + source - backdrop * source
    }

    /**
     * A [SeparableBlendMode] where if the base layer is light, the top layer becomes lighter; where the base layer is dark, the top becomes darker
     */
    data object Overlay : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = HardLight.blendColorChannel(source, backdrop)
    }

    /**
     * A [SeparableBlendMode] where for each channel the darkest of the base layer and top later is used
     */
    data object Darken : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = min(backdrop, source)
    }

    /**
     * A [SeparableBlendMode] where for each channel the lightest of the base layer and top later is used
     */
    data object Lighten : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = max(backdrop, source)
    }

    /**
     * A [SeparableBlendMode] where if the source color is lighter than 50% gray, the color is lightened, as if [Screen] was applied. If the source color is darker than 50% gray, the image is darkened, as if [Multiply] was applied
     */
    data object HardLight : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = if (source <= 0.5) {
            Multiply.blendColorChannel(backdrop, 2.0 * source)
        } else {
            Screen.blendColorChannel(backdrop, 2.0 * source - 1.0)
        }
    }

    /**
     * A [SeparableBlendMode] where if the source color is lighter than 50% gray, the color is lightened, as if [ColorDodge] was applied. If the source color is darker than 50% gray, the image is darkened, as if [ColorBurn] was applied
     */
    data object SoftLight : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = if (source <= 0.5) {
            backdrop - (1.0 - 2.0 * source) * backdrop * (1.0 - backdrop)
        } else {
            backdrop + (2.0 * source - 1.0) * if (backdrop <= 0.25) {
                ((16.0 * backdrop - 12.0) * backdrop + 4.0) * backdrop
            } else {
                sqrt(backdrop)
            }
        }
    }

    /**
     * A [SeparableBlendMode] where blending looks at the color information in each channel and brightens the base color to reflect the blend color by decreasing contrast between the two
     */
    data object ColorDodge : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = when {
            backdrop == 0.0 -> 0.0
            source == 1.0 -> 1.0
            else -> min(1.0, backdrop / (1.0 - source))
        }
    }

    /**
     * A [SeparableBlendMode] where blending looks at the color information in each channel and darkens the base color to reflect the blend color by increasing the contrast between the two
     */
    data object ColorBurn : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = when {
            backdrop == 1.0 -> 1.0
            source == 0.0 -> 0.0
            else -> 1.0 - min(1.0, (1.0 - backdrop) / source)
        }
    }

    /**
     * A [SeparableBlendMode] where blending looks at the color information in each channel and subtracts either the source color from the base color or the base color from the source color, depending on which has the greater brightness value
     */
    data object Difference : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = abs(backdrop - source)
    }

    /**
     * A [SeparableBlendMode] similar to [Difference] but lower in contrast
     */
    data object Exclusion : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = backdrop + source - 2.0 * backdrop * source
    }

    /**
     * A [NonSeparableBlendMode] that creates a result color with the luminance and saturation of the base color and the hue of the source color
     */
    data object Hue : NonSeparableBlendMode() {
        override fun blendColor(backdrop: KalugaColor, source: KalugaColor): KalugaColor = source
            .setSaturation(backdrop.saturation)
            .setLumination(backdrop.lumination)
    }

    /**
     * A [NonSeparableBlendMode] that creates a result color with the luminance and hue of the base color and the saturation of the source color
     */
    data object Saturation : NonSeparableBlendMode() {
        override fun blendColor(backdrop: KalugaColor, source: KalugaColor): KalugaColor = backdrop
            .setSaturation(source.saturation)
            .setLumination(backdrop.lumination)
    }

    /**
     * A [NonSeparableBlendMode] that creates a result color with the luminance of the base color and the hue and saturation of the source color
     */
    data object ColorBlend : NonSeparableBlendMode() {
        override fun blendColor(backdrop: KalugaColor, source: KalugaColor): KalugaColor = source.setLumination(backdrop.lumination)
    }

    /**
     * A [NonSeparableBlendMode] that creates a result color with the hue and saturation of the base color and the luminance of the source color
     */
    data object Luminosity : NonSeparableBlendMode() {
        override fun blendColor(backdrop: KalugaColor, source: KalugaColor): KalugaColor = backdrop.setLumination(source.lumination)
    }
}

/**
 * Blends two colors according to their [BlendMode]
 * For Alpha Blending the W3 standard is applied: https://www.w3.org/TR/compositing-1/#blending
 */
private fun KalugaColor.blend(source: KalugaColor, mode: BlendMode): KalugaColor {
    val alphaCompose = {
            backdropAlpha: Double,
            sourceAlpha: Double,
            compositeAlpha: Double,
            backdropColor: Double,
            sourceColor: Double,
            compositeColor: Double,
        ->
        Double
        (1.0 - sourceAlpha / compositeAlpha) * backdropColor +
            (sourceAlpha / compositeAlpha) * ((1 - backdropAlpha) * sourceColor + backdropAlpha * compositeColor)
    }
    val composite = mode.blendColor(this, source)
    val compositeAlpha = source.alpha + alpha - (source.alpha * alpha)
    return colorFrom(
        alphaCompose(alpha, source.alpha, compositeAlpha, red, source.red, composite.red),
        alphaCompose(alpha, source.alpha, compositeAlpha, green, source.green, composite.green),
        alphaCompose(alpha, source.alpha, compositeAlpha, blue, source.blue, composite.blue),
        compositeAlpha,
    )
}

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Normal]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Normal] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.normal(source: KalugaColor) = blend(source, BlendMode.Normal)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Multiply]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Multiply] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.multiply(source: KalugaColor) = blend(source, BlendMode.Multiply)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Screen]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Screen] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.screen(source: KalugaColor) = blend(source, BlendMode.Screen)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Overlay]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Overlay] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.overlay(source: KalugaColor) = blend(source, BlendMode.Overlay)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Darken]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Darken] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.darken(source: KalugaColor) = blend(source, BlendMode.Darken)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Lighten]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Lighten] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.lighten(source: KalugaColor) = blend(source, BlendMode.Lighten)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.HardLight]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.HardLight] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.hardLight(source: KalugaColor) = blend(source, BlendMode.HardLight)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.SoftLight]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.SoftLight] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.softLight(source: KalugaColor) = blend(source, BlendMode.SoftLight)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.ColorDodge]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.ColorDodge] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.dodge(source: KalugaColor) = blend(source, BlendMode.ColorDodge)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.ColorBurn]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.ColorBurn] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.burn(source: KalugaColor) = blend(source, BlendMode.ColorBurn)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Difference]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Difference] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.difference(source: KalugaColor) = blend(source, BlendMode.Difference)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Exclusion]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Exclusion] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.exclude(source: KalugaColor) = blend(source, BlendMode.Exclusion)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Hue]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Hue] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.hue(source: KalugaColor) = blend(source, BlendMode.Hue)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Saturation]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Saturation] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.saturate(source: KalugaColor) = blend(source, BlendMode.Saturation)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.Luminosity]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.Luminosity] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.luminate(source: KalugaColor) = blend(source, BlendMode.Luminosity)

/**
 * Blends a [KalugaColor] with another color using [BlendMode.ColorBlend]
 * @param source the [KalugaColor] that serves as the source color
 * @return a [KalugaColor] that is the result of [BlendMode.ColorBlend] being applied with this [KalugaColor] as the base color and [source] as the source color
 */
infix fun KalugaColor.colorBlend(source: KalugaColor) = blend(source, BlendMode.ColorBlend)
