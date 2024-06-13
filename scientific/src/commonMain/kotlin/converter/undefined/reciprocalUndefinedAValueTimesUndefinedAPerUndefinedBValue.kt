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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueTimesUndefinedAPerUndefinedBValue")
fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<RightDenominatorQuantity, RightDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
	reciprocalTargetUnit: RightDenominatorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueTimesMetricAndImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

@JvmName("metricReciprocalUndefinedAValueTimesMetricUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

@JvmName("imperialReciprocalUndefinedAValueTimesImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

@JvmName("ukImperialReciprocalUndefinedAValueTimesUKImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

@JvmName("usCustomaryReciprocalUndefinedAValueTimesUSCustomaryUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

@JvmName("metricAndUKImperialReciprocalUndefinedAValueTimesMetricAndUKImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueTimesMetricAndUSCustomaryUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorUnit,
	LeftUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftReciprocalAndRightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorQuantity>,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftReciprocalAndRightNumeratorQuantity, LeftReciprocalAndRightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
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

