/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.text.RoundingMode
import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.SystemScientificUnit
import com.splendo.kaluga.scientific.unit.convert

/**
 * A [ScientificValueFormatter] that formats a [ScientificValue] by splitting it up into denominations.
 * E.g. `2ft 4in`.
 * Build a formatter using [DenominatorScientificValueFormatter.with].
 */
class DenominatorScientificValueFormatter private constructor(
    private val denominators: Map<ScientificUnit<*>, ScientificValueDenominators<*, *, *>>,
    private val separator: String,
    private val scale: UInt,
    private val roundingPrecision: Double,
    private val includeZeroValues: IncludeZeroValues,
    private val customUnitTargets: Map<ScientificUnit<*>, ScientificUnit<*>>,
    private val customQuantityTargets: Map<PhysicalQuantity, ScientificUnit<*>>,
    private val denominatorFormatter: CommonScientificValueFormatter,
    private val lastDenominatorFormatter: CommonScientificValueFormatter,
    private val defaultFormatter: CommonScientificValueFormatter,
) : ScientificValueFormatter {

    companion object {

        /**
         * Creates a [DenominatorScientificValueFormatter]
         * @param builder the Builder function to build the [DenominatorScientificValueFormatter]
         */
        fun with(builder: Builder.() -> Unit): DenominatorScientificValueFormatter {
            return Builder().apply(builder).build()
        }
    }

    internal data class ScientificValueDenominators<
        Quantity : PhysicalQuantity,
        System: MeasurementSystem,
        Unit : SystemScientificUnit<System, Quantity>
        >(
        val unit: Unit,
        val denominators: List<SystemScientificUnit<System, Quantity>>
    ) {
        fun convertToIndexWithRemainder(value: Decimal, index: Int, scale: UInt, precision: Double): Pair<ScientificValue<Quantity, *>, Decimal> {
            // First get the value in the unit of the denominator at the given index
            val valueInUnitOfDenominator = unit.convert(value, denominators[index])
            // When at the last element there is no lower denominator, otherwise split into this denominator and the remainder
            return if (index < denominators.size - 1) {
                // Round down to scale taking into account the precision to prevent conversion errors
                val roundedValue = valueInUnitOfDenominator.roundDown(scale, precision)
                // Calculate the remainder in the original unit
                val remainder = if (roundedValue < valueInUnitOfDenominator) value - denominators[index].convert(roundedValue, unit) else 0.0.toDecimal()
                FormatterScientificValue(denominators[index], roundedValue) to remainder
            } else {
                FormatterScientificValue(denominators[index], valueInUnitOfDenominator) to 0.0.toDecimal()
            }
        }

        fun remainderEqualsToZeroFormat(remainder: Decimal, formatter: ScientificValueFormatter): Boolean {
            val remainderInFinalUnit = unit.convert(remainder, denominators.last())
            return equalsToZeroForFormat(remainderInFinalUnit, denominators.size - 1, formatter)
        }

        fun equalsToZeroForFormat(value: Decimal, indexOfUnit: Int, formatter: ScientificValueFormatter): Boolean {
            val valueUnit = denominators[indexOfUnit]
            // Format 0.0
            val zeroFormatted = formatter.format(FormatterScientificValue(valueUnit, 0.0.toDecimal()))
            // Format the value in the given unit
            val formatted = formatter.format(FormatterScientificValue(valueUnit, value))
            // If they are the same, the value is 0
            return zeroFormatted == formatted
        }

        private fun Decimal.roundDown(scale: UInt, precision: Double): Decimal {
            return (this + precision.toDecimal()).round(scale.toInt(), com.splendo.kaluga.base.utils.RoundingMode.RoundDown)
        }
    }

    /**
     * Setting for formatting zero values
     */
    enum class IncludeZeroValues {
        /**
         * Does not format a zero value unless not a single denominator is non-zero.
         */
        NONE,

        /**
         * Format all zero values
         */
        ALL,

        /**
         * Format only the first zero denominator that occurs at the end of the format.
         */
        FIRST
    }

    /**
     * Builder for creating a [DenominatorScientificValueFormatter]
     */
    class Builder internal constructor() {

        /**
         * The [NumberFormatter] to use for all denominator units except for the last formatted denominator
         */
        var denominatorUnitFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U, roundingMode = RoundingMode.Down))

        /**
         * The [NumberFormatter] to use for the last formatted denominator.
         */
        var lastDenominatorUnitFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U))

        /**
         * The CommonScientificValueFormatter to use for units not specified by the denominator
         */
        var defaultUnitFormatter = CommonScientificValueFormatter.defaultUnitFormatter

        /**
         * The scale used for rounding the denominator units.
         */
        var scale: UInt = 0U

        /**
         * The precision at which denominators are rounded down. If within the precision unit the value will be rounded up, otherwise down.
         */
        var roundingPrecision = 0.000000001

        /**
         * The separator between denominators
         */
        var separator: String = " "

        /**
         * The [IncludeZeroValues] settings to use when encountering a zero value denominator.
         */
        var includeZeroValues: IncludeZeroValues = IncludeZeroValues.NONE
        private val denominatorMap = mutableMapOf<ScientificUnit<*>, ScientificValueDenominators<*, *, *>>()
        private val customUnitTargets =  mutableMapOf<ScientificUnit<*>, ScientificUnit<*>>()
        private val customQuantityTargets =mutableMapOf<PhysicalQuantity, ScientificUnit<*>>()
        private val customFormatters = mutableMapOf<ScientificUnit<*>, CustomFormatHandler>()
        private val customSymbols = mutableMapOf<ScientificUnit<*>, String>()

        /**
         * Sets the denominator [SystemScientificUnit] of a given [SystemScientificUnit]
         * When formatting this unit, it will be split up into the denominator units (including the unit itself).
         * @param Quantity the [PhysicalQuantity] of the unit to denominate
         * @param System the [MeasurementSystem] of the unit to denominate
         * @param Unit the [SystemScientificUnit] to denominate
         * @param denominators the list of [SystemScientificUnit] to denominate the unit into
         */
        infix fun <
            Quantity : PhysicalQuantity,
            System: MeasurementSystem,
            Unit : SystemScientificUnit<System, Quantity>> Unit.denominateBy(denominators: List<SystemScientificUnit<System, Quantity>>) {
            denominatorMap[this] = ScientificValueDenominators(this, (denominators + this).toSet().sortedByDescending { it.convert(1, this) })
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

        internal fun build(): DenominatorScientificValueFormatter = DenominatorScientificValueFormatter(
            denominatorMap.toMap(),
            separator,
            scale,
            roundingPrecision,
            includeZeroValues,
            customUnitTargets,
            customQuantityTargets,
            CommonScientificValueFormatter(
                denominatorUnitFormatter,
                emptyMap(),
                emptyMap(),
                customSymbols,
                customFormatters,
            ),
            CommonScientificValueFormatter(
                lastDenominatorUnitFormatter,
                emptyMap(),
                emptyMap(),
                customSymbols,
                customFormatters,
            ),
            CommonScientificValueFormatter(
                defaultUnitFormatter,
                customUnitTargets,
                customQuantityTargets,
                customSymbols,
                customFormatters,
            ),
        )
    }

    override fun format(value: ScientificValue<*, *>): String {
        val customUnit = customUnitTargets[value.unit] ?: customQuantityTargets[value.unit.quantity]
        val valueToFormat = if (customUnit != null && customUnit != value.unit) {
            FormatterScientificValue(customUnit, customUnit.fromSIUnit(value.unit.toSIUnit(value.decimalValue)))
        } else {
            value
        }
        val denominators = denominators[valueToFormat.unit]
        return when {
            denominators == null || denominators.denominators.isEmpty() -> defaultFormatter.format(valueToFormat)
            denominators.denominators.size == 1 -> lastDenominatorFormatter.format(valueToFormat)
            else -> {
                denominators.denominators.indices.fold(valueToFormat.decimalValue to emptyList<String?>()) { (remainder, accumulator), index ->
                    val (valueInUnit, newRemainder) = denominators.convertToIndexWithRemainder(remainder, index, scale, roundingPrecision)
                    val formattedRemainderIsZero = denominators.remainderEqualsToZeroFormat(newRemainder, lastDenominatorFormatter)
                    val isEndingZeroOrLastNonZeroElement = formattedRemainderIsZero || index == denominators.denominators.size - 1
                    val useLastDenominatorFormat = when (includeZeroValues) {
                        IncludeZeroValues.ALL -> index == denominators.denominators.size - 1
                        IncludeZeroValues.FIRST -> isEndingZeroOrLastNonZeroElement
                        IncludeZeroValues.NONE -> isEndingZeroOrLastNonZeroElement
                    }
                    val formatter = if (useLastDenominatorFormat) lastDenominatorFormatter else denominatorFormatter
                    val toAdd = when {
                        !denominators.equalsToZeroForFormat(valueInUnit.decimalValue, index, formatter) -> listOf(valueInUnit)
                        includeZeroValues == IncludeZeroValues.ALL -> listOf(valueInUnit)
                        !isEndingZeroOrLastNonZeroElement -> listOf(null)
                        accumulator.isEmpty() -> listOf(valueInUnit, null)
                        includeZeroValues == IncludeZeroValues.NONE -> listOf(null)
                        accumulator.last() != null -> listOf(valueInUnit, null)
                        else -> listOf(null)
                    }
                    val newAccumulator = accumulator + toAdd.map { value -> value?.let { formatter.format(it) } }
                    newRemainder to newAccumulator
                }.second.filterNotNull().joinToString(separator = separator)
            }
        }
    }
}