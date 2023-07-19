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
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("wrappedUndefinedAXUndefinedBValueDivDefinedUndefinedAValue")
fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit : ScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorRightValue : UndefinedScientificValue<NumeratorRightQuantity, NumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	factory: (Decimal, NumeratorRightUnit) -> NumeratorRightValue
) = unit.right.byDividing(this, right, factory)

@JvmName("metricAndImperialWrappedUndefinedAXUndefinedBValueDivMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
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
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricWrappedUndefinedAXUndefinedBValueDivMetricDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialWrappedUndefinedAXUndefinedBValueDivImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialWrappedUndefinedAXUndefinedBValueDivUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryWrappedUndefinedAXUndefinedBValueDivUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialWrappedUndefinedAXUndefinedBValueDivMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryWrappedUndefinedAXUndefinedBValueDivMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit,
	WrappedNumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericWrappedUndefinedAXUndefinedBValueDivGenericDefinedUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorLeftAndDenominatorUnit : AbstractScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	WrappedNumeratorLeftAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	NumeratorRightQuantity : UndefinedQuantityType,
	NumeratorRightUnit : UndefinedScientificUnit<NumeratorRightQuantity>,
	NumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, WrappedNumeratorLeftAndDenominatorUnit, NumeratorRightQuantity, NumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorLeftAndDenominatorQuantity>, NumeratorRightQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

