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

@JvmName("undefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesUndefinedAXUndefinedBPerUndefinedCValue")
fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>, TargetNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>, LeftDenominatorRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
	leftNumeratorRightUnitXRightNumeratorRightUnit: LeftNumeratorRightUnit.(RightNumeratorRightUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerLeftDenominatorRightUnit: TargetNumeratorUnit.(LeftDenominatorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitXRightNumeratorRightUnit(right.unit.numerator.right).targetNumeratorUnitPerLeftDenominatorRightUnit(unit.denominator.right).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesMetricAndImperialUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
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
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
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
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesMetricUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesImperialUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesUKImperialUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesUSCustomaryUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesMetricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedCXUndefinedDPerUndefinedAXUndefinedEValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorLeftUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity, LeftDenominatorLeftAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorRightQuantity,
					RightNumeratorRightUnit
				>,
				LeftDenominatorRightQuantity,
				LeftDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

