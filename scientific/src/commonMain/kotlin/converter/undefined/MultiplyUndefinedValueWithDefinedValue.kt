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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : ScientificUnit<RightQuantity>,
    ExtendedRightUnit : UndefinedExtendedUnit<RightQuantity>,
    ExtendedRightValue : UndefinedScientificValue<UndefinedQuantityType.Extended<RightQuantity>, ExtendedRightUnit>,
    MultipliedUnit : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, UndefinedQuantityType.Extended<RightQuantity>, ExtendedRightUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>, MultipliedUnit>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    rightAsUndefined: ScientificValue<RightQuantity, RightUnit>.() -> ExtendedRightValue,
    multiply: LeftUnit.(ExtendedRightUnit) -> MultipliedUnit,
    factory: (Decimal, MultipliedUnit) -> TargetValue
) = times(right.rightAsUndefined(), multiply, factory)

@JvmName("metricAndImperialUndefinedValueTimesMetricAndImperialValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.MetricAndImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricUndefinedValueTimesMetricValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.Metric<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.Metric<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("imperialUndefinedValueTimesImperialValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.Imperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.Imperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("uKImperialUndefinedValueTimesUKImperialValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.UKImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.UKImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("uSCustomaryUndefinedValueTimesUSCustomaryValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.USCustomary<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.USCustomary<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndUKImperialUndefinedValueTimesMetricAndUKImperialValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.MetricAndUKImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndUKImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndUSCustomaryUndefinedValueTimesMetricAndUSCustomaryValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
        UndefinedMultipliedUnit.MetricAndUSCustomary<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
            >
        >
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndUSCustomary<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
            >
    ) -> TargetValue
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndImperialUndefinedValueTimesMetricAndImperialValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.MetricAndImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricUndefinedValueTimesMetricValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.Metric<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("imperialUndefinedValueTimesImperialValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.Imperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("uKImperialUndefinedValueTimesUKImperialValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.UKImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("uSCustomaryUndefinedValueTimesUSCustomaryValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.USCustomary<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUKImperialUndefinedValueTimesMetricAndUKImperialValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.MetricAndUKImperial<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}

@JvmName("metricAndUSCustomaryUndefinedValueTimesMetricAndUSCustomaryValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : ScientificValue<RightQuantity, RightUnit>
) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : ScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUSCustomary = times(right) {
        value: Decimal,
        unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
            LeftQuantity,
            LeftUnit,
            UndefinedQuantityType.Extended<RightQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
            > ->
    DefaultUndefinedScientificValue(value, unit)
}
