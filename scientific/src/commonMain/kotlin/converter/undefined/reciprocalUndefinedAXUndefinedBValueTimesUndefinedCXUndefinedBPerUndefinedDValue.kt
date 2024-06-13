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

@JvmName("reciprocalUndefinedAXUndefinedBValueTimesUndefinedCXUndefinedBPerUndefinedDValue")
fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorLeftQuantity, UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
	leftReciprocalLeftUnitXRightDenominatorUnit: LeftReciprocalLeftUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
	rightNumeratorLeftUnitPerTargetDenominatorUnit: RightNumeratorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.left.rightNumeratorLeftUnitPerTargetDenominatorUnit(unit.inverse.left.leftReciprocalLeftUnitXRightDenominatorUnit(right.unit.denominator)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedCXUndefinedBPerUndefinedDValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalRightAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalLeftUnitXRightDenominatorUnit = { x(it) },
		rightNumeratorLeftUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, RightDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftReciprocalLeftQuantity,
					LeftReciprocalLeftUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

