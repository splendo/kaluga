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

@JvmName("undefinedAPerUndefinedBXUndefinedCValueDivReciprocalUndefinedBValue")
fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	TargetUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, NumeratorDenominatorRightQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
	numeratorNumeratorUnitPerNumeratorDenominatorRightUnit: NumeratorNumeratorUnit.(NumeratorDenominatorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.numeratorNumeratorUnitPerNumeratorDenominatorRightUnit(unit.denominator.right).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBXUndefinedCValueDivMetricReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBXUndefinedCValueDivImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBXUndefinedCValueDivUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBXUndefinedCValueDivMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	NumeratorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorUnit,
	NumeratorDenominatorLeftAndDenominatorReciprocalQuantity : UndefinedQuantityType,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit,
	NumeratorDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorDenominatorRightUnit,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorQuantity, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : UndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity>,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorLeftAndDenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorRightUnit : UndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedMultipliedUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit, NumeratorDenominatorRightQuantity, NumeratorDenominatorRightUnit>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, UndefinedQuantityType.Multiplying<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorRightQuantity>, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedReciprocalUnit<NumeratorDenominatorLeftAndDenominatorReciprocalQuantity, NumeratorDenominatorLeftAndDenominatorReciprocalUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorNumeratorUnitPerNumeratorDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				NumeratorNumeratorQuantity,
				NumeratorNumeratorUnit,
				NumeratorDenominatorRightQuantity,
				NumeratorDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

