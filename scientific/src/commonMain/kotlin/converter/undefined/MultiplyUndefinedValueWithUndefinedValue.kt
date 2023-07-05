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
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    RightQuantity : UndefinedQuantityType,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    MultipliedUnit : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, MultipliedUnit>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    multiply: LeftUnit.(RightUnit) -> MultipliedUnit,
    factory: (Decimal, MultipliedUnit) -> TargetValue
) = unit.multiply(right.unit).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedValueTimesMetricAndImperialUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.MetricAndImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.MetricAndImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInMetric,
         LeftUnit : MeasurementUsage.UsedInUKImperial,
         LeftUnit : MeasurementUsage.UsedInUSCustomary,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInMetric,
         RightUnit : MeasurementUsage.UsedInUKImperial,
         RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { x(it) }, factory)

@JvmName("metricUndefinedValueTimesMetricUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInMetric,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInMetric = times(right, { x(it) }, factory)

@JvmName("imperialUndefinedValueTimesImperialUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.Imperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.Imperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInUKImperial,
         LeftUnit : MeasurementUsage.UsedInUSCustomary,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInUKImperial,
         RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { x(it) }, factory)

@JvmName("ukImperialUndefinedValueTimesUKImperialUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInUKImperial,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInUKImperial = times(right, { x(it) }, factory)

@JvmName("usCustomaryUndefinedValueTimesUSCustomaryUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInUSCustomary,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { x(it) }, factory)

@JvmName("metricAndUKImperialUndefinedValueTimesMetricAndUKImperialUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInMetric,
         LeftUnit : MeasurementUsage.UsedInUKImperial,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInMetric,
         RightUnit : MeasurementUsage.UsedInUKImperial = times(right, { x(it) }, factory)

@JvmName("metricAndUSCustomaryUndefinedValueTimesMetricAndUSCustomaryUndefinedValue")
fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>, UndefinedMultipliedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit>>
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
    right : UndefinedScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, UndefinedMultipliedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit>) -> TargetValue
)  where LeftUnit : UndefinedScientificUnit<LeftQuantity>,
         LeftUnit : MeasurementUsage.UsedInMetric,
         LeftUnit : MeasurementUsage.UsedInUSCustomary,
         RightUnit : UndefinedScientificUnit<RightQuantity>,
         RightUnit : MeasurementUsage.UsedInMetric,
         RightUnit : MeasurementUsage.UsedInUSCustomary = times(right, { x(it) }, factory)

@JvmName("metricAndImperialUndefinedValueTimesMetricAndImperialUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.MetricAndImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricUndefinedValueTimesMetricUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("imperialUndefinedValueTimesImperialUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.Imperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("ukImperialUndefinedValueTimesUKImperialUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUKImperial
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("usCustomaryUndefinedValueTimesUSCustomaryUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricAndUKImperialUndefinedValueTimesMetricAndUKImperialUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUKImperial,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUKImperial
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricAndUSCustomaryUndefinedValueTimesMetricAndUSCustomaryUndefinedValueDefault")
infix operator fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit
    > UndefinedScientificValue<LeftQuantity, LeftUnit>.times(right : UndefinedScientificValue<RightQuantity, RightUnit>) where
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    LeftUnit : MeasurementUsage.UsedInMetric,
    LeftUnit : MeasurementUsage.UsedInUSCustomary,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    RightUnit : MeasurementUsage.UsedInMetric,
    RightUnit : MeasurementUsage.UsedInUSCustomary
    = times(right) { value: Decimal, unit: UndefinedMultipliedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> -> DefaultUndefinedScientificValue(value, unit) }
