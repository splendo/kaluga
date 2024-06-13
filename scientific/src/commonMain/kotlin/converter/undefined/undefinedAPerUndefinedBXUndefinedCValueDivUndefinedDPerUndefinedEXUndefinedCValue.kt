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

@JvmName("undefinedAPerUndefinedBXUndefinedCValueDivUndefinedDPerUndefinedEXUndefinedCValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
	numeratorNumeratorUnitXDenominatorDenominatorLeftUnit: NumeratorNumeratorUnit.(DenominatorDenominatorLeftUnit) -> TargetNumeratorUnit,
	numeratorDenominatorLeftUnitXDenominatorNumeratorUnit: NumeratorDenominatorLeftUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorLeftUnit(right.unit.denominator.left).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.left.numeratorDenominatorLeftUnitXDenominatorNumeratorUnit(right.unit.numerator)).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndImperialUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBXUndefinedCValueDivMetricUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBXUndefinedCValueDivImperialUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBXUndefinedCValueDivUKImperialUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivUSCustomaryUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUKImperialUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUSCustomaryUndefinedDPerUndefinedEXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftUnit,
	NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftQuantity, NumeratorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorLeftUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorLeftQuantity,
					DenominatorDenominatorLeftUnit
				>,
				UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorDenominatorLeftQuantity,
					NumeratorDenominatorLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

