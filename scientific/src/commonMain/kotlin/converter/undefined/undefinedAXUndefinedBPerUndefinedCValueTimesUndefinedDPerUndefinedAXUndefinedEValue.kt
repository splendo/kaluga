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

@JvmName("undefinedAXUndefinedBPerUndefinedCValueTimesUndefinedDPerUndefinedAXUndefinedEValue")
fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, RightNumeratorQuantity, RightNumeratorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorQuantity, LeftDenominatorUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
	leftNumeratorRightUnitXRightNumeratorUnit: LeftNumeratorRightUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
	leftDenominatorUnitXRightDenominatorRightUnit: LeftDenominatorUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitXRightNumeratorUnit(right.unit.numerator).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.leftDenominatorUnitXRightDenominatorRightUnit(right.unit.denominator.right)).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndImperialUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
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
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueTimesMetricUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueTimesImperialUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueTimesUKImperialUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesUSCustomaryUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
	LeftNumeratorLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUKImperialUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUSCustomaryUndefinedDPerUndefinedAXUndefinedEValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorLeftUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>>, RightUnit>,
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
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity, LeftNumeratorLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorLeftQuantity, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorUnit = { x(it) },
		leftDenominatorUnitXRightDenominatorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftNumeratorRightQuantity,
					LeftNumeratorRightUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				UndefinedQuantityType.Multiplying<LeftDenominatorQuantity, RightDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftDenominatorQuantity,
					LeftDenominatorUnit,
					RightDenominatorRightQuantity,
					RightDenominatorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

