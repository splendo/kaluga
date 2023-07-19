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

@JvmName("undefinedAXUndefinedBPerUndefinedCValueTimesUndefinedCXUndefinedDPerUndefinedBValue")
fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	TargetUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, RightNumeratorRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
	leftNumeratorLeftUnitXRightNumeratorRightUnit: LeftNumeratorLeftUnit.(RightNumeratorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorRightUnit(right.unit.numerator.right).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndImperialUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
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
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueTimesMetricUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueTimesImperialUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueTimesUKImperialUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesUSCustomaryUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUKImperialUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueTimesMetricAndUSCustomaryUndefinedCXUndefinedDPerUndefinedBValue")
infix operator fun <
	LeftNumeratorLeftQuantity : UndefinedQuantityType,
	LeftNumeratorLeftUnit,
	LeftNumeratorRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorRightAndRightDenominatorUnit,
	LeftNumeratorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorLeftUnit : UndefinedScientificUnit<LeftNumeratorLeftQuantity>,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorQuantity>,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorLeftQuantity, LeftNumeratorLeftUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorLeftQuantity, LeftNumeratorRightAndRightDenominatorQuantity>, LeftNumeratorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorRightAndRightDenominatorQuantity, LeftNumeratorRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorLeftUnitXRightNumeratorRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				LeftNumeratorLeftQuantity,
				LeftNumeratorLeftUnit,
				RightNumeratorRightQuantity,
				RightNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

