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

@JvmName("undefinedAXUndefinedBValueDivUndefinedCXUndefinedBValue")
fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, DenominatorLeftQuantity, DenominatorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorLeftQuantity, DenominatorLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
	numeratorLeftUnitPerDenominatorLeftUnit: NumeratorLeftUnit.(DenominatorLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.left.numeratorLeftUnitPerDenominatorLeftUnit(right.unit.left).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueDivMetricUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueDivImperialUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueDivUKImperialUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueDivUSCustomaryUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorRightUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorRightAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorRightQuantity>,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorRightAndDenominatorRightQuantity, NumeratorRightAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorLeftUnitPerDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				NumeratorLeftQuantity,
				NumeratorLeftUnit,
				DenominatorLeftQuantity,
				DenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

