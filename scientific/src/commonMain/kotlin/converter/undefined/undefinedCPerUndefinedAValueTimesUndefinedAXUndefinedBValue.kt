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

@JvmName("undefinedCPerUndefinedAValueTimesUndefinedAXUndefinedBValue")
fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	TargetUnit : UndefinedMultipliedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, RightRightQuantity, RightRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
	leftNumeratorUnitXRightRightUnit: LeftNumeratorUnit.(RightRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.leftNumeratorUnitXRightRightUnit(right.unit.right).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedCPerUndefinedAValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedCPerUndefinedAValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedCPerUndefinedAValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedCPerUndefinedAValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedCPerUndefinedAValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedCPerUndefinedAValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedCPerUndefinedAValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightLeftQuantity>,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightLeftQuantity, LeftDenominatorAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				LeftNumeratorQuantity,
				LeftNumeratorUnit,
				RightRightQuantity,
				RightRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

