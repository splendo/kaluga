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

@JvmName("undefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesUndefinedAPerUndefinedBXUndefinedCValue")
fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorRightQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
	leftDenominatorLeftUnitXRightDenominatorRightUnit: LeftDenominatorLeftUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
	leftNumeratorRightUnitPerTargetDenominatorUnit: LeftNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitPerTargetDenominatorUnit(unit.denominator.left.leftDenominatorLeftUnitXRightDenominatorRightUnit(right.unit.denominator.right)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesMetricAndImperialUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesMetricUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesImperialUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesUKImperialUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesUSCustomaryUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesMetricAndUKImperialUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedBXUndefinedDPerUndefinedEXUndefinedAValueTimesMetricAndUSCustomaryUndefinedAPerUndefinedBXUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorRightAndRightNumeratorQuantity, LeftDenominatorRightAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
		leftNumeratorRightUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

