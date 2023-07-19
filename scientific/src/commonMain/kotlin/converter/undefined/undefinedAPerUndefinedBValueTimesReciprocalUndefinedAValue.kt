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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueTimesMetricAndImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedAPerUndefinedBValueTimesMetricReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedAPerUndefinedBValueTimesImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedAPerUndefinedBValueTimesUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedAPerUndefinedBValueTimesUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalQuantity>,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorAndRightReciprocalQuantity, LeftNumeratorAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

