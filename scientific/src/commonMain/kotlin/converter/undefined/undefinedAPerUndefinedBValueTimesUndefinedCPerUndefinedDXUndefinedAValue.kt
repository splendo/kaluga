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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueTimesUndefinedCPerUndefinedDXUndefinedAValue")
fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorQuantity, LeftDenominatorUnit, RightDenominatorLeftQuantity, RightDenominatorLeftUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
	leftDenominatorUnitXRightDenominatorLeftUnit: LeftDenominatorUnit.(RightDenominatorLeftUnit) -> TargetDenominatorUnit,
	rightNumeratorUnitPerTargetDenominatorUnit: RightNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.rightNumeratorUnitPerTargetDenominatorUnit(unit.denominator.leftDenominatorUnitXRightDenominatorLeftUnit(right.unit.denominator.left)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueTimesMetricAndImperialUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueTimesMetricUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueTimesImperialUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueTimesUKImperialUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueTimesUSCustomaryUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueTimesMetricAndUKImperialUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueTimesMetricAndUSCustomaryUndefinedCPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorUnitXRightDenominatorLeftUnit = { x(it) },
		rightNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

