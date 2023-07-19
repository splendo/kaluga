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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAXUndefinedBValueDivUndefinedAValue")
fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorRightValue : UndefinedScientificValue<NumeratorRightQuantity, NumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	factory: (Decimal, NumeratorRightUnit) -> NumeratorRightValue
) = unit.right.byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueDivMetricUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueDivImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueDivUKImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueDivUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXUndefinedBValueDivGenericUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

