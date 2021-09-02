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

package com.splendo.kaluga.resources

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Blend Modes as defined by https://www.adobe.com/content/dam/acom/en/devnet/pdf/pdf_reference_archive/blend_modes.pdf
 */
sealed class BlendMode {

    abstract fun blendColor(backdrop: Color, source: Color): Color

    sealed class SeparableBlendMode : BlendMode() {
        override fun blendColor(backdrop: Color, source: Color): Color = colorFrom(
            blendColorChannel(backdrop.red, source.red),
            blendColorChannel(backdrop.green, source.green),
            blendColorChannel(backdrop.blue, source.blue)
        )

        abstract fun blendColorChannel(backdrop: Double, source: Double): Double
    }

    sealed class NonSeparableBlendMode : BlendMode() {
        protected data class UnboundColor(val red: Double, val blue: Double, val green: Double) {
            val lumination: Double get() = 0.3 * red + 0.59 * green + 0.11 * blue
            fun setLumination(lumination: Double): UnboundColor {
                val delta = lumination - this.lumination
                return UnboundColor(red + delta, green + delta, blue + delta)
            }

            val clip: Color get() {
                val lumination = this.lumination
                val min = minOf(red, green, blue)
                val max = maxOf(red, green, blue)
                return when {
                    min < 0.0 -> colorFrom(
                            lumination + (((red - lumination) * lumination) / (lumination - min)),
                            lumination + (((green - lumination) * lumination) / (lumination - min)),
                            lumination + (((blue - lumination) * lumination) / (lumination - min))
                        )
                    max > 1.0 -> colorFrom(
                        lumination + (((red - lumination) * (1.0 - lumination)) / (max - lumination)),
                            lumination + (((green - lumination) * (1.0 - lumination)) / (max - lumination)),
                        lumination + (((blue - lumination) * (1.0 - lumination)) / (max - lumination))
                    )
                    else -> colorFrom(red, green, blue)
                }
            }
        }
        private val Color.unbounded get() = UnboundColor(red, green, blue)
        protected val Color.lumination get() = unbounded.lumination
        protected val Color.saturation: Double get() = maxOf(red, green, blue) - minOf(red, green, blue)
        protected fun Color.setLumination(lumination: Double): Color = unbounded.setLumination(lumination).clip
        protected fun Color.setSaturation(saturation: Double): Color {
            val keyRed = "red"
            val keyGreen = "green"
            val keyBlue = "blue"
            val channels = mapOf(keyRed to red, keyGreen to green, keyBlue to blue).toList().sortedBy { it.second }.toMutableList()
            if (channels[2].second > channels[0].second) {
                val newMiddle = (((channels[1].second - channels[0].second) * saturation) / (channels[2].second - channels[0].second))
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

    object Normal : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = source
    }

    object Multiply : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = backdrop * source
    }

    object Screen : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = backdrop + source - backdrop * source
    }

