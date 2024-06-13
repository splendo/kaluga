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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("dimensionlessValueDivReciprocalUndefinedAValue")
fun <
	NumeratorUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorReciprocalValue : UndefinedScientificValue<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
	factory: (Decimal, DenominatorReciprocalUnit) -> DenominatorReciprocalValue
) = right.unit.inverse.byDividing(this, right, factory)

@JvmName("metricAndImperialDimensionlessValueDivMetricAndImperialReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
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
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDimensionlessValueDivMetricReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDimensionlessValueDivImperialReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDimensionlessValueDivUKImperialReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDimensionlessValueDivUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDimensionlessValueDivMetricAndUKImperialReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDimensionlessValueDivMetricAndUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericDimensionlessValueDivGenericReciprocalUndefinedAValue")
infix operator fun <
	NumeratorUnit : Dimensionless,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: DenominatorReciprocalUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

