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

@JvmName("undefinedAXUndefinedBValueTimesUndefinedCPerUndefinedDXUndefinedBValue")
fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, RightNumeratorQuantity, RightNumeratorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>, TargetNumeratorUnit, RightDenominatorLeftQuantity, RightDenominatorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>, RightDenominatorLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
	leftLeftUnitXRightNumeratorUnit: LeftLeftUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerRightDenominatorLeftUnit: TargetNumeratorUnit.(RightDenominatorLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.left.leftLeftUnitXRightNumeratorUnit(right.unit.numerator).targetNumeratorUnitPerRightDenominatorLeftUnit(right.unit.denominator.left).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedCPerUndefinedDXUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : UndefinedQuantityType,
	LeftLeftUnit,
	LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorRightUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftUnit : UndefinedScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftQuantity, LeftLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftRightAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftRightAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftLeftQuantity, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftLeftQuantity,
					LeftLeftUnit,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

