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
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAXUndefinedBValueTimesUndefinedAPerUndefinedCValue")
fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightQuantity, LeftReciprocalRightUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
	leftReciprocalRightUnitXRightDenominatorUnit: LeftReciprocalRightUnit.(RightDenominatorUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.right.leftReciprocalRightUnitXRightDenominatorUnit(right.unit.denominator).reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.Metric<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.Imperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightNumeratorUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalLeftAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorQuantity>,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalLeftAndRightNumeratorQuantity, LeftReciprocalLeftAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalRightUnitXRightDenominatorUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<LeftReciprocalRightQuantity, RightDenominatorQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftReciprocalRightQuantity,
					LeftReciprocalRightUnit,
					RightDenominatorQuantity,
					RightDenominatorUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

