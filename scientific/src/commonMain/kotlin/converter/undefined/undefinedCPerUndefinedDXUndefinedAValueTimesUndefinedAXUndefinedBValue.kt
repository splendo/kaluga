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

@JvmName("undefinedCPerUndefinedDXUndefinedAValueTimesUndefinedAXUndefinedBValue")
fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, RightRightQuantity, RightRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>, TargetNumeratorUnit, LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>, LeftDenominatorLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
	leftNumeratorUnitXRightRightUnit: LeftNumeratorUnit.(RightRightUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerLeftDenominatorLeftUnit: TargetNumeratorUnit.(LeftDenominatorLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.leftNumeratorUnitXRightRightUnit(right.unit.right).targetNumeratorUnitPerLeftDenominatorLeftUnit(unit.denominator.left).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedCPerUndefinedDXUndefinedAValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedCPerUndefinedDXUndefinedAValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedCPerUndefinedDXUndefinedAValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedCPerUndefinedDXUndefinedAValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedCPerUndefinedDXUndefinedAValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedCPerUndefinedDXUndefinedAValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedCPerUndefinedDXUndefinedAValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftNumeratorUnitXRightRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, RightRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					RightRightQuantity,
					RightRightUnit
				>,
				LeftDenominatorLeftQuantity,
				LeftDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

