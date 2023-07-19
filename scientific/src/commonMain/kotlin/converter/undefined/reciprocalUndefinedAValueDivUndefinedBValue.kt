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
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueDivUndefinedBValue")
fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit, DenominatorQuantity, DenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
	numeratorReciprocalUnitXDenominatorUnit: NumeratorReciprocalUnit.(DenominatorUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.numeratorReciprocalUnitXDenominatorUnit(right.unit).reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueDivMetricAndImperialUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueDivMetricUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.Metric<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueDivImperialUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.Imperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueDivUKImperialUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueDivUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueDivMetricAndUKImperialUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueDivMetricAndUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalUnitXDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, DenominatorQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					DenominatorQuantity,
					DenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

