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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBValueTimesReciprocalUndefinedAValue")
fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	LeftRightValue : UndefinedScientificValue<LeftRightQuantity, LeftRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
	factory: (Decimal, LeftRightUnit) -> LeftRightValue
) = unit.right.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXUndefinedBValueTimesGenericReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, LeftRightQuantity, LeftRightUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

