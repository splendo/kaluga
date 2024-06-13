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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueTimesUndefinedBPerUndefinedDXUndefinedAValue")
fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightDenominatorLeftQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
	reciprocalTargetUnit: RightDenominatorLeftUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.left.reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueTimesMetricAndImperialUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueTimesMetricUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueTimesImperialUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueTimesUKImperialUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueTimesUSCustomaryUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueTimesMetricAndUKImperialUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueTimesMetricAndUSCustomaryUndefinedBPerUndefinedDXUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorRightUnit,
	LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorRightQuantity, LeftDenominatorAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorAndRightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorRightQuantity>,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit, LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, LeftNumeratorAndRightDenominatorRightQuantity, LeftNumeratorAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorAndRightNumeratorQuantity, LeftDenominatorAndRightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, LeftNumeratorAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			RightDenominatorLeftQuantity,
							RightDenominatorLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

