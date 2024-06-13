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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAValueTimesReciprocalUndefinedBValue")
fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	TargetReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalQuantity, LeftReciprocalUnit, RightReciprocalQuantity, RightReciprocalUnit>,
	TargetUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>, TargetReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
	leftReciprocalUnitXRightReciprocalUnit: LeftReciprocalUnit.(RightReciprocalUnit) -> TargetReciprocalUnit,
	reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.inverse.leftReciprocalUnitXRightReciprocalUnit(right.unit.inverse).reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAValueTimesMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.MetricAndImperial<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAValueTimesMetricReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.Metric<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAValueTimesImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.Imperial<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAValueTimesUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.UKImperial<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAValueTimesUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.USCustomary<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAValueTimesMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueTimesMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftReciprocalUnitXRightReciprocalUnit = { x(it) },
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			UndefinedQuantityType.Multiplying<LeftReciprocalQuantity, RightReciprocalQuantity>,
							UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftReciprocalQuantity,
					LeftReciprocalUnit,
					RightReciprocalQuantity,
					RightReciprocalUnit
				>>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

