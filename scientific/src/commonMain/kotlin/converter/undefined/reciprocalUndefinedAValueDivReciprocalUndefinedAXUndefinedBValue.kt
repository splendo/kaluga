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
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueDivReciprocalUndefinedAXUndefinedBValue")
fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorReciprocalRightValue : UndefinedScientificValue<DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
	factory: (Decimal, DenominatorReciprocalRightUnit) -> DenominatorReciprocalRightValue
) = right.unit.inverse.right.byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueDivMetricAndImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueDivMetricReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueDivImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueDivUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueDivUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueDivMetricAndUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueDivMetricAndUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericReciprocalUndefinedAValueDivGenericReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit>,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

