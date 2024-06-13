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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBValueTimesReciprocalUndefinedCXUndefinedBValue")
fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetUnit : UndefinedDividedUnit<LeftLeftQuantity, LeftLeftUnit, RightReciprocalLeftQuantity, RightReciprocalLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftLeftQuantity, RightReciprocalLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
	leftLeftUnitPerRightReciprocalLeftUnit: LeftLeftUnit.(RightReciprocalLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.left.leftLeftUnitPerRightReciprocalLeftUnit(right.unit.inverse.left).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftRightAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftRightAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftLeftQuantity,
				LeftLeftUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

