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
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmName

/**
 * Class describing a color
 */
expect class KalugaColor

/**
 * A wrapper for [KalugaColor] that allows it to be serialized
 * @property color the [KalugaColor] to wrap
 */
@Serializable(with = ColorSerializer::class)
data class SerializableColor(val color: KalugaColor)

/**
 * Gets a [SerializableColor] instance of this color
 */
val KalugaColor.serializable get() = SerializableColor(this)

/**
 * Gets the red value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.red: Double

/**
 * Gets the red value of the color in a range between `0` and `255`
 */
expect val KalugaColor.redInt: Int

/**
 * Gets the green value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.green: Double

/**
 * Gets the green value of the color in a range between `0` and `255`
 */
expect val KalugaColor.greenInt: Int

/**
 * Gets the blue value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.blue: Double

/**
 * Gets the blue value of the color in a range between `0` and `255`
 */
expect val KalugaColor.blueInt: Int

/**
 * Gets the alpha value of the color in a range between `0.0` and `1.0`
 */
expect val KalugaColor.alpha: Double

/**
 * Gets the alpha value of the color in a range between `0` and `255`
 */
expect val KalugaColor.alphaInt: Int

/**
 * Creates a [KalugaColor] using red, green, blue, and (optional) alpha, all ranging between `0.0` and `1.0`.
 * @param red The red color value ranging between `0.0` and `1.0`.
 * @param green The green color value ranging between `0.0` and `1.0`.
 * @param blue The blue color value ranging between `0.0` and `1.0`.
 * @param alpha The alpha color value ranging between `0.0` and `1.0`. Defaults to `1.0`
 * @return The [KalugaColor] with the corresponding red, green, blue, and alpha values
 */
expect fun colorFrom(red: Double, green: Double, blue: Double, alpha: Double = 1.0): KalugaColor

/**
 * Creates a [KalugaColor] using red, green, blue, and (optional) alpha, all ranging between `0` and `255`.
 * @param redInt The red color value ranging between `0` and `255`.
 * @param greenInt The green color value ranging between `0` and `255`.
 * @param blueInt The blue color value ranging between `0` and `255`.
 * @param alphaInt The alpha color value ranging between `0` and `255`. Defaults to `255`
 * @return The [KalugaColor] with the corresponding red, green, blue, and alpha values
 */
expect fun colorFrom(redInt: Int, greenInt: Int, blueInt: Int, alphaInt: Int = 255): KalugaColor

/**
 * Attempts to parse a given [String] into a [Color].
 * The string should be formatted as either `#AARRGGBB` or `#RRGGBB` for the parsing to succeed.
 * @param hexString The [String] to parse as a [Color]
 * @return The [Color] associated with [hexString] or `null` if improperly formatted.
 */
fun colorFrom(hexString: String): KalugaColor? = if (hexString.startsWith('#')) {
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
 * The inverted [KalugaColor]
 */
val KalugaColor.inverted: KalugaColor get() = colorFrom(1.0 - red, 1.0 - green, 1.0 - blue, alpha)

/**
 * The hex string representing this color
 */
val KalugaColor.hexString: String
    get() {
        return "#${alphaInt.toHex(2)}${redInt.toHex(2)}${greenInt.toHex(2)}${blueInt.toHex(2)}"
    }

private fun Int.toHex(minSize: Int): String {
    val hexValue = this.toString(16)
    val prefix = List(minSize - hexValue.length) { "0" }
    return listOf(*prefix.toTypedArray(), hexValue).joinToString("")
}

/**
 * A [KSerializer] for [SerializableColor]
 */
open class ColorSerializer : KSerializer<SerializableColor> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ColorString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SerializableColor) {
        val string = value.color.hexString
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): SerializableColor {
        val string = decoder.decodeString()
        return SerializableColor(colorFrom(string)!!)
    }
}
