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

@Deprecated(
    "Due to name clashes with platform classes and API changes this class has been renamed and changed to an interface. It will be removed in a future release.",
    ReplaceWith("KalugaColor"),
)
typealias Font = KalugaFont

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

enum class Style {
    DEFAULT,
    SERIF,
    MONOSPACE,
}

enum class Traits {
    ITALIC,
}

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

fun createDefaultFont(weight: FontWeight, style: Style = Style.DEFAULT, traits: Set<Traits> = emptySet()): KalugaFont = createDefaultFont(weight.value, style, traits)

expect fun createDefaultFont(weight: Int, style: Style = Style.DEFAULT, traits: Set<Traits> = emptySet()): KalugaFont
