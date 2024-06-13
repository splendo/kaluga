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

@JvmName("undefinedAXUndefinedCPerUndefinedDValueTimesReciprocalUndefinedAXUndefinedBValue")
fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorQuantity, LeftDenominatorUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	TargetUnit : UndefinedDividedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorRightQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
	leftDenominatorUnitXRightReciprocalRightUnit: LeftDenominatorUnit.(RightReciprocalRightUnit) -> TargetDenominatorUnit,
	leftNumeratorRightUnitPerTargetDenominatorUnit: LeftNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitPerTargetDenominatorUnit(unit.denominator.leftDenominatorUnitXRightReciprocalRightUnit(right.unit.inverse.right)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedCPerUndefinedDValueTimesMetricAndImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedCPerUndefinedDValueTimesMetricReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedCPerUndefinedDValueTimesImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedCPerUndefinedDValueTimesUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedCPerUndefinedDValueTimesUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedCPerUndefinedDValueTimesMetricAndUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedCPerUndefinedDValueTimesMetricAndUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightReciprocalLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity>,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightReciprocalLeftQuantity, LeftNumeratorLeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightReciprocalRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightReciprocalRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightReciprocalRightQuantity,
					RightReciprocalRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

