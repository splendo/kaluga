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

@JvmName("undefinedAXUndefinedBPerUndefinedCValueDivUndefinedBXUndefinedDPerUndefinedEValue")
fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorQuantity, NumeratorDenominatorUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>, UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorNumeratorRightQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
	numeratorNumeratorLeftUnitXDenominatorDenominatorUnit: NumeratorNumeratorLeftUnit.(DenominatorDenominatorUnit) -> TargetNumeratorUnit,
	numeratorDenominatorUnitXDenominatorNumeratorRightUnit: NumeratorDenominatorUnit.(DenominatorNumeratorRightUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.numeratorNumeratorLeftUnitXDenominatorDenominatorUnit(right.unit.denominator).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.numeratorDenominatorUnitXDenominatorNumeratorRightUnit(right.unit.numerator.right)).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndImperialUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueDivMetricUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueDivImperialUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueDivUKImperialUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivUSCustomaryUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUKImperialUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUSCustomaryUndefinedBXUndefinedDPerUndefinedEValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit,
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
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		numeratorDenominatorUnitXDenominatorNumeratorRightUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorNumeratorLeftQuantity,
					NumeratorNumeratorLeftUnit,
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

