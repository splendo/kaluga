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

@JvmName("undefinedAXUndefinedBPerUndefinedCValueDivUndefinedBPerUndefinedDValue")
fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>, TargetNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, DenominatorDenominatorQuantity>, NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
	numeratorNumeratorLeftUnitXDenominatorDenominatorUnit: NumeratorNumeratorLeftUnit.(DenominatorDenominatorUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerNumeratorDenominatorUnit: TargetNumeratorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.numeratorNumeratorLeftUnitXDenominatorDenominatorUnit(right.unit.denominator).targetNumeratorUnitPerNumeratorDenominatorUnit(unit.denominator).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndImperialUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueDivMetricUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueDivImperialUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueDivUKImperialUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivUSCustomaryUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUKImperialUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUSCustomaryUndefinedBPerUndefinedDValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorNumeratorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity, NumeratorNumeratorRightAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitXDenominatorDenominatorUnit = { x(it) },
		targetNumeratorUnitPerNumeratorDenominatorUnit = { per(it) },
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
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

