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
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : ScientificUnit<LeftQuantity>,
    ExtendedLeftUnit : UndefinedExtendedUnit<LeftQuantity>,
    ExtendedLeftValue : UndefinedScientificValue<UndefinedQuantityType.Extended<LeftQuantity>, ExtendedLeftUnit>,
    RightQuantity : UndefinedQuantityType,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    MultipliedUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftQuantity>, ExtendedLeftUnit, RightQuantity, RightUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>, MultipliedUnit>,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    leftAsUndefined: ScientificValue<LeftQuantity, LeftUnit>.() -> ExtendedLeftValue,
    multiply: ExtendedLeftUnit.(RightUnit) -> MultipliedUnit,
    factory: (Decimal, MultipliedUnit) -> TargetValue,
) = leftAsUndefined().times(right, multiply, factory)

@JvmName("metricAndImperialValueTimesUndefinedMetricAndImperialValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.MetricAndImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricValueTimesUndefinedMetricValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.Metric<UndefinedQuantityType.Extended<LeftQuantity>, WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>, RightQuantity, RightUnit>,>,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.Metric<UndefinedQuantityType.Extended<LeftQuantity>, WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>, RightQuantity, RightUnit>,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("imperialValueTimesUndefinedImperialValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.Imperial<UndefinedQuantityType.Extended<LeftQuantity>, WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>, RightQuantity, RightUnit>,>,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.Imperial<UndefinedQuantityType.Extended<LeftQuantity>, WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>, RightQuantity, RightUnit>,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("ukImperialValueTimesUndefinedUKImperialValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.UKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.UKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("usCustomaryValueTimesUndefinedUSCustomaryValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.USCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.USCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndUKImperialValueTimesUndefinedMetricAndUKImperialValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.MetricAndUKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndUKImperial<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndUSCustomaryValueTimesUndefinedMetricAndUSCustomaryValue")
fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>,
        UndefinedMultipliedUnit.MetricAndUSCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (
        Decimal,
        UndefinedMultipliedUnit.MetricAndUSCustomary<
            UndefinedQuantityType.Extended<LeftQuantity>,
            WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
            RightQuantity,
            RightUnit,
            >,
    ) -> TargetValue,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right, { asUndefined() }, { x(it) }, factory)

@JvmName("metricAndImperialValueTimesUndefinedMetricAndImperialValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.MetricAndImperial<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }

@JvmName("metricValueTimesUndefinedMetricValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.Metric<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }

@JvmName("imperialValueTimesUndefinedImperialValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.Imperial<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }

@JvmName("ukImperialValueTimesUndefinedUKImperialValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.UKImperial<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }

@JvmName("usCustomaryValueTimesUndefinedUSCustomaryValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.USCustomary<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }

@JvmName("metricAndUKImperialValueTimesUndefinedMetricAndUKImperialValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.MetricAndUKImperial<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }

@JvmName("metricAndUSCustomaryValueTimesUndefinedMetricAndUSCustomaryValueDefault")
infix operator fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > ScientificValue<LeftQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) {
            value: Decimal,
            unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
                UndefinedQuantityType.Extended<LeftQuantity>,
                WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
                RightQuantity,
                RightUnit,
                >,
        ->
        DefaultUndefinedScientificValue(value, unit)
    }
