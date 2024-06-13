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

import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialUndefinedBPerUndefinedAValueTimesMetricAndImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBPerUndefinedAValueTimesMetricUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBPerUndefinedAValueTimesImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBPerUndefinedAValueTimesUKImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBPerUndefinedAValueTimesUSCustomaryUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBPerUndefinedAValueTimesMetricAndUKImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBPerUndefinedAValueTimesMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericUndefinedBPerUndefinedAValueTimesGenericUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftDenominatorAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) =
	right times this

