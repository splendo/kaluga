/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands
 
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

/**
 * Loads a [String] based on a provided identifier.
 */
interface StringLoader {
    /**
     * Attempts to load the string resource associated with a given identifier. If no match is found, the [defaultValue] will be returned.
     * @param identifier The identifier to find the [String] resource for.
     * @param defaultValue The [String] to return if no match was found for the identifier.
     * @return The associated [String] resources or [defaultValue] if no such resource was found.
     */
    fun loadString(identifier: String, defaultValue: String): String

    /**
     * Attempts to load grammatically correct pluralization of the given string identifier.
     * If no match is found, the [defaultValue] will be returned.
     * @param quantity The value to get pluralized string for.
     * @param identifier The identifier to find the format [String] resource for.
     * @param defaultValue The [String] to return if no match was found for the identifier.
     * @return The associated [String] resources or [defaultValue] if no such resource was found.
     */
    fun loadQuantityString(identifier: String, quantity: Int, defaultValue: String): String
}

/** Default implementation of a [StringLoader]. */
expect class DefaultStringLoader() : StringLoader

/**
 * Loads a [KalugaColor] based on a provided identifier.
 */
interface KalugaColorLoader {
    /**
     * Attempts to load the [KalugaColor] resource associated with a given identifier. If no match is found, the [defaultValue] will be returned.
     * @param identifier The identifier to find the [KalugaColor] resource for.
     * @param defaultValue The [KalugaColor] to return if no match was found for the identifier.
     * @return The associated [KalugaColor] resources or [defaultValue] if no such resource was found.
     */
    fun loadColor(identifier: String, defaultValue: KalugaColor?): KalugaColor?
}

/** Default implementation of a [KalugaColorLoader]. */
expect class DefaultColorLoader() : KalugaColorLoader

/**
 * Loads an [Image] based on a provided identifier.
 */
interface ImageLoader {
    /**
     * Attempts to load the [Image] resource associated with a given identifier. If no match is found, the [defaultValue] will be returned.
     * @param identifier The identifier to find the [Image] resource for.
     * @param defaultValue The [Image] to return if no match was found for the identifier.
     * @return The associated [Image] resources or [defaultValue] if no such resource was found.
     */
    fun loadImage(identifier: String, defaultValue: Image?): Image?
}

/** Default implementation of a [ImageLoader]. */
expect class DefaultImageLoader() : ImageLoader

/**
 * Loads a [Font] based on a provided identifier.
 */
interface FontLoader {
    /**
     * Attempts to load the [Font] resource associated with a given identifier. If no match is found, the [defaultValue] will be returned.
     * @param identifier The identifier to find the [Font] resource for.
     * @param defaultValue The [Font] to return if no match was found for the identifier.
     * @return The associated [Font] resources or [defaultValue] if no such resource was found.
     */
    suspend fun loadFont(identifier: String, defaultValue: Font?): Font?
}

/** Default implementation of a [FontLoader]. */
expect class DefaultFontLoader() : FontLoader

/**
 * Treats this string as a resource identifier for a [String] and grabs the associated [String]
 * @param stringLoader The [StringLoader] used for loading the associated [String] resource.
 * @param defaultValue The [String] to return if no match was found for the identifier. Defaults to `this`.
 * @return The [String] associated with the identifier represented by this String, or [defaultValue] if no such [String] could be found.
 */
fun String.localized(
    stringLoader: StringLoader = DefaultStringLoader(),
    defaultValue: String = this
) = stringLoader.loadString(this, defaultValue)

/**
 * Treats this string as a resource identifier for a plural string format [String] and
 * formats given value using associated format [String]
 * @param quantity The [Int] value to be pluralize
 * @param stringLoader The [StringLoader] used for loading the associated [String] resource.
 * @param defaultValue The [String] to return if no match was found for the identifier. Defaults to `this`.
 * @return The [String] associated with the identifier represented by this String, or [defaultValue] if no such [String] could be found.
 */
fun String.quantity(
    quantity: Int,
    stringLoader: StringLoader = DefaultStringLoader(),
    defaultValue: String = this
): String = stringLoader.loadQuantityString(this, quantity, defaultValue)

/**
 * Treats this string as a resource identifier for a [KalugaColor] and grabs the associated [KalugaColor]
 * @param colorLoader The [KalugaColorLoader] used for loading the associated [KalugaColor] resource.
 * @param defaultValue The [KalugaColor] to return if no match was found for the identifier. Defaults to `null`.
 * @return The [KalugaColor] associated with the identifier represented by this String, or [defaultValue] if no such [KalugaColor] could be found.
 */
fun String.asColor(
    colorLoader: KalugaColorLoader = DefaultColorLoader(),
    defaultValue: KalugaColor? = null
): KalugaColor? = colorLoader.loadColor(this, defaultValue)

/**
 * Treats this string as a resource identifier for a [KalugaColor] and grabs the associated [Image]
 * @param imageLoader The [ImageLoader] used for loading the associated [Image] resource.
 * @param defaultValue The [Image] to return if no match was found for the identifier. Defaults to `null`.
 * @return The [Image] associated with the identifier represented by this String, or [defaultValue] if no such [Image] could be found.
 */
fun String.asImage(
    imageLoader: ImageLoader = DefaultImageLoader(),
    defaultValue: Image? = null
): Image? = imageLoader.loadImage(this, defaultValue)

/**
 * Treats this string as a resource identifier for a [Font] and grabs the associated [Font]
 * @param fontLoader The [FontLoader] used for loading the associated [Font] resource.
 * @param defaultValue The [Font] to return if no match was found for the identifier. Defaults to `null`.
 * @return The [Font] associated with the identifier represented by this String, or [defaultValue] if no such [Font] could be found.
 */
suspend fun String.asFont(
    fontLoader: FontLoader = DefaultFontLoader(),
    defaultValue: Font? = null
): Font? = fontLoader.loadFont(this, defaultValue)

/**
 * Attempts to parse a given [String] into a [KalugaColor].
 * The string should be formatted as either `#AARRGGBB` or `#RRGGBB` for the parsing to succeed.
 * @param hexString The [String] to parse as a [KalugaColor]
 * @return The [KalugaColor] associated with [hexString] or `null` if improperly formatted.
 */
fun colorFrom(hexString: String): KalugaColor? {
    return if (hexString.startsWith('#')) {
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
}
