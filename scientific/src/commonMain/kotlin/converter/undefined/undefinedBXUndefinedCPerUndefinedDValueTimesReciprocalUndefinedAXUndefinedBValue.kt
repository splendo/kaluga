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
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedBXUndefinedCPerUndefinedDValueTimesReciprocalUndefinedAXUndefinedBValue")
fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorQuantity, LeftDenominatorUnit, RightReciprocalLeftQuantity, RightReciprocalLeftUnit>,
	TargetUnit : UndefinedDividedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorRightQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
	leftDenominatorUnitXRightReciprocalLeftUnit: LeftDenominatorUnit.(RightReciprocalLeftUnit) -> TargetDenominatorUnit,
	leftNumeratorRightUnitPerTargetDenominatorUnit: LeftNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitPerTargetDenominatorUnit(unit.denominator.leftDenominatorUnitXRightReciprocalLeftUnit(right.unit.inverse.left)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedBXUndefinedCPerUndefinedDValueTimesMetricAndImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedBXUndefinedCPerUndefinedDValueTimesMetricReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedBXUndefinedCPerUndefinedDValueTimesImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedBXUndefinedCPerUndefinedDValueTimesUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedBXUndefinedCPerUndefinedDValueTimesUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedBXUndefinedCPerUndefinedDValueTimesMetricAndUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedBXUndefinedCPerUndefinedDValueTimesMetricAndUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalRightUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, LeftNumeratorLeftAndRightReciprocalRightQuantity, LeftNumeratorLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalLeftUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalLeftQuantity,
					RightReciprocalLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

