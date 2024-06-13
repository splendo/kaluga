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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAValueDivUndefinedAPerUndefinedBValue")
fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorDenominatorValue : UndefinedScientificValue<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
	factory: (Decimal, DenominatorDenominatorUnit) -> DenominatorDenominatorValue
) = right.unit.denominator.byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueDivMetricAndImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueDivMetricUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueDivImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueDivUKImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueDivUSCustomaryUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueDivMetricAndUKImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueDivMetricAndUSCustomaryUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAValueDivGenericUndefinedAPerUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

