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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("denominatorTimesUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) = times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricAndImperialDenominatorTimesMetricAndImperialUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInMetric,
      DividerUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricDenominatorTimesMetricUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInMetric =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("imperialDenominatorTimesImperialUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("ukImperialDenominatorTimesUKImperialUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInUKImperial =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("usCustomaryDenominatorTimesUSCustomaryUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricAndUKImperialDenominatorTimesMetricAndUKImperialUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInMetric,
      DividerUnit : MeasurementUsage.UsedInUKImperial =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }

@JvmName("metricAndUSCustomaryDenominatorTimesMetricAndUSCustomaryUndefinedDividingUnit")
infix operator fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
) where
      LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInMetric,
      DividerUnit : MeasurementUsage.UsedInUSCustomary =
    times(right) { value: Decimal, unit: NumeratorUnit -> DefaultUndefinedScientificValue(value, unit) }
