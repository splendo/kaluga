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
import com.splendo.kaluga.scientific.unit.reciprocal
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueDivDefinedUndefinedBValue")
fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit : ScientificUnit<DenominatorQuantity>,
	WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<DenominatorQuantity, DenominatorUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit, UndefinedQuantityType.Extended<DenominatorQuantity>, WrappedDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
	denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
	numeratorReciprocalUnitXWrappedDenominatorUnit: NumeratorReciprocalUnit.(WrappedDenominatorUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.numeratorReciprocalUnitXWrappedDenominatorUnit(right.unit.denominatorAsUndefined()).reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueDivMetricAndImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndImperial<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueDivMetricDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.Metric<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.Metric<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueDivImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.Imperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.Imperial<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueDivUKImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.UKImperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.UKImperial<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueDivUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.USCustomary<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.USCustomary<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueDivMetricAndUKImperialDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndUKImperial<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueDivMetricAndUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	NumeratorReciprocalQuantity : UndefinedQuantityType,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalQuantity>, NumeratorUnit>.div(
	right: ScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : AbstractScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorAsUndefined = { asUndefined() },
		numeratorReciprocalUnitXWrappedDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalQuantity, UndefinedQuantityType.Extended<DenominatorQuantity>>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorReciprocalQuantity,
					NumeratorReciprocalUnit,
					UndefinedQuantityType.Extended<DenominatorQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndUSCustomary<DenominatorQuantity, DenominatorUnit>
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

