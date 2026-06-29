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
 * Class describing a font
 */
expect class KalugaFont

/**
 * The default system [KalugaFont]
 */
expect val defaultFont: KalugaFont

/**
 * The default bold system [KalugaFont]
 */
expect val defaultBoldFont: KalugaFont

/**
 * The default italic system [KalugaFont]
 */
expect val defaultItalicFont: KalugaFont

/**
 * The default monospace system [KalugaFont]
 */
expect val defaultMonospaceFont: KalugaFont

/**
 * Style of the system font
 */
enum class FontStyle {

    /**
     * Default system font style
     */
    DEFAULT,

    /**
     * Serif system font style
     */
    SERIF,

    /**
     * Monospace system font style
     */
    MONOSPACE,
}

/**
 * Trait to add to a system font
 */
enum class FontTrait {

    /**
     * Makes the system font appear in italics
     */
    ITALIC,
}

/**
 * Font weights as defined by [W3](https://www.w3.org/TR/css-fonts-4/#font-weight-prop)
 * @property value the value of the weight. Must be in range [1, 100]
 */
enum class FontWeight(val value: Int) {
    THIN(100),
    EXTRA_LIGHT(200),
    LIGHT(300),
    NORMAL(400),
    MEDIUM(500),
    SEMI_BOLD(600),
    BOLD(700),
    EXTRA_BOLD(800),
    BLACK(900),
}

/**
 * Creates a system font with a given [FontWeight], [FontStyle] and [FontTrait]
 * @param weight the [FontWeight] to apply
 * @param style the [FontStyle] to apply
 * @param traits the set of [FontTrait] to apply
 * @return a [KalugaFont] representing the system font with the given specifications
 */
fun createDefaultFont(weight: FontWeight, style: FontStyle = FontStyle.DEFAULT, traits: Set<FontTrait> = emptySet()): KalugaFont = createDefaultFont(weight.value, style, traits)

/**
 * Creates a system font with a given weight, [FontStyle] and [FontTrait]
 * @param weight the weight to apply. Must be in range [1, 100]
 * @param style the [FontStyle] to apply
 * @param traits the set of [FontTrait] to apply
 * @return a [KalugaFont] representing the system font with the given specifications
 */
expect fun createDefaultFont(weight: Int, style: FontStyle = FontStyle.DEFAULT, traits: Set<FontTrait> = emptySet()): KalugaFont
