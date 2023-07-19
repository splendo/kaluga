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
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAXUndefinedBValueDivUndefinedCPerUndefinedBValue")
fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
	numeratorReciprocalLeftUnitXDenominatorNumeratorUnit: NumeratorReciprocalLeftUnit.(DenominatorNumeratorUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.left.numeratorReciprocalLeftUnitXDenominatorNumeratorUnit(right.unit.numerator).reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueDivMetricUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.Metric<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueDivImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.Imperial<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueDivUKImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueDivUSCustomaryUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedCPerUndefinedBValue")
infix operator fun <
	NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftUnit,
	NumeratorReciprocalRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftQuantity, NumeratorReciprocalLeftUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, NumeratorReciprocalRightAndDenominatorDenominatorQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalRightAndDenominatorDenominatorQuantity, NumeratorReciprocalRightAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalLeftUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorReciprocalLeftQuantity,
					NumeratorReciprocalLeftUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

