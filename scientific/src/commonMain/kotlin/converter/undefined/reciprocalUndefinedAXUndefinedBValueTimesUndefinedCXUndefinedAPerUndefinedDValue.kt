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

@JvmName("reciprocalUndefinedAXUndefinedBValueTimesUndefinedCXUndefinedAPerUndefinedDValue")
fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftReciprocalRightQuantity, LeftReciprocalRightUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorLeftQuantity, UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
	leftReciprocalRightUnitXRightDenominatorUnit: LeftReciprocalRightUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
	rightNumeratorLeftUnitPerTargetDenominatorUnit: RightNumeratorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.left.rightNumeratorLeftUnitPerTargetDenominatorUnit(unit.inverse.right.leftReciprocalRightUnitXRightDenominatorUnit(right.unit.denominator)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedCXUndefinedAPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

