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

@file:JvmName("ColorCommonKt")

package com.splendo.kaluga.resources

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmName

/**
 * Class describing a color
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect sealed class KalugaColor {

    /**
     * A [KalugaColor] that is represented by a single rgb-value
     */
    class RGBColor : KalugaColor

    /**
     * A [KalugaColor] that has a different [RGBColor] for [isInDarkMode] being `true` or `false`.
     * @property defaultColor the [RGBColor] to use when [isInDarkMode] is `false`.
     * @property darkColor the [RGBColor] to use when [isInDarkMode] is `true`.
     */
    class DarkLightColor : KalugaColor {
        val defaultColor: RGBColor
        val darkColor: RGBColor
    }
}

/**
 * Gets the [KalugaColor.RGBColor] [forDarkMode]
 * @param forDarkMode if `true` will return the [KalugaColor.RGBColor] to use when [isInDarkMode] is true,
 * otherwise returns the default color.
 * @return the [KalugaColor.RGBColor] associated with [forDarkMode]
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.rgbColor(forDarkMode: Boolean) = if (forDarkMode) darkColor else defaultColor

/**
 * A wrapper for [KalugaColor] that allows it to be serialized
 * @property color the [KalugaColor] to wrap
 */
@Serializable(with = ColorSerializer::class)
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
data class SerializableColor(val color: KalugaColor)

/**
 * A wrapper for [KalugaColor.RGBColor] that allows it to be serialized
 * @property color the [KalugaColor.RGBColor] to wrap
 */
@Serializable(with = RGBColorSerializer::class)
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
data class SerializableRGBColor(val color: KalugaColor.RGBColor)

