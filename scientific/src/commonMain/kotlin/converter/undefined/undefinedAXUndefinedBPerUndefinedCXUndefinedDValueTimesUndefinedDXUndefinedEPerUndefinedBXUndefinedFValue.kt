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

@JvmName("undefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, RightDenominatorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
	leftNumeratorLeftUnitXRightNumeratorRightUnit: LeftNumeratorLeftUnit.(RightNumeratorRightUnit) -> TargetNumeratorUnit,
	leftDenominatorLeftUnitXRightDenominatorRightUnit: LeftDenominatorLeftUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorRightUnit(right.unit.numerator.right).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.left.leftDenominatorLeftUnitXRightDenominatorRightUnit(right.unit.denominator.right)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndImperialUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
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
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
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
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesImperialUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
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
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUKImperialUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUSCustomaryUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndUKImperialUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
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
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndUSCustomaryUndefinedDXUndefinedEPerUndefinedBXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
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
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
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
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightNumeratorLeftQuantity, LeftDenominatorRightAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		leftDenominatorLeftUnitXRightDenominatorRightUnit = { x(it) },
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

