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
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueDivReciprocalUndefinedBValue")
fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit : ScientificUnit<NumeratorQuantity>,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorQuantity, NumeratorUnit>,
	TargetUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<NumeratorQuantity>, WrappedNumeratorUnit, DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<NumeratorQuantity>, DenominatorReciprocalQuantity>, TargetUnit>
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
	numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
	wrappedNumeratorUnitXDenominatorReciprocalUnit: WrappedNumeratorUnit.(DenominatorReciprocalUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numeratorAsUndefined().wrappedNumeratorUnitXDenominatorReciprocalUnit(right.unit.inverse).byDividing(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
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
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.Metric<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorUnit,
	DenominatorReciprocalQuantity : UndefinedQuantityType,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> ScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorUnit : AbstractScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorAsUndefined = { asUndefined() },
		wrappedNumeratorUnitXDenominatorReciprocalUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Extended<NumeratorQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<NumeratorQuantity, NumeratorUnit>,
				DenominatorReciprocalQuantity,
				DenominatorReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

