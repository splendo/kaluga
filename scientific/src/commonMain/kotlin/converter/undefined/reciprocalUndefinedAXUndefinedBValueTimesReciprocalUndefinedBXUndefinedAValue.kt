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
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.One
import kotlin.jvm.JvmName

@JvmName("reciprocalUndefinedAXUndefinedBValueTimesReciprocalUndefinedBXUndefinedAValue")
fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
	getDimensionless: () -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = getDimensionless().byMultiplying(this, right, factory)

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericReciprocalUndefinedAXUndefinedBValueTimesGenericReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity>,
	LeftReciprocalRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit, LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>, LeftReciprocalUnit>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalRightAndRightReciprocalLeftUnit, LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightReciprocalRightQuantity, LeftReciprocalRightAndRightReciprocalLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightReciprocalLeftQuantity, LeftReciprocalLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

