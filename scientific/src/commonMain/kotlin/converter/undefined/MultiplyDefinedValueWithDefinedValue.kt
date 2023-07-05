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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultUndefinedScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.asUndefined
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : ScientificUnit<LeftQuantity>,
    ExtendedLeftUnit : UndefinedExtendedUnit<LeftQuantity>,
    ExtendedLeftValue : UndefinedScientificValue<UndefinedQuantityType.Extended<LeftQuantity>, ExtendedLeftUnit>,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : ScientificUnit<RightQuantity>,
    ExtendedRightUnit : UndefinedExtendedUnit<RightQuantity>,
    ExtendedRightValue : UndefinedScientificValue<UndefinedQuantityType.Extended<RightQuantity>, ExtendedRightUnit>,
    MultipliedUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftQuantity>, ExtendedLeftUnit, UndefinedQuantityType.Extended<RightQuantity>, ExtendedRightUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>, MultipliedUnit>
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    leftAsUndefined: ScientificValue<LeftQuantity, LeftUnit>.() -> ExtendedLeftValue,
    rightAsUndefined: ScientificValue<RightQuantity, RightUnit>.() -> ExtendedRightValue,
    multiply: ExtendedLeftUnit.(ExtendedRightUnit) -> MultipliedUnit,
    factory: (Decimal, MultipliedUnit) -> TargetValue
) = leftAsUndefined().times(right, rightAsUndefined, multiply, factory)

@JvmName("metricAndImperialDefinedValueTimesMetricAndImperialDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.MetricAndImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
            >
        ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricDefinedValueTimesMetricDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.Metric<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.Metric<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("imperialDefinedValueTimesImperialDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.Imperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.Imperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("uKImperialDefinedValueTimesUKImperialDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.UKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.UKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("uSCustomaryDefinedValueTimesUSCustomaryDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.USCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.USCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndUKImperialDefinedValueTimesMetricAndUKImperialDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.MetricAndUKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndUKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndUSCustomaryDefinedValueTimesMetricAndUSCustomaryDefinedValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.MetricAndUSCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
            >
        >
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndUSCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = asUndefined().times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndImperialDefinedValueTimesMetricAndImperialDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) {
    value: Decimal,
    unit: UndefinedMultipliedUnit.MetricAndImperial<
        UndefinedQuantityType.Extended<LeftQuantity>,
        WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
        UndefinedQuantityType.Extended<RightQuantity>,
        WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
        > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricDefinedValueTimesMetricDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric
    = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.Metric<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("imperialDefinedValueTimesImperialDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.Imperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("uKImperialDefinedValueTimesUKImperialDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial
    = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.UKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("uSCustomaryDefinedValueTimesUSCustomaryDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.USCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUKImperialDefinedValueTimesMetricAndUKImperialDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial
    = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.MetricAndUKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUSCustomaryDefinedValueTimesMetricAndUSCustomaryDefinedValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
) where
    LeftUnit : ScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}