/**
 * Gets a [SerializableColor] instance of this color
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
val KalugaColor.serializable get() = SerializableColor(this)

/**
 * Gets a [SerializableRGBColor] instance of this color
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
val KalugaColor.RGBColor.serializable get() = SerializableRGBColor(this)

/**
 * Gets the red value of the color in a range between `0.0` and `1.0`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.red: Double

/**
 * Gets the red value of the color [forDarkMode] in a range between `0.0` and `1.0`
 * @param forDarkMode if `true` will return the red value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.red(forDarkMode: Boolean) = rgbColor(forDarkMode).red

/**
 * Gets the red value of the color in a range between `0` and `255`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.redInt: Int

/**
 * Gets the red value of the color [forDarkMode] in a range between `0` and `255`
 * @param forDarkMode if `true` will return the red value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.redInt(forDarkMode: Boolean) = rgbColor(forDarkMode).redInt

/**
 * Gets the green value of the color in a range between `0.0` and `1.0`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.green: Double

/**
 * Gets the green value of the color [forDarkMode] in a range between `0.0` and `1.0`
 * @param forDarkMode if `true` will return the green value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.green(forDarkMode: Boolean) = rgbColor(forDarkMode).green

/**
 * Gets the green value of the color in a range between `0` and `255`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.greenInt: Int

/**
 * Gets the green value of the color [forDarkMode] in a range between `0` and `255`
 * @param forDarkMode if `true` will return the green value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.greenInt(forDarkMode: Boolean) = rgbColor(forDarkMode).greenInt

/**
 * Gets the blue value of the color in a range between `0.0` and `1.0`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.blue: Double

/**
 * Gets the blue value of the color [forDarkMode] in a range between `0.0` and `1.0`
 * @param forDarkMode if `true` will return the blue value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.blue(forDarkMode: Boolean) = rgbColor(forDarkMode).blue

/**
 * Gets the blue value of the color in a range between `0` and `255`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.blueInt: Int

/**
 * Gets the blue value of the color [forDarkMode] in a range between `0` and `255`
 * @param forDarkMode if `true` will return the blue value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.blueInt(forDarkMode: Boolean) = rgbColor(forDarkMode).blueInt

/**
 * Gets the alpha value of the color in a range between `0.0` and `1.0`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.alpha: Double

/**
 * Gets the alpha value of the color [forDarkMode] in a range between `0.0` and `1.0`
 * @param forDarkMode if `true` will return the alpha value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.alpha(forDarkMode: Boolean) = rgbColor(forDarkMode).alpha

/**
 * Gets the alpha value of the color in a range between `0` and `255`
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect val KalugaColor.RGBColor.alphaInt: Int

/**
 * Gets the alpha value of the color [forDarkMode] in a range between `0` and `255`
 * @param forDarkMode if `true` will return the alpha value of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.alphaInt(forDarkMode: Boolean) = rgbColor(forDarkMode).alphaInt

/**
 * Creates a [KalugaColor.RGBColor] using red, green, blue, and (optional) alpha, all ranging between `0.0` and `1.0`.
 * @param red The red color value ranging between `0.0` and `1.0`.
 * @param green The green color value ranging between `0.0` and `1.0`.
 * @param blue The blue color value ranging between `0.0` and `1.0`.
 * @param alpha The alpha color value ranging between `0.0` and `1.0`. Defaults to `1.0`
 * @return The [KalugaColor.RGBColor] with the corresponding red, green, blue, and alpha values
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double = 1.0): KalugaColor.RGBColor

/**
 * Creates a [KalugaColor.RGBColor] using red, green, blue, and (optional) alpha, all ranging between `0` and `255`.
 * @param redInt The red color value ranging between `0` and `255`.
 * @param greenInt The green color value ranging between `0` and `255`.
 * @param blueInt The blue color value ranging between `0` and `255`.
 * @param alphaInt The alpha color value ranging between `0` and `255`. Defaults to `255`
 * @return The [KalugaColor.RGBColor] with the corresponding red, green, blue, and alpha values
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int = 255): KalugaColor.RGBColor

/**
 * Attempts to parse a given [String] into a [KalugaColor.RGBColor].
 * The string should be formatted as either `#AARRGGBB` or `#RRGGBB` for the parsing to succeed.
 * @param hexString The [String] to parse as a [KalugaColor.RGBColor]
 * @return The [KalugaColor.RGBColor] associated with [hexString] or `null` if improperly formatted.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun colorFrom(hexString: String): KalugaColor.RGBColor? = if (hexString.startsWith('#')) {
    val hexColor = hexString.substring(1).toLong(16)
    when (hexString.length) {
        9 -> {
            val alpha = hexColor ushr 24
            val red = (hexColor shr 16) and 0xFF
            val green = (hexColor shr 8) and 0xFF
            val blue = hexColor and 0xFF
            colorFrom(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())
        }

        7 -> {
            val red = hexColor ushr 16
            val green = (hexColor shr 8) and 0xFF
            val blue = hexColor and 0xFF
            colorFrom(red.toInt(), green.toInt(), blue.toInt())
        }

        else -> null
    }
} else {
    null
}

/**
 * Creates a [KalugaColor.DarkLightColor] that uses [darkModeColor] when [isInDarkMode] and this color otherwise.
 * If this color has a dark mode already it will be overwritten.
 * If [darkModeColor] has a dark mode, it will be used.
 * @param darkModeColor the [KalugaColor.RGBColor] to use when [isInDarkMode]
 * @return a [KalugaColor.DarkLightColor] that supports a custom color in dark mode.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
expect infix fun KalugaColor.RGBColor.withDarkMode(darkModeColor: KalugaColor.RGBColor): KalugaColor.DarkLightColor

/**
 * The inverted [KalugaColor.RGBColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
val KalugaColor.RGBColor.inverted: KalugaColor.RGBColor get() = colorFrom(1.0 - red, 1.0 - green, 1.0 - blue, alpha)

/**
 * The inverted [KalugaColor.DarkLightColor]. Each value will be inverted individually.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
val KalugaColor.DarkLightColor.inverted get() = defaultColor.inverted withDarkMode darkColor.inverted

/**
 * Creates a [KalugaColor.DarkLightColor] from another by using the [KalugaColor.DarkLightColor.darkColor] as the default color and vice versa.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
val KalugaColor.DarkLightColor.flipped get() = darkColor withDarkMode defaultColor

/**
 * The hex string representing this color.
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
val KalugaColor.RGBColor.hexString: String
    get() {
        return "#${alphaInt.toHex(2)}${redInt.toHex(2)}${greenInt.toHex(2)}${blueInt.toHex(2)}"
    }

/**
 * Gets the hex string representing the color [forDarkMode]
 * @param forDarkMode if `true` will return the hex string representation of [KalugaColor.DarkLightColor.darkColor], otherwise the value of [KalugaColor.DarkLightColor.defaultColor].
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
fun KalugaColor.DarkLightColor.hexString(forDarkMode: Boolean) = rgbColor(forDarkMode).hexString

private fun Int.toHex(minSize: Int): String {
    val hexValue = this.toString(16)
    val prefix = List(minSize - hexValue.length) { "0" }
    return listOf(*prefix.toTypedArray(), hexValue).joinToString("")
}

/**
 * A [KSerializer] for [SerializableColor]
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
open class ColorSerializer : KSerializer<SerializableColor> {
    override val descriptor = buildClassSerialDescriptor("ColorString") {
        element<Boolean>("isDarkLight")
        element<String>("default")
        element<String>("darkMode", isOptional = true)
    }

    override fun serialize(encoder: Encoder, value: SerializableColor) {
        when (val color = value.color) {
            is KalugaColor.RGBColor -> {
                encoder.encodeBoolean(false)
                encoder.encodeString(color.hexString)
                encoder.encodeNull()
            }

            is KalugaColor.DarkLightColor -> {
                encoder.encodeBoolean(true)
                encoder.encodeString(color.defaultColor.hexString)
                encoder.encodeString(color.darkColor.hexString)
            }

            else -> throw IllegalArgumentException("Unknown KalugaColor $color")
        }
    }

    override fun deserialize(decoder: Decoder): SerializableColor = SerializableColor(
        if (decoder.decodeBoolean()) {
            colorFrom(decoder.decodeString())!! withDarkMode colorFrom(decoder.decodeString())!!
        } else {
            colorFrom(decoder.decodeString())!!
        },
    )
}

/**
 * A [KSerializer] for [SerializableRGBColor]
 */
@Deprecated("This feature has been deprecated. It is recommended to use Compose Multiplatform instead.")
open class RGBColorSerializer : KSerializer<SerializableRGBColor> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ColorString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SerializableRGBColor) {
        val string = value.color.hexString
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): SerializableRGBColor {
        val string = decoder.decodeString()
        return SerializableRGBColor(colorFrom(string)!!)
    }
}
