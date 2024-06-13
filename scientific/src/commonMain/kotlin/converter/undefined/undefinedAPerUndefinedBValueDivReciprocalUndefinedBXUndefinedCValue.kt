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

@JvmName("undefinedAPerUndefinedBValueDivReciprocalUndefinedBXUndefinedCValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	TargetUnit : UndefinedMultipliedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<NumeratorNumeratorQuantity, DenominatorReciprocalRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
	numeratorNumeratorUnitXDenominatorReciprocalRightUnit: NumeratorNumeratorUnit.(DenominatorReciprocalRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.numeratorNumeratorUnitXDenominatorReciprocalRightUnit(right.unit.inverse.right).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueDivMetricAndImperialReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueDivMetricReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueDivImperialReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueDivUKImperialReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueDivUSCustomaryReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueDivMetricAndUKImperialReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryReciprocalUndefinedBXUndefinedCValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit,
	NumeratorUnit,
	DenominatorReciprocalRightQuantity : UndefinedQuantityType,
	DenominatorReciprocalRightUnit,
	DenominatorReciprocalUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : UndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity>,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorAndDenominatorReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalRightUnit : UndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, NumeratorDenominatorAndDenominatorReciprocalLeftUnit, DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorAndDenominatorReciprocalLeftQuantity, DenominatorReciprocalRightQuantity>, DenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitXDenominatorReciprocalRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				DenominatorReciprocalRightQuantity,
				DenominatorReciprocalRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

