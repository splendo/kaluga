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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity

sealed class UndefinedScientificUnit<QuantityType : PhysicalQuantity.Undefined.QuantityType> : ScientificUnit<PhysicalQuantity.Undefined<QuantityType>> {
    abstract val quantityType: QuantityType
    override val quantity by lazy { PhysicalQuantity.Undefined(quantityType) }
    internal abstract val numeratorUnits: List<ScientificUnit<*>>
    internal abstract val denominatorUnits: List<ScientificUnit<*>>
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

    private fun ScientificUnit<*>.formatSymbol() {
        if (quantity is PhysicalQuantity.Undefined<*>) {
            symbol
        } else {
            "($symbol)"
        }
    }
}

sealed class WrappedUndefinedScientificUnit<
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>
    > : UndefinedScientificUnit<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>() {
    abstract val wrapped: WrappedUnit
    override val quantityType by lazy { PhysicalQuantity.Undefined.QuantityType.Wrapped(wrapped.quantity) }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy { listOf(wrapped) }
    override val denominatorUnits: List<ScientificUnit<*>> = emptyList()

    override fun fromSIUnit(value: Decimal): Decimal = wrapped.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = wrapped.fromSIUnit(value)

    class MetricAndImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInMetricAndImperial {
        override val system = MeasurementSystem.MetricAndImperial
    }

    class Metric<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), MetricScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    class Imperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), ImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInImperial {
        override val system = MeasurementSystem.Imperial
    }

    class UKImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), UKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    class USCustomary<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), USCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    class MetricAndUKImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInMetricAndUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    class MetricAndUSCustomary<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit
        > internal constructor(override val wrapped: WrappedUnit) : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>(), MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Wrapped<WrappedQuantity>>> where
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    WrappedUnit : MeasurementUsage.UsedInMetricAndUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}

sealed class CustomUndefinedScientificUnit<CustomQuantity> : UndefinedScientificUnit<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>() {
    abstract val customQuantity: CustomQuantity
    override val quantityType by lazy { PhysicalQuantity.Undefined.QuantityType.Custom(customQuantity) }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy { listOf(this) }
    override val denominatorUnits: List<ScientificUnit<*>> = emptyList()

    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value

    abstract class MetricAndImperial<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.MetricAndImperial
    }

    abstract class Metric<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), MetricScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.Metric
    }

    abstract class Imperial<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), ImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.Imperial
    }

    abstract class UKImperial<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), UKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.UKImperial
    }

    abstract class USCustomary<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), USCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.USCustomary
    }

    abstract class MetricAndUKImperial<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    abstract class MetricAndUSCustomary<CustomQuantity> : CustomUndefinedScientificUnit<CustomQuantity>(), MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Custom<CustomQuantity>>> {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}

sealed class DividedUndefinedScientificUnit<
    NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>
    > : UndefinedScientificUnit<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>() {
    abstract val numerator: NumeratorUnit
    abstract val denominator: DenominatorUnit
    override val quantityType by lazy {
        PhysicalQuantity.Undefined.QuantityType.Dividing(numerator.quantityType, denominator.quantityType)
    }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        numerator.numeratorUnits + denominator.denominatorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        denominator.numeratorUnits + numerator.denominatorUnits
    }

    override fun fromSIUnit(value: Decimal): Decimal = denominator.toSIUnit(numerator.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = denominator.fromSIUnit(numerator.toSIUnit(value))

    data class MetricAndImperial<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndImperial {
        override val system = MeasurementSystem.MetricAndImperial
    }

    data class Metric<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), MetricScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetric,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), ImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInImperial {
        override val system = MeasurementSystem.Imperial
    }

    data class UKImperial<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), UKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUKImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), USCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    data class MetricAndUSCustomary<
        NumeratorQuantity : PhysicalQuantity.Undefined.QuantityType,
        NumeratorUnit,
        DenominatorQuantity : PhysicalQuantity.Undefined.QuantityType,
        DenominatorUnit
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(), MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>> where
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    NumeratorUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    DenominatorUnit : MeasurementUsage.UsedInMetricAndUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}

sealed class MultipliedUndefinedScientificUnit<
    LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity.Undefined.QuantityType,
    RightUnit : UndefinedScientificUnit<RightQuantity>
    > : UndefinedScientificUnit<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>() {
    abstract val left: LeftUnit
    abstract val right: RightUnit

    override val quantityType by lazy { PhysicalQuantity.Undefined.QuantityType.Multiplying(left.quantityType, right.quantityType) }
    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        left.numeratorUnits + right.numeratorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        left.denominatorUnits + right.denominatorUnits
    }

    override fun fromSIUnit(value: Decimal): Decimal = left.fromSIUnit(right.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = left.toSIUnit(right.toSIUnit(value))

    data class MetricAndImperial<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndImperial
    {
        override val system = MeasurementSystem.MetricAndImperial
    }

    data class Metric<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        MetricScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric
    {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        ImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInImperial
    {
        override val system = MeasurementSystem.Imperial
    }

    data class UKImperial<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial
    {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        USCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUKImperial
    {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    data class MetricAndUSCustomary<
        LeftQuantity : PhysicalQuantity.Undefined.QuantityType,
        LeftUnit,
        RightQuantity : PhysicalQuantity.Undefined.QuantityType,
        RightUnit
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit
    ) : MultipliedUndefinedScientificUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Multiplying<LeftQuantity, RightQuantity>>> where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetricAndUSCustomary
    {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}

sealed class InvertedUndefinedScientificUnit<
    InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
    InverseUnit : UndefinedScientificUnit<InverseQuantity>
    > : UndefinedScientificUnit<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>() {
    abstract val inverse: InverseUnit

    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        inverse.denominatorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        inverse.numeratorUnits
    }

    override val quantityType: PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity> by lazy {
        PhysicalQuantity.Undefined.QuantityType.Inverse(inverse.quantityType)
    }

    override fun fromSIUnit(value: Decimal): Decimal = inverse.fromSIUnit(1.0.toDecimal() / value)
    override fun toSIUnit(value: Decimal): Decimal = inverse.toSIUnit(1.0.toDecimal() / value)

    data class MetricAndImperial<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndImperial {
        override val system = MeasurementSystem.MetricAndImperial
    }

    data class Metric<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        MetricScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        ImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInImperial {
        override val system = MeasurementSystem.Imperial
    }

    data class UKImperial<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        UKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        USCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    data class MetricAndUSCustomary<
        InverseQuantity : PhysicalQuantity.Undefined.QuantityType,
        InverseUnit
        > internal constructor(override val inverse: InverseUnit) : InvertedUndefinedScientificUnit<InverseQuantity, InverseUnit>(),
        MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<PhysicalQuantity.Undefined.QuantityType.Inverse<InverseQuantity>>> where
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}
