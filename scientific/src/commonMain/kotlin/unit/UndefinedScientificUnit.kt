/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.UndefinedQuantityType

sealed interface UndefinedScientificUnit<QuantityType : UndefinedQuantityType> : ScientificUnit<PhysicalQuantity.Undefined<QuantityType>> {
    val quantityType: QuantityType
    val numeratorUnits: List<ScientificUnit<*>>
    val denominatorUnits: List<ScientificUnit<*>>

    sealed interface MetricAndImperial<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<QuantityType>> {
        val metric: Metric<QuantityType>
        val imperial: Imperial<QuantityType>
        val ukImperial: UKImperial<QuantityType>
        val usCustomary: USCustomary<QuantityType>
        val metricAndUKImperial: MetricAndUKImperial<QuantityType>
        val metricAndUSCustomary: MetricAndUSCustomary<QuantityType>
    }
    sealed interface Metric<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        MetricScientificUnit<PhysicalQuantity.Undefined<QuantityType>>
    sealed interface Imperial<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        ImperialScientificUnit<PhysicalQuantity.Undefined<QuantityType>> {
        val ukImperial: UKImperial<QuantityType>
        val usCustomary: USCustomary<QuantityType>
    }
    sealed interface UKImperial<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        UKImperialScientificUnit<PhysicalQuantity.Undefined<QuantityType>>
    sealed interface USCustomary<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        USCustomaryScientificUnit<PhysicalQuantity.Undefined<QuantityType>>
    sealed interface MetricAndUKImperial<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<QuantityType>> {
        val metric: Metric<QuantityType>
        val ukImperial: UKImperial<QuantityType>
    }
    sealed interface MetricAndUSCustomary<QuantityType : UndefinedQuantityType> :
        UndefinedScientificUnit<QuantityType>,
        MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<QuantityType>> {
        val metric: Metric<QuantityType>
        val usCustomary: USCustomary<QuantityType>
    }
}

sealed class AbstractUndefinedScientificUnit<QuantityType : UndefinedQuantityType> : UndefinedScientificUnit<QuantityType> {
    override val quantity by lazy { PhysicalQuantity.Undefined(quantityType) }

    override val symbol: String by lazy {
        val groupedNumerators = numeratorUnits.groupingBy { it }.eachCount()
        val groupedDenominators = denominatorUnits.groupingBy { it }.eachCount()

        val numeratorSymbols = groupedNumerators.mapNotNull { (unit, count) ->
            val correctedCount = count - (groupedDenominators[unit] ?: 0)
            when {
                correctedCount == 1 -> unit.formatSymbol()
                correctedCount > 1 -> "${unit.formatSymbol()}$correctedCount"
                else -> null
            }
        }.joinToString(" * ")

        val shouldNotateDenominatorWithNegatives = numeratorSymbols.isEmpty()
        val denominatorSymbols = groupedDenominators.mapNotNull { (unit, count) ->
            val correctedCount = count - (groupedNumerators[unit] ?: 0)
            when {
                correctedCount == 1 && !shouldNotateDenominatorWithNegatives -> unit.formatSymbol()
                correctedCount > 0 -> {
                    val countToUse = if (shouldNotateDenominatorWithNegatives) correctedCount * -1 else correctedCount
                    "${unit.formatSymbol()}$countToUse"
                }
                else -> null
            }
        }.joinToString(" * ")

        listOf(numeratorSymbols, denominatorSymbols).filter { it.isNotEmpty() }.joinToString(" / ").ifEmpty { One.symbol }
    }

    private fun ScientificUnit<*>.formatSymbol(): String = when {
        quantity is PhysicalQuantity.Undefined<*> -> symbol
        symbol.matches(".*[\\d/*].*".toRegex()) -> "($symbol)"
        else -> symbol
    }
}

sealed class CustomUndefinedScientificUnit<CustomQuantity> : AbstractUndefinedScientificUnit<UndefinedQuantityType.Custom<CustomQuantity>>() {
    abstract val customQuantity: CustomQuantity
    override val quantityType by lazy { UndefinedQuantityType.Custom(customQuantity) }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy { listOf(this) }
    override val denominatorUnits: List<ScientificUnit<*>> = emptyList()

    abstract override val symbol: String

    abstract class MetricAndImperial<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : Metric<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndImperial.customQuantity
                override val symbol: String = this@MetricAndImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val imperial: UndefinedScientificUnit.Imperial<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : Imperial<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndImperial.customQuantity
                override val symbol: String = this@MetricAndImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val ukImperial: UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : UKImperial<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndImperial.customQuantity
                override val symbol: String = this@MetricAndImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val usCustomary: UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : USCustomary<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndImperial.customQuantity
                override val symbol: String = this@MetricAndImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val metricAndUKImperial: UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : MetricAndUKImperial<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndImperial.customQuantity
                override val symbol: String = this@MetricAndImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val metricAndUSCustomary: UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : MetricAndUSCustomary<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndImperial.customQuantity
                override val symbol: String = this@MetricAndImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
    }

    abstract class Metric<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.Metric
    }

    abstract class Imperial<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.Imperial


        override val ukImperial: UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : UKImperial<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@Imperial.customQuantity
                override val symbol: String = this@Imperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@Imperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@Imperial.fromSIUnit(value)
            }
        }
        override val usCustomary: UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : USCustomary<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@Imperial.customQuantity
                override val symbol: String = this@Imperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@Imperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@Imperial.fromSIUnit(value)
            }
        }
    }

    abstract class UKImperial<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.UKImperial
    }

    abstract class USCustomary<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.USCustomary
    }

    abstract class MetricAndUKImperial<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.MetricAndUKImperial
        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : Metric<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndUKImperial.customQuantity
                override val symbol: String = this@MetricAndUKImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.fromSIUnit(value)
            }
        }
        override val ukImperial: UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : UKImperial<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndUKImperial.customQuantity
                override val symbol: String = this@MetricAndUKImperial.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.fromSIUnit(value)
            }
        }
    }

    abstract class MetricAndUSCustomary<CustomQuantity> :
        CustomUndefinedScientificUnit<CustomQuantity>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Custom<CustomQuantity>> {
        override val system = MeasurementSystem.MetricAndUSCustomary
        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : Metric<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndUSCustomary.customQuantity
                override val symbol: String = this@MetricAndUSCustomary.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.fromSIUnit(value)
            }
        }
        override val usCustomary: UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Custom<CustomQuantity>> by lazy {
            object : USCustomary<CustomQuantity>() {
                override val customQuantity: CustomQuantity = this@MetricAndUSCustomary.customQuantity
                override val symbol: String = this@MetricAndUSCustomary.symbol
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.fromSIUnit(value)
            }
        }
    }
}
