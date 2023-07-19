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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueDivWrappedUndefinedAXUndefinedBPerUndefinedCValue")
fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit : ScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorDenominatorQuantity, DenominatorNumeratorRightQuantity>, TargetUnit>
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
	denominatorDenominatorUnitPerDenominatorNumeratorRightUnit: DenominatorDenominatorUnit.(DenominatorNumeratorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.denominatorDenominatorUnitPerDenominatorNumeratorRightUnit(right.unit.numerator.right).byDividing(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueDivMetricAndImperialWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueDivMetricWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueDivImperialWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueDivUKImperialWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueDivUSCustomaryWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueDivMetricAndUKImperialWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueDivMetricAndUSCustomaryWrappedUndefinedAXUndefinedBPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit,
	DenominatorNumeratorRightQuantity : UndefinedQuantityType,
	DenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorLeftUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorLeftQuantity>,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorLeftQuantity, NumeratorAndDenominatorNumeratorLeftUnit>,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorRightUnit : UndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, WrappedNumeratorAndDenominatorNumeratorLeftUnit, DenominatorNumeratorRightQuantity, DenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorLeftQuantity>, DenominatorNumeratorRightQuantity>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorRightQuantity,
				DenominatorNumeratorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

