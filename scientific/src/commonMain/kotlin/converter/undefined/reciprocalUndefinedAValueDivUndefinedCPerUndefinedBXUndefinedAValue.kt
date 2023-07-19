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
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueDivUndefinedCPerUndefinedBXUndefinedAValue")
fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, DenominatorNumeratorQuantity, DenominatorNumeratorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorDenominatorLeftQuantity, DenominatorNumeratorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
	denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit: DenominatorDenominatorLeftUnit.(DenominatorNumeratorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.left.denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit(right.unit.numerator).byDividing(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueDivMetricAndImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueDivMetricUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueDivImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueDivUKImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueDivUSCustomaryUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueDivMetricAndUKImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueDivMetricAndUSCustomaryUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	NumeratorReciprocalAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit,
	NumeratorUnit,
	DenominatorNumeratorQuantity : UndefinedQuantityType,
	DenominatorNumeratorUnit,
	DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorDenominatorLeftUnit,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorNumeratorQuantity, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>>, DenominatorUnit>,
) where
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : UndefinedScientificUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity>,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorReciprocalAndDenominatorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorLeftUnit : UndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedMultipliedUnit<DenominatorDenominatorLeftQuantity, DenominatorDenominatorLeftUnit, NumeratorReciprocalAndDenominatorDenominatorRightQuantity, NumeratorReciprocalAndDenominatorDenominatorRightUnit>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, UndefinedQuantityType.Multiplying<DenominatorDenominatorLeftQuantity, NumeratorReciprocalAndDenominatorDenominatorRightQuantity>, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorLeftUnitPerDenominatorNumeratorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				DenominatorDenominatorLeftQuantity,
				DenominatorDenominatorLeftUnit,
				DenominatorNumeratorQuantity,
				DenominatorNumeratorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

