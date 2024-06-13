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

@JvmName("undefinedAXUndefinedBValueDivUndefinedBValue")
fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorLeftValue : UndefinedScientificValue<NumeratorLeftQuantity, NumeratorLeftUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	factory: (Decimal, NumeratorLeftUnit) -> NumeratorLeftValue
) = unit.left.byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueDivMetricUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueDivImperialUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueDivUKImperialUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueDivUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) where
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXUndefinedBValueDivGenericUndefinedBValue")
infix operator fun <
	NumeratorLeftQuantity : UndefinedQuantityType,
	NumeratorLeftUnit : UndefinedScientificUnit<NumeratorLeftQuantity>,
	NumeratorRightAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorRightAndDenominatorUnit : UndefinedScientificUnit<NumeratorRightAndDenominatorQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftQuantity, NumeratorLeftUnit, NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftQuantity, NumeratorRightAndDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorRightAndDenominatorQuantity, NumeratorRightAndDenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

