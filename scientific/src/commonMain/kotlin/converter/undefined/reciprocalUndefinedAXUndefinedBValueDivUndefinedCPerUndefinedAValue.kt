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

@JvmName("reciprocalUndefinedAXUndefinedBValueDivUndefinedCPerUndefinedAValue")
fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit, DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
	numeratorReciprocalRightUnitXDenominatorNumeratorUnit: NumeratorReciprocalRightUnit.(DenominatorNumeratorUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.right.numeratorReciprocalRightUnitXDenominatorNumeratorUnit(right.unit.numerator).reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueDivMetricAndImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueDivMetricUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.Metric<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueDivImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.Imperial<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueDivUKImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueDivUSCustomaryUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueDivMetricAndUKImperialUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueDivMetricAndUSCustomaryUndefinedCPerUndefinedAValue")
infix operator fun <
	NumeratorReciprocalLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit,
	NumeratorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalRightUnit,
	NumeratorReciprocalUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : UndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity>,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalLeftAndDenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalRightUnit : UndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorReciprocalUnit : UndefinedMultipliedUnit<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit, NumeratorReciprocalRightQuantity, NumeratorReciprocalRightUnit>,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalRightQuantity>, NumeratorReciprocalUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, NumeratorReciprocalLeftAndDenominatorDenominatorQuantity, NumeratorReciprocalLeftAndDenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorReciprocalRightUnitXDenominatorNumeratorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<NumeratorReciprocalRightQuantity, DenominatorNumeratorQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorReciprocalRightQuantity,
					NumeratorReciprocalRightUnit,
					DenominatorNumeratorQuantity,
					DenominatorNumeratorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

