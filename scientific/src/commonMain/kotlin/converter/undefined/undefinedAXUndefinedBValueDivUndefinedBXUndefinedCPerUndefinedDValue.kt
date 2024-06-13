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

@JvmName("undefinedAXUndefinedBValueDivUndefinedBXUndefinedCPerUndefinedDValue")
fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>, TargetNumeratorUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>, DenominatorNumeratorRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
	numeratorLeftUnitXDenominatorDenominatorUnit: NumeratorLeftUnit.(DenominatorDenominatorUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerDenominatorNumeratorRightUnit: TargetNumeratorUnit.(DenominatorNumeratorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.left.numeratorLeftUnitXDenominatorDenominatorUnit(right.unit.denominator).targetNumeratorUnitPerDenominatorNumeratorRightUnit(right.unit.numerator.right).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueDivMetricUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.Metric<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueDivImperialUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueDivUKImperialUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueDivUSCustomaryUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedBXUndefinedCPerUndefinedDValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorNumeratorLeftUnit,
	NumeratorUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity>,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<NumeratorRightAndDenominatorNumeratorLeftQuantity, NumeratorRightAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorRightAndDenominatorNumeratorLeftQuantity, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, DenominatorDenominatorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorLeftQuantity,
					NumeratorLeftUnit,
					DenominatorDenominatorQuantity,
					DenominatorDenominatorUnit
				>,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

