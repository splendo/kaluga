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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBPerUndefinedCValueDivUndefinedAXUndefinedDPerUndefinedEValue")
fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorQuantity, NumeratorDenominatorUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>, UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
	numeratorNumeratorRightUnitXDenominatorDenominatorUnit: NumeratorNumeratorRightUnit.(DenominatorDenominatorUnit) -> TargetNumeratorUnit,
	numeratorDenominatorUnitXDenominatorNumeratorRightUnit: NumeratorDenominatorUnit.(DenominatorNumeratorRightUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.numeratorNumeratorRightUnitXDenominatorDenominatorUnit(right.unit.denominator).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.numeratorDenominatorUnitXDenominatorNumeratorRightUnit(right.unit.numerator.right)).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndImperialUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueDivMetricUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueDivImperialUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueDivUKImperialUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivUSCustomaryUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUKImperialUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUSCustomaryUndefinedAXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorLeftAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorRightQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorNumeratorRightQuantity,
					NumeratorNumeratorRightUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorNumeratorRightQuantity,
					DenominatorNumeratorRightUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

