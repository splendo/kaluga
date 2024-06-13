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

@JvmName("reciprocalUndefinedAXUndefinedBValueTimesUndefinedAXUndefinedCPerUndefinedDValue")
fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftReciprocalRightQuantity, LeftReciprocalRightUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorRightQuantity, RightNumeratorRightUnit, UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorRightQuantity, UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
	leftReciprocalRightUnitXRightDenominatorUnit: LeftReciprocalRightUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
	rightNumeratorRightUnitPerTargetDenominatorUnit: RightNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.right.rightNumeratorRightUnitPerTargetDenominatorUnit(unit.inverse.right.leftReciprocalRightUnitXRightDenominatorUnit(right.unit.denominator)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedAXUndefinedCPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorLeftUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
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

