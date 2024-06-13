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

@JvmName("undefinedAXUndefinedBValueTimesUndefinedCPerUndefinedAValue")
fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	TargetUnit : UndefinedMultipliedUnit<LeftRightQuantity, LeftRightUnit, RightNumeratorQuantity, RightNumeratorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftRightQuantity, RightNumeratorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
	leftRightUnitXRightNumeratorUnit: LeftRightUnit.(RightNumeratorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.right.leftRightUnitXRightNumeratorUnit(right.unit.numerator).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorUnit,
	LeftRightQuantity : UndefinedQuantityType,
	LeftRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorQuantity, LeftRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftLeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : UndefinedScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit, LeftRightQuantity, LeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftLeftAndRightDenominatorQuantity, LeftLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				LeftRightQuantity,
				LeftRightUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

