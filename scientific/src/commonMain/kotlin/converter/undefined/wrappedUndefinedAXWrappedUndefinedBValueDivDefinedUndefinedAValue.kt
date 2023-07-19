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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("wrappedUndefinedAXWrappedUndefinedBValueDivDefinedUndefinedAValue")
fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit : ScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit : ScientificUnit<NumeratorRightQuantity>,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorRightValue : ScientificValue<NumeratorRightQuantity, NumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	factory: (Decimal, NumeratorRightUnit) -> NumeratorRightValue
) = unit.right.wrapped.byDividing(this, right, factory)

@JvmName("metricAndImperialWrappedUndefinedAXWrappedUndefinedBValueDivMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricWrappedUndefinedAXWrappedUndefinedBValueDivMetricDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialWrappedUndefinedAXWrappedUndefinedBValueDivImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialWrappedUndefinedAXWrappedUndefinedBValueDivUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryWrappedUndefinedAXWrappedUndefinedBValueDivUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialWrappedUndefinedAXWrappedUndefinedBValueDivMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryWrappedUndefinedAXWrappedUndefinedBValueDivMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericWrappedUndefinedAXWrappedUndefinedBValueDivGenericDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

