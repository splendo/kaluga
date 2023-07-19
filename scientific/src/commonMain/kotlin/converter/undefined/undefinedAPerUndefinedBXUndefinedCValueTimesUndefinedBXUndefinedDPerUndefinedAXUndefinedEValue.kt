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

@JvmName("undefinedAPerUndefinedBXUndefinedCValueTimesUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorRightQuantity, LeftDenominatorRightUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorRightQuantity, RightNumeratorRightUnit, UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorRightQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
	leftDenominatorRightUnitXRightDenominatorRightUnit: LeftDenominatorRightUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
	rightNumeratorRightUnitPerTargetDenominatorUnit: RightNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.right.rightNumeratorRightUnitPerTargetDenominatorUnit(unit.denominator.right.leftDenominatorRightUnitXRightDenominatorRightUnit(right.unit.denominator.right)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBXUndefinedCValueTimesMetricAndImperialUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBXUndefinedCValueTimesMetricUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBXUndefinedCValueTimesImperialUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBXUndefinedCValueTimesUKImperialUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBXUndefinedCValueTimesUSCustomaryUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBXUndefinedCValueTimesMetricAndUKImperialUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBXUndefinedCValueTimesMetricAndUSCustomaryUndefinedBXUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorLeftUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorLeftQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftQuantity>,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorAndRightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		rightNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorRightQuantity,
					LeftDenominatorRightUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

