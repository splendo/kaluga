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

package com.splendo.kaluga.base.text

import com.splendo.kaluga.base.utils.Locale

/**
 * The String that serves as a line separator.
 */
expect val lineSeparator: String

/**
 * Converts a String to its lower cased variant based on a given [Locale]
 * @param locale The [Locale] to use for transforming to lower case.
 */
expect fun String.lowerCased(locale: Locale): String

/**
 * Converts a String to its upper cased variant based on a given [Locale]
 * @param locale The [Locale] to use for transforming to upper case.
 */
expect fun String.upperCased(locale: Locale): String
