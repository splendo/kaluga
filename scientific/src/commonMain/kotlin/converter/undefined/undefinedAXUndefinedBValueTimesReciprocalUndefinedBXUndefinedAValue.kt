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

@JvmName("undefinedAXUndefinedBValueTimesReciprocalUndefinedBXUndefinedAValue")
fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
	TargetUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
	getDimensionless: () -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = getDimensionless().byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit,
	LeftUnit,
	RightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>,
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

@JvmName("genericUndefinedAXUndefinedBValueTimesGenericReciprocalUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalRightUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalRightQuantity>,
	LeftRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalLeftUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalLeftQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit, LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit>,
	RightReciprocalUnit : UndefinedMultipliedUnit<LeftRightAndRightReciprocalLeftQuantity, LeftRightAndRightReciprocalLeftUnit, LeftLeftAndRightReciprocalRightQuantity, LeftLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>, RightReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalRightQuantity, LeftRightAndRightReciprocalLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftRightAndRightReciprocalLeftQuantity, LeftLeftAndRightReciprocalRightQuantity>>, RightUnit>,
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

