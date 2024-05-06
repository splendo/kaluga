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

@file:JvmName("JvmFont")

package com.splendo.kaluga.resources

/**
 * Class describing a font
 */
actual class KalugaFont

/**
 * The default system [KalugaFont]
 */
actual val defaultFont: KalugaFont get() = KalugaFont()

/**
 * The default bold system [KalugaFont]
 */
actual val defaultBoldFont: KalugaFont get() = KalugaFont()

/**
 * The default italic system [KalugaFont]
 */
actual val defaultItalicFont: KalugaFont get() = KalugaFont()

/**
 * The default monospace system [KalugaFont]
 */
actual val defaultMonospaceFont: KalugaFont get() = KalugaFont()

/**
 * Creates a system font with a given weight, [FontStyle] and [FontTrait]
 * @param weight the weight to apply. Must be in range [1, 100]
 * @param style the [FontStyle] to apply
 * @param traits the set of [FontTrait] to apply
 * @return a [KalugaFont] representing the system font with the given specifications
 */
actual fun createDefaultFont(weight: Int, style: FontStyle, traits: Set<FontTrait>): KalugaFont = KalugaFont()
