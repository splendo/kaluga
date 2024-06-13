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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBPerUndefinedCValueDivUndefinedBXUndefinedAValue")
fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	TargetUnit : UndefinedReciprocalUnit<NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
	reciprocalTargetUnit: NumeratorDenominatorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.denominator.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBPerUndefinedCValueDivMetricUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBPerUndefinedCValueDivImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBPerUndefinedCValueDivUKImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivUSCustomaryUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUKImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBPerUndefinedCValueDivMetricAndUSCustomaryUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorLeftAndDenominatorRightUnit,
	NumeratorNumeratorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
	NumeratorNumeratorRightAndDenominatorLeftUnit,
	NumeratorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorLeftAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorLeftAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorLeftAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorRightAndDenominatorLeftUnit : UndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity>,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorRightAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorNumeratorUnit : UndefinedMultipliedUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit, NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorRightAndDenominatorLeftQuantity>, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<NumeratorNumeratorRightAndDenominatorLeftQuantity, NumeratorNumeratorRightAndDenominatorLeftUnit, NumeratorNumeratorLeftAndDenominatorRightQuantity, NumeratorNumeratorLeftAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

