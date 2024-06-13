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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueDivReciprocalUndefinedBValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorNumeratorValue : UndefinedScientificValue<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
	factory: (Decimal, NumeratorNumeratorUnit) -> NumeratorNumeratorValue
) = unit.numerator.byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAPerUndefinedBValueDivGenericReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

