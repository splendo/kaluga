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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.ScientificUnit

internal typealias CustomFormatHandler = (Number) -> String

/**
 * An implementation of [ScientificValueFormatter]
 * Use [CommonScientificValueFormatter.with] to create an instance that can be customized per [ScientificUnit] type
 */
class CommonScientificValueFormatter internal constructor(
    private val defaultValueFormatter: NumberFormatter,
    private val customUnitTargets: Map<ScientificUnit<*>, ScientificUnit<*>>,
    private val customQuantityTargets: Map<PhysicalQuantity, ScientificUnit<*>>,
    private val customSymbols: Map<ScientificUnit<*>, String>,
    private val customFormatters: Map<ScientificUnit<*>, CustomFormatHandler>,
) : ScientificValueFormatter {

    companion object {
        internal val defaultUnitFormatter get() = NumberFormatter(style = NumberFormatStyle.Decimal(minIntegerDigits = 1U))

        /**
         * Creates a [CommonScientificValueFormatter]
         * @param builder the [Builder] function to build the [CommonScientificValueFormatter]
         * @return the created [CommonScientificValueFormatter]
         */
        fun with(builder: Builder.() -> Unit): CommonScientificValueFormatter = Builder().apply(builder).build()

        /**
         * A default [CommonScientificValueFormatter] that formats all units as themselves using the current user [com.splendo.kaluga.base.utils.KalugaLocale]
         */
        val default get() = with {}
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
            @Deprecated("Use CommonScientificValueFormatter.with", replaceWith = ReplaceWith("CommonScientificValueFormatter.with { build }"))
            fun build(build: Builder.() -> Unit = {}): ScientificValueFormatter {
                val builder = Builder()
                return builder.build()
            }
        }

        var defaultValueFormatter = CommonScientificValueFormatter.defaultUnitFormatter
        private val customUnitTargets = mutableMapOf<ScientificUnit<*>, ScientificUnit<*>>()
        private val customQuantityTargets = mutableMapOf<PhysicalQuantity, ScientificUnit<*>>()
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

        /**
         * Formats any [ScientificValue] with quantity [Quantity] as a unit of [unit]
         * Is overruled by [ScientificUnit.formatAs]
         * @param Quantity the [PhysicalQuantity] whose units should be formatted as [unit]
         * @param unit the [ScientificUnit] to which all [ScientificValue] of [Quantity] should be converted before formatting.
         */
        infix fun <Quantity : PhysicalQuantity> Quantity.formatAs(unit: ScientificUnit<Quantity>) {
            customQuantityTargets[this] = unit
        }

        /**
         * Formats any [ScientificValue] of this unit into [unit]
         * Overrules [PhysicalQuantity.formatAs]
         * @param Quantity the [PhysicalQuantity] of the unit that should be formatted as [unit]
         * @param unit the [ScientificUnit] to which this unit should be converted before formatting.
         */
        infix fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.formatAs(unit: ScientificUnit<Quantity>) {
            customUnitTargets[this] = unit
        }

        /**
         * Formats a value in a [ScientificUnit] using [CustomFormatHandler]
         * @param handler the handler to format the unit as
         */
        infix fun ScientificUnit<*>.formatUsing(handler: CustomFormatHandler) {
            customFormatters[this] = handler
        }

        /**
         * Sets a custom symbol for formatting a value of a [ScientificUnit]
         */
        infix fun ScientificUnit<*>.usesCustomSymbol(symbol: String) {
            customSymbols[this] = symbol
        }

        fun build(): CommonScientificValueFormatter =
            CommonScientificValueFormatter(this.defaultValueFormatter, customUnitTargets, customQuantityTargets, customSymbols, customFormatters)
    }

    override fun format(value: ScientificValue<*, *>): String {
        val customUnit = customUnitTargets[value.unit] ?: customQuantityTargets[value.unit.quantity]
        val valueToFormat = if (customUnit != null && customUnit != value.unit) {
            FormatterScientificValue(customUnit.fromSIUnit(value.unit.toSIUnit(value.decimalValue)), customUnit)
        } else {
            value
        }
        val customFormatter = customFormatters[value.unit]
        return customFormatter?.let { it(value.value) } ?: defaultFormat(valueToFormat)
    }

    private fun defaultFormat(value: ScientificValue<*, *>): String = listOfNotNull(
        defaultValueFormatter.format(value.value),
        (customSymbols[value.unit] ?: value.unit.symbol).takeIf(String::isNotBlank),
    ).joinToString(separator = Typography.nbsp.toString())
}
