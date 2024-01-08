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
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.SystemScientificUnit
import com.splendo.kaluga.scientific.unit.convert

class DenominatorScientificValueFormatter private constructor(
    private val denominators: Map<ScientificUnit<*>, ScientificValueDenominators<*, *, *>>,
    private val separator: String,
    private val scale: Int,
    private val roundingPrecision: Double,
    private val includeZeroValues: IncludeZeroValues,
    private val denominatorFormatter: CommonScientificValueFormatter,
    private val lastDenominatorFormatter: CommonScientificValueFormatter,
    private val defaultFormatter: CommonScientificValueFormatter,
) : ScientificValueFormatter {

    companion object {
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
            fun convertToIndexWithRemainder(value: Decimal, index: Int, scale: Int, precision: Double): Pair<ScientificValue<Quantity, *>, Decimal> {
                val valueInUnitOfDenominator = unit.convert(value, denominators[index])
                return if (index < denominators.size - 1) {
                    val roundedValue = valueInUnitOfDenominator.roundDown(scale, precision)
                    val remainder = if (roundedValue < valueInUnitOfDenominator) value - denominators[index].convert(roundedValue, unit) else 0.0.toDecimal()
                    DenominatorScientificValue(denominators[index], roundedValue) to remainder
                } else {
                    DenominatorScientificValue(denominators[index], valueInUnitOfDenominator) to 0.0.toDecimal()
                }
            }

            fun remainderEqualsToZeroFormat(remainder: Decimal, formatter: ScientificValueFormatter): Boolean {
                val remainderInFinalUnit = unit.convert(remainder, denominators.last())
                return equalsToZeroForFormat(remainderInFinalUnit, denominators.size - 1, formatter)
            }

            fun equalsToZeroForFormat(value: Decimal, indexOfUnit: Int, formatter: ScientificValueFormatter): Boolean {
                val valueUnit = denominators[indexOfUnit]
                val zeroFormatted = formatter.format(DenominatorScientificValue(valueUnit, 0.0.toDecimal()))
                val formatted = formatter.format(DenominatorScientificValue(valueUnit, value))
                return zeroFormatted == formatted
            }

            private fun Decimal.roundDown(scale: Int, precision: Double): Decimal {
                return (this + precision.toDecimal()).round(scale, com.splendo.kaluga.base.utils.RoundingMode.RoundDown)
            }
        }

    enum class IncludeZeroValues {
        NONE,
        ALL,
        FIRST
    }

    internal class DenominatorScientificValue<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>>(
        override val unit: Unit,
        override val decimalValue: Decimal
    ) : ScientificValue<Quantity, Unit> {
        override val value: Number = decimalValue.toDouble()
    }


    class Builder internal constructor() {

        var denominatorUnitFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U, roundingMode = RoundingMode.Down))
        var lastDenominatorUnitFormatter = denominatorUnitFormatter
        var defaultUnitFormatter = CommonScientificValueFormatter.defaultUnitFormatter
        var scale: Int = 0
        var roundingPrecision = 0.000000001
        var separator: String = " "
        var includeZeroValues: IncludeZeroValues = IncludeZeroValues.NONE
        private val denominatorMap = mutableMapOf<ScientificUnit<*>, ScientificValueDenominators<*, *, *>>()
        private val customFormatters = mutableMapOf<ScientificUnit<*>, CustomFormatHandler>()
        private val customSymbols = mutableMapOf<ScientificUnit<*>, String>()

        infix fun <
            Quantity : PhysicalQuantity,
            System: MeasurementSystem,
            Unit : SystemScientificUnit<System, Quantity>> Unit.denominateBy(denominators: List<SystemScientificUnit<System, Quantity>>) {
                denominatorMap[this] = ScientificValueDenominators(this, (denominators + this).toSet().sortedByDescending { it.convert(1, this) })
            }

        infix fun ScientificUnit<*>.formatUsing(handler: CustomFormatHandler) {
            customFormatters[this] = handler
        }

        infix fun ScientificUnit<*>.usesCustomSymbol(symbol: String) {
            customSymbols[this] = symbol
        }

        fun build(): DenominatorScientificValueFormatter = DenominatorScientificValueFormatter(
            denominatorMap.toMap(),
            separator,
            scale,
            roundingPrecision,
            includeZeroValues,
            CommonScientificValueFormatter(
                denominatorUnitFormatter,
                customSymbols,
                customFormatters,
            ),
            CommonScientificValueFormatter(
                lastDenominatorUnitFormatter,
                customSymbols,
                customFormatters,
            ),
            CommonScientificValueFormatter(
                defaultUnitFormatter,
                customSymbols,
                customFormatters,
            ),
        )
    }

    override fun format(value: ScientificValue<*, *>): String {
        val denominators = denominators[value.unit]
        return when {
            denominators == null || denominators.denominators.isEmpty() -> defaultFormatter.format(value)
            denominators.denominators.size == 1 -> lastDenominatorFormatter.format(value)
            else -> {
                denominators.denominators.indices.fold(value.decimalValue to emptyList<String?>()) { (remainder, accumulator), index ->
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