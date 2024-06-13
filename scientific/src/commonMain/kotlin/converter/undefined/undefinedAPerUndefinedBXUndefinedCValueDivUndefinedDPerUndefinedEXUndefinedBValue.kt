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

@JvmName("undefinedAPerUndefinedBXUndefinedCValueDivUndefinedDPerUndefinedEXUndefinedBValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit, DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorLeftQuantity>, UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
	numeratorNumeratorUnitXDenominatorDenominatorLeftUnit: NumeratorNumeratorUnit.(DenominatorDenominatorLeftUnit) -> TargetNumeratorUnit,
	numeratorDenominatorRightUnitXDenominatorNumeratorUnit: NumeratorDenominatorRightUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorLeftUnit(right.unit.denominator.left).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.right.numeratorDenominatorRightUnitXDenominatorNumeratorUnit(right.unit.numerator)).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndImperialUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
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
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBXUndefinedCValueDivMetricUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBXUndefinedCValueDivImperialUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBXUndefinedCValueDivUKImperialUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivUSCustomaryUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUKImperialUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUSCustomaryUndefinedDPerUndefinedEXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorLeftUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
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
				UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorDenominatorRightQuantity,
					NumeratorDenominatorRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

