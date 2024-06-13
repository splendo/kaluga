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
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueDivUndefinedBPerUndefinedAValue")
fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorNumeratorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
	reciprocalTargetUnit: DenominatorNumeratorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueDivMetricAndImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueDivMetricUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueDivImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueDivUKImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueDivUSCustomaryUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueDivMetricAndUKImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueDivMetricAndUSCustomaryUndefinedBPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalAndDenominatorDenominatorQuantity, NumeratorReciprocalAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			DenominatorNumeratorQuantity,
							DenominatorNumeratorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

