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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAValueDivUndefinedAPerWrappedUndefinedBValue")
fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit : ScientificUnit<DenominatorDenominatorQuantity>,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorDenominatorValue : ScientificValue<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
	factory: (Decimal, DenominatorDenominatorUnit) -> DenominatorDenominatorValue
) = right.unit.denominator.wrapped.byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueDivMetricAndImperialUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueDivMetricUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueDivImperialUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueDivUKImperialUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueDivUSCustomaryUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueDivMetricAndUKImperialUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueDivMetricAndUSCustomaryUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit,
	WrappedDenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericUndefinedAValueDivGenericUndefinedAPerWrappedUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorAndDenominatorNumeratorQuantity>,
	DenominatorDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorDenominatorUnit : AbstractScientificUnit<DenominatorDenominatorQuantity>,
	WrappedDenominatorDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : UndefinedDividedUnit<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>, WrappedDenominatorDenominatorUnit>
	> UndefinedScientificValue<NumeratorAndDenominatorNumeratorQuantity, NumeratorAndDenominatorNumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorAndDenominatorNumeratorQuantity, UndefinedQuantityType.Extended<DenominatorDenominatorQuantity>>, DenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: DenominatorDenominatorUnit
		->
		DefaultScientificValue(value, unit)
	}

