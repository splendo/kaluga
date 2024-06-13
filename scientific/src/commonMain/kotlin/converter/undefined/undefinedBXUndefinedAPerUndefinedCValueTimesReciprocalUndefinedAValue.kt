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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialUndefinedBXUndefinedAPerUndefinedCValueTimesMetricAndImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBXUndefinedAPerUndefinedCValueTimesMetricReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBXUndefinedAPerUndefinedCValueTimesImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBXUndefinedAPerUndefinedCValueTimesUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBXUndefinedAPerUndefinedCValueTimesUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBXUndefinedAPerUndefinedCValueTimesMetricAndUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBXUndefinedAPerUndefinedCValueTimesMetricAndUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightReciprocalUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftNumeratorRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalQuantity>,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightReciprocalQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftNumeratorRightAndRightReciprocalQuantity, LeftNumeratorRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

