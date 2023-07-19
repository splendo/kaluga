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
import kotlin.jvm.JvmName

@JvmName("undefinedAValueTimesReciprocalUndefinedAXUndefinedBValue")
fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetUnit : UndefinedReciprocalUnit<RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalRightQuantity>, TargetUnit>
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
	reciprocalTargetUnit: RightReciprocalRightUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.inverse.right.reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueTimesMetricAndImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueTimesMetricReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueTimesImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueTimesUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueTimesUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueTimesMetricAndUKImperialReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueTimesMetricAndUSCustomaryReciprocalUndefinedAXUndefinedBValue")
infix operator fun <
	LeftAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalLeftUnit,
	RightReciprocalRightQuantity : UndefinedQuantityType,
	RightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftAndRightReciprocalLeftQuantity>,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalRightUnit : UndefinedScientificUnit<RightReciprocalRightQuantity>,
	RightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftAndRightReciprocalLeftQuantity, LeftAndRightReciprocalLeftUnit, RightReciprocalRightQuantity, RightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftAndRightReciprocalLeftQuantity, RightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			RightReciprocalRightQuantity,
							RightReciprocalRightUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

