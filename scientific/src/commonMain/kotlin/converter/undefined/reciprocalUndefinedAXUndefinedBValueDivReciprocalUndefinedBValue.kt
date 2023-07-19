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

@JvmName("reciprocalUndefinedAXUndefinedBValueDivReciprocalUndefinedBValue")
fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	TargetUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
	reciprocalTargetUnit: NumeratorReciprocalLeftUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.left.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorReciprocalQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightAndDenominatorReciprocalQuantity, NumeratorReciprocalRightAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			NumeratorReciprocalLeftQuantity,
							NumeratorReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

