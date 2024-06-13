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

@JvmName("undefinedAXUndefinedBValueTimesReciprocalUndefinedBValue")
fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftLeftValue : UndefinedScientificValue<LeftLeftQuantity, LeftLeftUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
	factory: (Decimal, LeftLeftUnit) -> LeftLeftValue
) = unit.left.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXUndefinedBValueTimesGenericReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

