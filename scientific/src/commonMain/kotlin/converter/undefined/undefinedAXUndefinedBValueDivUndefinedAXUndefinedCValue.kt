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

@JvmName("undefinedAXUndefinedBValueDivUndefinedAXUndefinedCValue")
fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	TargetUnit : UndefinedDividedUnit<NumeratorRightQuantity, NumeratorRightUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorRightQuantity, DenominatorRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
	numeratorRightUnitPerDenominatorRightUnit: NumeratorRightUnit.(DenominatorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.right.numeratorRightUnitPerDenominatorRightUnit(right.unit.right).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueDivMetricUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueDivImperialUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueDivUKImperialUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueDivUSCustomaryUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedAXUndefinedCValue")
infix operator fun <
	NumeratorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorLeftUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorLeftQuantity, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorLeftAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorLeftQuantity>,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorLeftQuantity, NumeratorLeftAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorRightUnitPerDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				NumeratorRightQuantity,
				NumeratorRightUnit,
				DenominatorRightQuantity,
				DenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

