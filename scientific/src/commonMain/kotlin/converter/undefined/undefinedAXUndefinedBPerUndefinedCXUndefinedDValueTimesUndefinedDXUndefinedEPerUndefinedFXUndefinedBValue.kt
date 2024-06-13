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

@JvmName("undefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, RightDenominatorLeftQuantity, RightDenominatorLeftUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
	leftNumeratorLeftUnitXRightNumeratorRightUnit: LeftNumeratorLeftUnit.(RightNumeratorRightUnit) -> TargetNumeratorUnit,
	leftDenominatorLeftUnitXRightDenominatorLeftUnit: LeftDenominatorLeftUnit.(RightDenominatorLeftUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorRightUnit(right.unit.numerator.right).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.left.leftDenominatorLeftUnitXRightDenominatorLeftUnit(right.unit.denominator.left)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndImperialUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesImperialUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUKImperialUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUSCustomaryUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndUKImperialUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndUSCustomaryUndefinedDXUndefinedEPerUndefinedFXUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorRightAndRightDenominatorRightQuantity, LeftNumeratorRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorLeftUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftNumeratorLeftQuantity,
					LeftNumeratorLeftUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorLeftQuantity,
					LeftDenominatorLeftUnit,
					RightDenominatorLeftQuantity,
					RightDenominatorLeftUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

