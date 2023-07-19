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
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAValueDivReciprocalUndefinedBValue")
fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	TargetUnit : UndefinedMultipliedUnit<NumeratorQuantity, NumeratorUnit, DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorQuantity, DenominatorReciprocalQuantity>, TargetUnit>
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
	numeratorUnitXDenominatorReciprocalUnit: NumeratorUnit.(DenominatorReciprocalUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numeratorUnitXDenominatorReciprocalUnit(right.unit.inverse).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

