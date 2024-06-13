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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueDivWrappedUndefinedAXUndefinedBValue")
fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit : ScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	TargetUnit : UndefinedReciprocalUnit<DenominatorRightQuantity, DenominatorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorRightQuantity>, TargetUnit>
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
	reciprocalTargetUnit: DenominatorRightUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.right.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueDivMetricAndImperialWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueDivMetricWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueDivImperialWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueDivUKImperialWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueDivUSCustomaryWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueDivMetricAndUKImperialWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueDivMetricAndUSCustomaryWrappedUndefinedAXUndefinedBValue")
infix operator fun <
	NumeratorAndDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorLeftUnit,
	WrappedNumeratorAndDenominatorLeftUnit,
	DenominatorRightQuantity : UndefinedQuantityType,
	DenominatorRightUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, DenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorLeftQuantity>,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorLeftQuantity, NumeratorAndDenominatorLeftUnit>,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorRightUnit : UndefinedScientificUnit<DenominatorRightQuantity>,
	DenominatorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorLeftQuantity>, WrappedNumeratorAndDenominatorLeftUnit, DenominatorRightQuantity, DenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			DenominatorRightQuantity,
							DenominatorRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

