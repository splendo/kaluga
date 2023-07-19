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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("undefinedAValueDivUndefinedBXUndefinedAValue")
fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	TargetUnit : UndefinedReciprocalUnit<DenominatorLeftQuantity, DenominatorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
	reciprocalTargetUnit: DenominatorLeftUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.left.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueDivMetricAndImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueDivMetricUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueDivImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueDivUKImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueDivUSCustomaryUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueDivMetricAndUKImperialUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueDivMetricAndUSCustomaryUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorRightUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorAndDenominatorRightQuantity>,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorAndDenominatorRightQuantity, NumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			DenominatorLeftQuantity,
							DenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

