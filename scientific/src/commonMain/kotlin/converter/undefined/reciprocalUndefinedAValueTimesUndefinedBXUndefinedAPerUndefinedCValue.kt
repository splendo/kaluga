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
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueTimesUndefinedBXUndefinedAPerUndefinedCValue")
fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorLeftQuantity, RightDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
	rightNumeratorLeftUnitPerRightDenominatorUnit: RightNumeratorLeftUnit.(RightDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.left.rightNumeratorLeftUnitPerRightDenominatorUnit(right.unit.denominator).byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueTimesMetricAndImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueTimesMetricUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueTimesImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueTimesUKImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueTimesUSCustomaryUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueTimesMetricAndUKImperialUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueTimesMetricAndUSCustomaryUndefinedBXUndefinedAPerUndefinedCValue")
infix operator fun <
	LeftReciprocalAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : UndefinedQuantityType,
	RightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightDenominatorQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightNumeratorRightQuantity>,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : UndefinedScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit, LeftReciprocalAndRightNumeratorRightQuantity, LeftReciprocalAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<RightNumeratorLeftQuantity, LeftReciprocalAndRightNumeratorRightQuantity>, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorLeftUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorLeftQuantity,
				RightNumeratorLeftUnit,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

