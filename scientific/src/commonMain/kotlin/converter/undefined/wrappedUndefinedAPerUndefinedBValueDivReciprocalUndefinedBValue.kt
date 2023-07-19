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
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("wrappedUndefinedAPerUndefinedBValueDivReciprocalUndefinedBValue")
fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit : ScientificUnit<NumeratorNumeratorQuantity>,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorNumeratorValue : ScientificValue<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
	factory: (Decimal, NumeratorNumeratorUnit) -> NumeratorNumeratorValue
) = unit.numerator.wrapped.byDividing(this, right, factory)

@JvmName("metricAndImperialWrappedUndefinedAPerUndefinedBValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
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
		DefaultScientificValue(value, unit)
	}

@JvmName("metricWrappedUndefinedAPerUndefinedBValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialWrappedUndefinedAPerUndefinedBValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialWrappedUndefinedAPerUndefinedBValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryWrappedUndefinedAPerUndefinedBValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialWrappedUndefinedAPerUndefinedBValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryWrappedUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit,
	WrappedNumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericWrappedUndefinedAPerUndefinedBValueDivGenericReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorUnit : AbstractScientificUnit<NumeratorNumeratorQuantity>,
	WrappedNumeratorNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit>,
	NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, WrappedNumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity, NumeratorDenominatorAndDenominatorReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorQuantity>, NumeratorDenominatorAndDenominatorReciprocalQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

