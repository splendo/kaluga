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

/**
 * Default implementation of a [StringLoader].
 */
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

/**
 * Default implementation of a [KalugaColorLoader].
 */
expect class DefaultColorLoader() : KalugaColorLoader

/**
 * Loads a [KalugaImage] based on a provided identifier.
 */
interface ImageLoader {
    /**
     * Attempts to load the [KalugaImage] resource associated with a given identifier. If no match is found, the [defaultValue] will be returned.
     * @param identifier The identifier to find the [KalugaImage] resource for.
     * @param defaultValue The [KalugaImage] to return if no match was found for the identifier.
     * @return The associated [KalugaImage] resources or [defaultValue] if no such resource was found.
     */
    fun loadImage(identifier: String, defaultValue: KalugaImage?): KalugaImage?
}

/**
 * Default implementation of an [ImageLoader].
 */
expect class DefaultImageLoader() : ImageLoader

/**
 * Loads a [KalugaFont] based on a provided identifier.
 */
interface FontLoader {
    /**
     * Attempts to load the [KalugaFont] resource associated with a given identifier. If no match is found, the [defaultValue] will be returned.
     * @param identifier The identifier to find the [KalugaFont] resource for.
     * @param defaultValue The [KalugaFont] to return if no match was found for the identifier.
     * @return The associated [KalugaFont] resources or [defaultValue] if no such resource was found.
     */
    suspend fun loadFont(identifier: String, defaultValue: KalugaFont?): KalugaFont?
}

/**
 * Default implementation of a [FontLoader].
 */
expect class DefaultFontLoader() : FontLoader

/**
 * Treats this string as a resource identifier for a [String] and grabs the associated [String]
 * @param stringLoader The [StringLoader] used for loading the associated [String] resource.
 * @param defaultValue The [String] to return if no match was found for the identifier. Defaults to `this`.
 * @return The [String] associated with the identifier represented by this String, or [defaultValue] if no such [String] could be found.
 */
fun String.localized(stringLoader: StringLoader = DefaultStringLoader(), defaultValue: String = this) = stringLoader.loadString(this, defaultValue)

/**
 * Treats this string as a resource identifier for a plural string format [String] and
 * formats given value using associated format [String]
 * @param quantity The [Int] value to be pluralize
 * @param stringLoader The [StringLoader] used for loading the associated [String] resource.
 * @param defaultValue The [String] to return if no match was found for the identifier. Defaults to `this`.
 * @return The [String] associated with the identifier represented by this String, or [defaultValue] if no such [String] could be found.
 */
fun String.quantity(quantity: Int, stringLoader: StringLoader = DefaultStringLoader(), defaultValue: String = this): String =
    stringLoader.loadQuantityString(this, quantity, defaultValue)

/**
 * Treats this string as a resource identifier for a [KalugaColor] and grabs the associated [KalugaColor]
 * @param colorLoader The [KalugaColorLoader] used for loading the associated [KalugaColor] resource.
 * @param defaultValue The [KalugaColor] to return if no match was found for the identifier. Defaults to `null`.
 * @return The [KalugaColor] associated with the identifier represented by this String, or [defaultValue] if no such [KalugaColor] could be found.
 */
fun String.asColor(colorLoader: KalugaColorLoader = DefaultColorLoader(), defaultValue: KalugaColor? = null): KalugaColor? = colorLoader.loadColor(this, defaultValue)

/**
 * Treats this string as a resource identifier for a [KalugaColor] and grabs the associated [KalugaImage]
 * @param imageLoader The [ImageLoader] used for loading the associated [KalugaImage] resource.
 * @param defaultValue The [KalugaImage] to return if no match was found for the identifier. Defaults to `null`.
 * @return The [KalugaImage] associated with the identifier represented by this String, or [defaultValue] if no such [KalugaImage] could be found.
 */
fun String.asImage(imageLoader: ImageLoader = DefaultImageLoader(), defaultValue: KalugaImage? = null): KalugaImage? = imageLoader.loadImage(this, defaultValue)

/**
 * Treats this string as a resource identifier for a [KalugaFont] and grabs the associated [KalugaFont]
 * @param fontLoader The [FontLoader] used for loading the associated [KalugaFont] resource.
 * @param defaultValue The [KalugaFont] to return if no match was found for the identifier. Defaults to `null`.
 * @return The [KalugaFont] associated with the identifier represented by this String, or [defaultValue] if no such [KalugaFont] could be found.
 */
suspend fun String.asFont(fontLoader: FontLoader = DefaultFontLoader(), defaultValue: KalugaFont? = null): KalugaFont? = fontLoader.loadFont(this, defaultValue)
