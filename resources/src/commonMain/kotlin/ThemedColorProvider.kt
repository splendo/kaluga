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
@file:JvmName("CommonThemedColorProvider")
package com.splendo.kaluga.resources

import kotlin.jvm.JvmName
import kotlin.reflect.KProperty

/** Multiplatform Color Provider that is aware of a Dark and Light mode */
abstract class ThemedColorProvider {

    /** light version of this color */
    abstract val lightColor: Color

    /** dark version of this color */
    abstract val darkColor: Color

    /** main color, automatically changing based on theme */
    abstract val color: Color
}

/** This extension allows to get the hexValue of the provided color with the "by" syntax */
inline operator fun ThemedColorProvider.getValue(thisRef: Any?, property: KProperty<*>): Color = color

/**
 * One single color represented in 2 themes Dark or Light.
 * @param light color when presented in Light mode: formatted as hexadecimal string
 * @param dark color when presented in Dark mode: formatted as hexadecimal string
 */
expect fun themeColor(light: String, dark: String): ThemedColorProvider
