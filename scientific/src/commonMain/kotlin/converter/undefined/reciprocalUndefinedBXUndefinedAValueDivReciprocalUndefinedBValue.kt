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

@JvmName("reciprocalUndefinedBXUndefinedAValueDivReciprocalUndefinedBValue")
fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	TargetUnit : UndefinedReciprocalUnit<NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
	reciprocalTargetUnit: NumeratorReciprocalRightUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.right.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedBXUndefinedAValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedBXUndefinedAValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedBXUndefinedAValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedBXUndefinedAValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedBXUndefinedAValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedBXUndefinedAValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedBXUndefinedAValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity>,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorReciprocalLeftAndDenominatorReciprocalQuantity, NumeratorReciprocalLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			NumeratorReciprocalRightQuantity,
							NumeratorReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

