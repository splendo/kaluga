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

@JvmName("undefinedAXUndefinedBPerUndefinedCValueDivUndefinedAValue")
fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorRightQuantity, NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
	numeratorNumeratorRightUnitPerNumeratorDenominatorUnit: NumeratorNumeratorRightUnit.(NumeratorDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.right.numeratorNumeratorRightUnitPerNumeratorDenominatorUnit(unit.denominator).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueDivMetricUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueDivImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueDivUKImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUKImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorUnit,
	NumeratorNumeratorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorQuantity>,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightUnit : UndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorLeftAndDenominatorUnit, NumeratorNumeratorRightQuantity, NumeratorNumeratorRightUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorQuantity, NumeratorNumeratorRightQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorRightUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				NumeratorNumeratorRightQuantity,
				NumeratorNumeratorRightUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

