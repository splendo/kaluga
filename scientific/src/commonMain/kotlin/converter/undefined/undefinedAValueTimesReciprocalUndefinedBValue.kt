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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("undefinedAValueTimesReciprocalUndefinedBValue")
fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	TargetUnit : UndefinedDividedUnit<LeftQuantity, LeftUnit, RightReciprocalQuantity, RightReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftQuantity, RightReciprocalQuantity>, TargetUnit>
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
	leftUnitPerRightReciprocalUnit: LeftUnit.(RightReciprocalUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.leftUnitPerRightReciprocalUnit(right.unit.inverse).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueTimesMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueTimesMetricReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueTimesImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueTimesUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueTimesUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueTimesMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueTimesMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftQuantity,
				LeftUnit,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

