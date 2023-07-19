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

@JvmName("undefinedAXUndefinedBValueTimesReciprocalUndefinedAXUndefinedCValue")
fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetUnit : UndefinedDividedUnit<LeftRightQuantity, LeftRightUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftRightQuantity, RightReciprocalRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
	leftRightUnitPerRightReciprocalRightUnit: LeftRightUnit.(RightReciprocalRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.right.leftRightUnitPerRightReciprocalRightUnit(right.unit.inverse.right).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedAXUndefinedCValue")
infix operator fun <
	LeftLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalLeftUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalLeftQuantity>,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitPerRightReciprocalRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftRightQuantity,
				LeftRightUnit,
				RightReciprocalRightQuantity,
				RightReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