    object Overlay : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = HardLight.blendColorChannel(source, backdrop)
    }

    object Darken : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = min(backdrop, source)
    }

    object Lighten : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = max(backdrop, source)
    }

    object HardLight : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double {
            return if (source <= 0.5) {
                Multiply.blendColorChannel(backdrop, 2.0 * source)
            } else {
                Screen.blendColorChannel(backdrop, 2.0 * source - 1.0)
            }
        }
    }

    object SoftLight : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double {
            return if (source <= 0.5) {
                backdrop - (1.0 - 2.0 * source) * backdrop * (1.0 - backdrop)
            } else {
                backdrop + (2.0 * source - 1.0) * if (backdrop <= 0.25) {
                    ((16.0 * backdrop - 12.0) * backdrop + 4.0) * backdrop
                } else {
                    sqrt(backdrop)
                }
            }
        }
    }

    object ColorDodge : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = when {
                backdrop == 0.0 -> 0.0
                source == 1.0 -> 1.0
                else -> min(1.0, backdrop / (1.0 - source))
            }
        }

    object ColorBurn : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = when {
            backdrop == 1.0 -> 1.0
            source == 0.0 -> 0.0
            else -> 1.0 - min(1.0, (1.0 - backdrop) / source)
        }
    }

    object Difference : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double = abs(backdrop - source)
    }

    object Exclusion : SeparableBlendMode() {
        override fun blendColorChannel(backdrop: Double, source: Double): Double {
            return backdrop + source - 2.0 * backdrop * source
        }
    }

    object Hue : NonSeparableBlendMode() {
        override fun blendColor(backdrop: Color, source: Color): Color = source
                .setSaturation(backdrop.saturation)
                .setLumination(backdrop.lumination)
    }

    object Saturation : NonSeparableBlendMode() {
        override fun blendColor(backdrop: Color, source: Color): Color = backdrop
            .setSaturation(source.saturation)
            .setLumination(backdrop.lumination)
    }

    object ColorBlend : NonSeparableBlendMode() {
        override fun blendColor(backdrop: Color, source: Color): Color = source.setLumination(backdrop.lumination)
    }

    object Luminosity : NonSeparableBlendMode() {
        override fun blendColor(backdrop: Color, source: Color): Color = backdrop.setLumination(source.lumination)
    }
}

/**
 * Blends two colors according to their [BlendMode]
 * For Alpha Blending the W3 standard is applied: https://www.w3.org/TR/compositing-1/#blending
 */
private fun Color.blend(source: Color, mode: BlendMode): Color {
    val alphaCompose = {
            backdropAlpha: Double,
            sourceAlpha: Double,
            compositeAlpha: Double,
            backdropColor: Double,
            sourceColor: Double,
            compositeColor: Double -> Double
        (1.0 - sourceAlpha / compositeAlpha) * backdropColor +
            (sourceAlpha / compositeAlpha) * ((1 - backdropAlpha) * sourceColor + backdropAlpha * compositeColor)
    }
    val composite = mode.blendColor(this, source)
    val compositeAlpha = source.alpha + alpha - (source.alpha * alpha)
    return colorFrom(
        alphaCompose(alpha, source.alpha, compositeAlpha, red, source.red, composite.red),
        alphaCompose(alpha, source.alpha, compositeAlpha, green, source.green, composite.green),
        alphaCompose(alpha, source.alpha, compositeAlpha, blue, source.blue, composite.blue),
        compositeAlpha
    )
}

infix fun Color.normal(source: Color) = blend(source, BlendMode.Normal)
infix fun Color.plus(source: Color) = blend(source, BlendMode.Normal)
infix fun Color.times(source: Color) = blend(source, BlendMode.Multiply)
infix fun Color.multiply(source: Color) = blend(source, BlendMode.Multiply)
infix fun Color.screen(source: Color) = blend(source, BlendMode.Screen)
infix fun Color.overlay(source: Color) = blend(source, BlendMode.Overlay)
infix fun Color.darken(source: Color) = blend(source, BlendMode.Darken)
infix fun Color.lighten(source: Color) = blend(source, BlendMode.Lighten)
infix fun Color.hardLight(source: Color) = blend(source, BlendMode.HardLight)
infix fun Color.softLight(source: Color) = blend(source, BlendMode.SoftLight)
infix fun Color.dodge(source: Color) = blend(source, BlendMode.ColorDodge)
infix fun Color.burn(source: Color) = blend(source, BlendMode.ColorBurn)
infix fun Color.difference(source: Color) = blend(source, BlendMode.Difference)
infix fun Color.minus(source: Color) = blend(source, BlendMode.Difference)
infix fun Color.exclude(source: Color) = blend(source, BlendMode.Exclusion)
infix fun Color.hue(source: Color) = blend(source, BlendMode.Hue)
infix fun Color.saturate(source: Color) = blend(source, BlendMode.Saturation)
infix fun Color.luminate(source: Color) = blend(source, BlendMode.Luminosity)
infix fun Color.colorBlend(source: Color) = blend(source, BlendMode.ColorBlend)
