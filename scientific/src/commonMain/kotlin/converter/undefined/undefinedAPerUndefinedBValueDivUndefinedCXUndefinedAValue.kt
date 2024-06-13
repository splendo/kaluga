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

@JvmName("undefinedAPerUndefinedBValueDivUndefinedCXUndefinedAValue")
fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<NumeratorDenominatorQuantity, NumeratorDenominatorUnit, DenominatorLeftQuantity, DenominatorLeftUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
	numeratorDenominatorUnitXDenominatorLeftUnit: NumeratorDenominatorUnit.(DenominatorLeftUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.denominator.numeratorDenominatorUnitXDenominatorLeftUnit(right.unit.left).reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueDivMetricAndImperialUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueDivMetricUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.Metric<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueDivImperialUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.Imperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueDivUKImperialUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueDivUSCustomaryUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueDivMetricAndUKImperialUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryUndefinedCXUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorRightUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorLeftQuantity : UndefinedQuantityType,
	DenominatorLeftUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<DenominatorLeftQuantity, NumeratorNumeratorAndDenominatorRightQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorRightUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorLeftUnit : UndefinedScientificUnit<DenominatorLeftQuantity>,
	DenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	DenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedMultipliedUnit<DenominatorLeftQuantity, DenominatorLeftUnit, NumeratorNumeratorAndDenominatorRightQuantity, NumeratorNumeratorAndDenominatorRightUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorDenominatorUnitXDenominatorLeftUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<NumeratorDenominatorQuantity, DenominatorLeftQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					NumeratorDenominatorQuantity,
					NumeratorDenominatorUnit,
					DenominatorLeftQuantity,
					DenominatorLeftUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

