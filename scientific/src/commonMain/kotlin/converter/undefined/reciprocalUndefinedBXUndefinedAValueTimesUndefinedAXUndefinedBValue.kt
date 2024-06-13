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

@JvmName("metricAndImperialReciprocalUndefinedBXUndefinedAValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedBXUndefinedAValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedBXUndefinedAValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedBXUndefinedAValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedBXUndefinedAValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedBXUndefinedAValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedBXUndefinedAValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericReciprocalUndefinedBXUndefinedAValueTimesGenericUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightRightQuantity>,
	LeftReciprocalRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightLeftQuantity>,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit, LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>, LeftReciprocalUnit>,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalRightAndRightLeftUnit, LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalLeftAndRightRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightRightQuantity, LeftReciprocalRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalRightAndRightLeftQuantity, LeftReciprocalLeftAndRightRightQuantity>, RightUnit>,
) =
	right times this

