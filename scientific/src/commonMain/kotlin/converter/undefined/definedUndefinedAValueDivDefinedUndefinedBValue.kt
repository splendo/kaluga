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
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueDivDefinedUndefinedBValue")
fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit : ScientificUnit<NumeratorQuantity>,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit : ScientificUnit<DenominatorQuantity>,
	WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorQuantity, NumeratorUnit>,
	WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorQuantity, DenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorQuantity>, WrappedNumeratorUnit, UndefinedQuantityType.Extended<DenominatorQuantity>, WrappedDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorQuantity>, UndefinedQuantityType.Extended<DenominatorQuantity>>, TargetUnit>
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
	numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
	denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
	wrappedNumeratorUnitPerWrappedDenominatorUnit: WrappedNumeratorUnit.(WrappedDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numeratorAsUndefined().wrappedNumeratorUnitPerWrappedDenominatorUnit(right.unit.denominatorAsUndefined()).byDividing(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueDivMetricAndImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueDivMetricDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.Metric<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.Metric<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueDivImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueDivUKImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueDivUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueDivMetricAndUKImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueDivMetricAndUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		denominatorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitPerWrappedDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<NumeratorQuantity, NumeratorUnit>,
				UndefinedQuantityType.Extended<DenominatorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<DenominatorQuantity, DenominatorUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

