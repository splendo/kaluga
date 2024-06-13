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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAXWrappedUndefinedBValueDivUndefinedAValue")
fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit : ScientificUnit<NumeratorRightQuantity>,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorRightValue : ScientificValue<NumeratorRightQuantity, NumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
	factory: (Decimal, NumeratorRightUnit) -> NumeratorRightValue
) = unit.right.wrapped.byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAXWrappedUndefinedBValueDivMetricAndImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXWrappedUndefinedBValueDivMetricUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXWrappedUndefinedBValueDivImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXWrappedUndefinedBValueDivUKImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXWrappedUndefinedBValueDivUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXWrappedUndefinedBValueDivMetricAndUKImperialUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXWrappedUndefinedBValueDivMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit,
	WrappedNumeratorRightUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) where
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorLeftAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	NumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXWrappedUndefinedBValueDivGenericUndefinedAValue")
infix operator fun <
	NumeratorLeftAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorLeftAndDenominatorUnit : UndefinedScientificUnit<NumeratorLeftAndDenominatorQuantity>,
	NumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorRightUnit : AbstractScientificUnit<NumeratorRightQuantity>,
	WrappedNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorRightQuantity, NumeratorRightUnit>,
	NumeratorUnit : UndefinedMultipliedUnit<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit, UndefinedQuantityType.Extended<NumeratorRightQuantity>, WrappedNumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorLeftAndDenominatorQuantity, UndefinedQuantityType.Extended<NumeratorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorLeftAndDenominatorQuantity, NumeratorLeftAndDenominatorUnit>,
) =
	div(right) {
		value: Decimal,
		unit: NumeratorRightUnit
		->
		DefaultScientificValue(value, unit)
	}

