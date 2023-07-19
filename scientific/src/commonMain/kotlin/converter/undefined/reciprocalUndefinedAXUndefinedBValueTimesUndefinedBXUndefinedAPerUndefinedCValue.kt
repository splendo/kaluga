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

@JvmName("reciprocalUndefinedAXUndefinedBValueTimesUndefinedBXUndefinedAPerUndefinedCValue")
fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<RightDenominatorQuantity, RightDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
	reciprocalTargetUnit: RightDenominatorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorRightUnit,
	LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightNumeratorLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit, LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalRightAndRightNumeratorLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalRightAndRightNumeratorLeftUnit, LeftReciprocalLeftAndRightNumeratorRightQuantity, LeftReciprocalLeftAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightNumeratorLeftQuantity, LeftReciprocalLeftAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			RightDenominatorQuantity,
							RightDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

