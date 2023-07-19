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

@JvmName("reciprocalUndefinedAValueDivReciprocalUndefinedBXUndefinedAValue")
fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorReciprocalLeftValue : UndefinedScientificValue<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
	factory: (Decimal, DenominatorReciprocalLeftUnit) -> DenominatorReciprocalLeftValue
) = right.unit.inverse.left.byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueDivMetricAndImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueDivMetricReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueDivImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueDivUKImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueDivUSCustomaryReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueDivMetricAndUKImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueDivMetricAndUSCustomaryReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericReciprocalUndefinedAValueDivGenericReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorReciprocalAndDenominatorReciprocalRightQuantity, NumeratorReciprocalAndDenominatorReciprocalRightUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorReciprocalAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalLeftUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

