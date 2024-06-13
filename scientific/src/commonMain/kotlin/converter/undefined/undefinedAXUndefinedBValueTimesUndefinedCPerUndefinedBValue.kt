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

@JvmName("undefinedAXUndefinedBValueTimesUndefinedCPerUndefinedBValue")
fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	TargetUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit, RightNumeratorQuantity, RightNumeratorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorQuantity, RightNumeratorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
	leftRightAndRightDenominatorUnitXRightNumeratorUnit: LeftRightAndRightDenominatorUnit.(RightNumeratorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.right.leftRightAndRightDenominatorUnitXRightNumeratorUnit(right.unit.numerator).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedCPerUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftRightAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorQuantity>,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftRightAndRightDenominatorQuantity, LeftRightAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftRightAndRightDenominatorUnitXRightNumeratorUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				LeftRightAndRightDenominatorQuantity,
				LeftRightAndRightDenominatorUnit,
				RightNumeratorQuantity,
				RightNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

