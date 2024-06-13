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

@JvmName("undefinedAXUndefinedBPerUndefinedCValueTimesUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, TargetNumeratorUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightDenominatorRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
	leftNumeratorLeftUnitXRightNumeratorRightUnit: LeftNumeratorLeftUnit.(RightNumeratorRightUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerRightDenominatorRightUnit: TargetNumeratorUnit.(RightDenominatorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorRightUnit(right.unit.numerator.right).targetNumeratorUnitPerRightDenominatorRightUnit(right.unit.denominator.right).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndImperialUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
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
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueTimesMetricUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueTimesImperialUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueTimesUKImperialUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesUSCustomaryUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUKImperialUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUSCustomaryUndefinedCXUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorLeftUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorRightAndRightDenominatorLeftQuantity, LeftNumeratorRightAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorRightAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
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
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

