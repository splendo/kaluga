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

@JvmName("undefinedAXUndefinedBValueTimesReciprocalUndefinedCXUndefinedAValue")
fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetUnit : UndefinedDividedUnit<LeftRightQuantity, LeftRightUnit, RightReciprocalLeftQuantity, RightReciprocalLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftRightQuantity, RightReciprocalLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
	leftRightUnitPerRightReciprocalLeftUnit: LeftRightUnit.(RightReciprocalLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.right.leftRightUnitPerRightReciprocalLeftUnit(right.unit.inverse.left).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedCXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalLeftQuantity,
				RightReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

