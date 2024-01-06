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
import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.base.utils.toInt
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.SystemScientificUnit
import com.splendo.kaluga.scientific.unit.convert

class DenominatorScientificValueFormatter private constructor(
    private val denominators: Map<ScientificUnit<*>, ScientificValueDenominators<*, *, *>>,
    private val separator: String,
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
            fun convertToIndexWithRemainder(value: Decimal, index: Int): Pair<ScientificValue<Quantity, *>, Decimal> {
                val valueInUnitOfDenominator = unit.convert(value, denominators[index])
                return if (index < denominators.size - 1) {
                    val remainder = value - denominators[index].convert(valueInUnitOfDenominator.toInt(), unit).toDecimal()
                    DenominatorScientificValue(denominators[index], valueInUnitOfDenominator.toInt().toDecimal()) to remainder
                } else {
                    DenominatorScientificValue(denominators[index], valueInUnitOfDenominator) to 0.0.toDecimal()
                }
            }

            fun equalsToZeroForFormat(value: Decimal, index: Int, formatter: ScientificValueFormatter): Boolean {
                val unit = denominators[index]
                val zeroFormatted = formatter.format(DenominatorScientificValue(unit, 0.0.toDecimal()))
                val formatted = formatter.format(DenominatorScientificValue(unit, value))
                return zeroFormatted == formatted
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

        var denominatorUnitFormatter = NumberFormatter(style = NumberFormatStyle.Decimal(minIntegerDigits = 1U, maxFractionDigits = 0U))
        var lastDenominatorUnitFormatter = denominatorUnitFormatter
        var defaultUnitFormatter = CommonScientificValueFormatter.defaultUnitFormatter
        var separator: String = Typography.nbsp.toString()
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
                denominators.denominators.foldIndexed(value.decimalValue to emptyList<String?>()) { index, (remainder, accumulator), _ ->
                    val (valueInUnit, newRemainder) = denominators.convertToIndexWithRemainder(remainder, index)
                    val formattedRemainderIsZero = denominators.equalsToZeroForFormat(newRemainder, denominators.denominators.size - 1, lastDenominatorFormatter)
                    val isLastElement = when {
                        index == denominators.denominators.size - 1 -> true
                        formattedRemainderIsZero && includeZeroValues == IncludeZeroValues.NONE -> true
                        valueInUnit.value.toInt() == 0 && formattedRemainderIsZero && includeZeroValues == IncludeZeroValues.FIRST && accumulator.lastOrNull() != null -> true
                        else -> false
                    }
                    val formatter = if (isLastElement) lastDenominatorFormatter else denominatorFormatter
                    val toAdd = when {
                        !denominators.equalsToZeroForFormat(valueInUnit.decimalValue, index, formatter) -> listOf(valueInUnit)
                        includeZeroValues == IncludeZeroValues.ALL -> listOf(valueInUnit)
                        !isLastElement -> listOf(null)
                        accumulator.lastOrNull() == null -> listOf(valueInUnit, null)
                        else -> listOf(null)
                    }
                    val newAccumulator = accumulator + toAdd.filterNotNull().map(formatter::format)
                    newRemainder to newAccumulator
                }.second.filterNotNull().joinToString(separator = separator)
            }
        }
    }
}