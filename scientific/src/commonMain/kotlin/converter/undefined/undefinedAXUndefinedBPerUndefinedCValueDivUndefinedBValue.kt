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
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBPerUndefinedCValueDivUndefinedBValue")
fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorLeftQuantity, NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit: NumeratorNumeratorLeftUnit.(NumeratorDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.left.numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit(unit.denominator).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndImperialUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueDivMetricUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueDivImperialUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueDivUKImperialUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUKImperialUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftUnit,
	NumeratorNumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorQuantity>,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftQuantity, NumeratorNumeratorLeftUnit, NumeratorNumeratorRightAndDenominatorQuantity, NumeratorNumeratorRightAndDenominatorUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftQuantity, NumeratorNumeratorRightAndDenominatorQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorLeftUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				NumeratorNumeratorLeftQuantity,
				NumeratorNumeratorLeftUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

