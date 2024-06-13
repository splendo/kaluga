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

@JvmName("undefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorRightQuantity, LeftDenominatorRightUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorRightQuantity, RightDenominatorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
	leftNumeratorRightUnitXRightNumeratorLeftUnit: LeftNumeratorRightUnit.(RightNumeratorLeftUnit) -> TargetNumeratorUnit,
	leftDenominatorRightUnitXRightDenominatorRightUnit: LeftDenominatorRightUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitXRightNumeratorLeftUnit(right.unit.numerator.left).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.right.leftDenominatorRightUnitXRightDenominatorRightUnit(right.unit.denominator.right)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndImperialUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesImperialUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUKImperialUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesUSCustomaryUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndUKImperialUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCXUndefinedDValueTimesMetricAndUSCustomaryUndefinedEXUndefinedCPerUndefinedAXUndefinedFValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity>,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorLeftAndRightNumeratorRightQuantity, LeftDenominatorLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
		leftDenominatorRightUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorLeftQuantity,
					RightNumeratorLeftUnit
				>,
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

