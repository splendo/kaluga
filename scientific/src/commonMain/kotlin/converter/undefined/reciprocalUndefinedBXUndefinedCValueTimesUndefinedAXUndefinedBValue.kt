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

import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialReciprocalUndefinedBXUndefinedCValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedBXUndefinedCValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedBXUndefinedCValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedBXUndefinedCValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedBXUndefinedCValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedBXUndefinedCValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedBXUndefinedCValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

