package com.splendo.kaluga.formatted

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

/**
 * Container for multiple formatters
 *
 * Can be used if in the same situation we can receive value in different formats. For instance if we receive date as a string or as a timestamp.
 * Primary formatter always has priority and it will be used for conversion to sting.
 * If primary formatter throws an error, formatter will try to apply any of secondary formatters until it finds the correct one.
 * Exception [FormattersScopeException] will be thrown if no suitable formatters will be found.
 *
 * @property primary primary formatter
 * @property secondary list of secondary formatters
 */
class FormattersScope<T : Any>(private val primary: Formatter<T>, private val secondary: List<Formatter<T>>) : Formatter<T> {
    /**
     * Converts string value to the value of type T
     *
     * First tries to parse string using primary formatter, if it throws an error, will try to apply any of secondary formatters until it finds the correct one.
     * Exception [FormattersScopeException] will be thrown if no suitable formatters will be found.
     */
    override fun value(string: String): T =
            value(primary, string)
        ?: secondary.mapNotNull { formatter ->
            value(formatter, string)
    }.firstOrNull()
            ?: throw FormattersScopeException()

    private fun value(formatter: Formatter<T>, string: String): T? =
        try { formatter.value(string) } catch (e: Exception) { null }

    /**
     * Converts value of type T to string using primary formatter
     */
    override fun string(value: T): String =
            primary.string(value)
}

/**
 * Exception will be thrown by Formatters scope if no suitable formatter was found
 */
class FormattersScopeException(message: String = "No suitable formatter was found") : Exception(message)
