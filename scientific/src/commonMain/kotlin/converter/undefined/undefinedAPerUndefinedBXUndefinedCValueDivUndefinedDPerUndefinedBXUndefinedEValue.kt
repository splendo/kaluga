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

@JvmName("undefinedAPerUndefinedBXUndefinedCValueDivUndefinedDPerUndefinedBXUndefinedEValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	TargetDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit, DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>, TargetNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>, TargetDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>, UndefinedQuantityType.Multiplying<NumeratorDenominatorRightQuantity, DenominatorNumeratorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
	numeratorNumeratorUnitXDenominatorDenominatorRightUnit: NumeratorNumeratorUnit.(DenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
	numeratorDenominatorRightUnitXDenominatorNumeratorUnit: NumeratorDenominatorRightUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
	targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorRightUnit(right.unit.denominator.right).targetNumeratorUnitPerTargetDenominatorUnit(unit.denominator.right.numeratorDenominatorRightUnitXDenominatorNumeratorUnit(right.unit.numerator)).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndImperialUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

@JvmName("metricUndefinedAPerUndefinedBXUndefinedCValueDivMetricUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

@JvmName("imperialUndefinedAPerUndefinedBXUndefinedCValueDivImperialUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

@JvmName("ukImperialUndefinedAPerUndefinedBXUndefinedCValueDivUKImperialUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

@JvmName("usCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivUSCustomaryUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUKImperialUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUSCustomaryUndefinedDPerUndefinedBXUndefinedEValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorRightQuantity : UndefinedQuantityType,
	DenominatorDenominatorRightUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorRightUnit : UndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, NumeratorDenominatorLeftAndDenominatorDenominatorLeftUnit, DenominatorDenominatorRightQuantity, DenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity, DenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorDenominatorRightUnit = { x(it) },
		numeratorDenominatorRightUnitXDenominatorNumeratorUnit = { x(it) },
		targetNumeratorUnitPerTargetDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorDenominatorRightQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorNumeratorQuantity,
					NumeratorNumeratorUnit,
					DenominatorDenominatorRightQuantity,
					DenominatorDenominatorRightUnit
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

