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
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBPerUndefinedCValueTimesUndefinedDXUndefinedCPerUndefinedAValue")
fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	TargetUnit : UndefinedMultipliedUnit<LeftNumeratorRightQuantity, LeftNumeratorRightUnit, RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftNumeratorRightQuantity, RightNumeratorLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
	leftNumeratorRightUnitXRightNumeratorLeftUnit: LeftNumeratorRightUnit.(RightNumeratorLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.leftNumeratorRightUnitXRightNumeratorLeftUnit(right.unit.numerator.left).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndImperialUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueTimesMetricUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueTimesImperialUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueTimesUKImperialUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesUSCustomaryUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorQuantity>,
	LeftNumeratorLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightUnit : UndefinedScientificUnit<LeftNumeratorRightQuantity>,
	LeftNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit, LeftNumeratorRightQuantity, LeftNumeratorRightUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUKImperialUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUSCustomaryUndefinedDXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorLeftAndRightDenominatorUnit,
	LeftNumeratorRightQuantity : UndefinedQuantityType,
	LeftNumeratorRightUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorLeftAndRightDenominatorQuantity>, RightUnit>,
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
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorRightQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorLeftAndRightDenominatorQuantity, LeftNumeratorLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorRightUnitXRightNumeratorLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				LeftNumeratorRightQuantity,
				LeftNumeratorRightUnit,
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

