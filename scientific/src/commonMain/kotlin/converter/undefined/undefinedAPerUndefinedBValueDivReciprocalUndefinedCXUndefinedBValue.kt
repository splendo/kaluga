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
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueDivReciprocalUndefinedCXUndefinedBValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	TargetUnit : UndefinedMultipliedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorReciprocalLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
	numeratorNumeratorUnitXDenominatorReciprocalLeftUnit: NumeratorNumeratorUnit.(DenominatorReciprocalLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.numeratorNumeratorUnitXDenominatorReciprocalLeftUnit(right.unit.inverse.left).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueDivMetricAndImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueDivMetricReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueDivImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueDivUKImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueDivUSCustomaryReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueDivMetricAndUKImperialReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryReciprocalUndefinedCXUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit,
	NumeratorUnit,
	DenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	DenominatorReciprocalLeftUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalRightQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalLeftUnit : UndefinedScientificUnit<DenominatorReciprocalLeftQuantity>,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<DenominatorReciprocalLeftQuantity, DenominatorReciprocalLeftUnit, NumeratorDenominatorAndDenominatorReciprocalRightQuantity, NumeratorDenominatorAndDenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<DenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalLeftUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalLeftQuantity,
				DenominatorReciprocalLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

