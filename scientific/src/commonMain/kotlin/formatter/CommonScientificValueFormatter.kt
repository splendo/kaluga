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

package com.splendo.kaluga.scientific.formatter

import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.ScientificUnit

internal typealias CustomFormatHandler = (Number) -> String

/**
 * An implementation of [ScientificValueFormatter]
 * Use [CommonScientificValueFormatter.Builder] to create an instance that can be customized per [ScientificUnit] type
 */
class CommonScientificValueFormatter internal constructor(
    private val defaultValueFormatter: NumberFormatter,
    private val customSymbols: Map<ScientificUnit<*>, String>,
    private val customFormatters: Map<ScientificUnit<*>, CustomFormatHandler>,
) : ScientificValueFormatter {

    companion object {
        internal val defaultUnitFormatter get() = NumberFormatter(style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))
        fun where(builder: Builder.() -> Unit): CommonScientificValueFormatter {
            return Builder().apply(builder).build()
        }
        val default get() = where {}
    }


    /**
     * Builder for creating a [CommonScientificValueFormatter]
     */
    class Builder internal constructor() {
        companion object {

            /**
             * Builds a [ScientificValueFormatter] using a [Builder]
             * @param build method for configuring the [Builder]
             * @return the built [ScientificValueFormatter]
             */
            @Deprecated("Use CommonScientificValueFormatter.where", replaceWith = ReplaceWith("CommonScientificValueFormatter.where { build }"))
            fun build(build: Builder.() -> Unit = {}): ScientificValueFormatter {
                val builder = Builder()
                return builder.build()
            }
        }

        var defaultValueFormatter = CommonScientificValueFormatter.defaultUnitFormatter
        private val customFormatters = mutableMapOf<ScientificUnit<*>, CustomFormatHandler>()
        private val customSymbols = mutableMapOf<ScientificUnit<*>, String>()

        /**
         * Sets a method for converting a [Number] in a given [ScientificUnit] into a String representation of its value
         * If not set for a given [ScientificUnit] this will automatically format using [ScientificUnit.symbol]
         * @param unit the [ScientificUnit] to apply the formatting to
         * @param format method for formatting a value in the given [ScientificUnit]
         */
        @Deprecated("Use formatUsing", replaceWith = ReplaceWith("unit formatUsing format"))
        fun useFormat(unit: ScientificUnit<*>, format: CustomFormatHandler) {
            unit formatUsing format
        }

        infix fun ScientificUnit<*>.formatUsing(handler: CustomFormatHandler) {
            customFormatters[this] = handler
        }

        infix fun ScientificUnit<*>.usesCustomSymbol(symbol: String) {
            customSymbols[this] = symbol
        }

        fun build(): CommonScientificValueFormatter {
            return CommonScientificValueFormatter(this.defaultValueFormatter, emptyMap(), customFormatters)
        }
    }

    override fun format(value: ScientificValue<*, *>): String {
        val customFormatter = customFormatters[value.unit]
        return customFormatter?.let { it(value.value) } ?: defaultFormat(value)
    }

    private fun defaultFormat(value: ScientificValue<*, *>): String =
        "${defaultValueFormatter.format(value.value)}${Typography.nbsp}${customSymbols[value.unit] ?: value.unit.symbol.withoutSpaces()}"
}

private fun String.withoutSpaces() = filterNot(Char::isWhitespace)
