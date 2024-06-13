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

@JvmName("definedUndefinedAValueDivUndefinedBXWrappedUndefinedAPerUndefinedCValue")
fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit : ScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit, DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorDenominatorQuantity, DenominatorNumeratorLeftQuantity>, TargetUnit>
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
	denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit: DenominatorDenominatorUnit.(DenominatorNumeratorLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit(right.unit.numerator.left).byDividing(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueDivMetricAndImperialUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueDivMetricUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueDivImperialUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueDivUKImperialUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueDivUSCustomaryUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueDivMetricAndUKImperialUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueDivMetricAndUSCustomaryUndefinedBXWrappedUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorAndDenominatorNumeratorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
	DenominatorNumeratorLeftUnit,
	WrappedNumeratorAndDenominatorNumeratorRightUnit,
	DenominatorNumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> ScientificValue<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorAndDenominatorNumeratorRightUnit : AbstractScientificUnit<NumeratorAndDenominatorNumeratorRightQuantity>,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorLeftUnit : UndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorNumeratorRightQuantity, NumeratorAndDenominatorNumeratorRightUnit>,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedMultipliedUnit<DenominatorNumeratorLeftQuantity, DenominatorNumeratorLeftUnit, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>, WrappedNumeratorAndDenominatorNumeratorRightUnit>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<DenominatorNumeratorLeftQuantity, UndefinedQuantityType.Extended<NumeratorAndDenominatorNumeratorRightQuantity>>, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerDenominatorNumeratorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				DenominatorNumeratorLeftQuantity,
				DenominatorNumeratorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

