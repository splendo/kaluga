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
import com.splendo.kaluga.scientific.unit.reciprocal
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedCValueTimesReciprocalUndefinedAXUndefinedBValue")
fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<LeftDenominatorQuantity, LeftDenominatorUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
	leftDenominatorUnitXRightReciprocalRightUnit: LeftDenominatorUnit.(RightReciprocalRightUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.denominator.leftDenominatorUnitXRightReciprocalRightUnit(right.unit.inverse.right).reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedCValueTimesMetricAndImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedCValueTimesMetricReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.Metric<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedCValueTimesImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.Imperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedCValueTimesUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedCValueTimesUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedCValueTimesMetricAndUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedCValueTimesMetricAndUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightReciprocalLeftUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightReciprocalLeftQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightReciprocalLeftQuantity>,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightReciprocalLeftQuantity, LeftNumeratorAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

