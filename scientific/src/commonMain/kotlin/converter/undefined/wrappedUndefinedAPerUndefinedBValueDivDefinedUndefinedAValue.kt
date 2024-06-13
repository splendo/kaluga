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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("wrappedUndefinedAPerUndefinedBValueDivDefinedUndefinedAValue")
fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit : ScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	reciprocalTargetUnit: NumeratorDenominatorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.denominator.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialWrappedUndefinedAPerUndefinedBValueDivMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricWrappedUndefinedAPerUndefinedBValueDivMetricDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialWrappedUndefinedAPerUndefinedBValueDivImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialWrappedUndefinedAPerUndefinedBValueDivUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryWrappedUndefinedAPerUndefinedBValueDivUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialWrappedUndefinedAPerUndefinedBValueDivMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryWrappedUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorNumeratorAndDenominatorUnit,
	WrappedNumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: ScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorNumeratorAndDenominatorQuantity>, WrappedNumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

