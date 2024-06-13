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

@JvmName("metricAndImperialReciprocalUndefinedAValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedAValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedAValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedAValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedAValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedAValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit,
	LeftUnit,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) where
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightRightUnit : MeasurementUsage.UsedInMetric,
	RightRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericReciprocalUndefinedAValueTimesGenericUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalAndRightLeftQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightLeftUnit : UndefinedScientificUnit<LeftReciprocalAndRightLeftQuantity>,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit>,
	RightRightQuantity : UndefinedQuantityType,
	RightRightUnit : UndefinedScientificUnit<RightRightQuantity>,
	RightUnit : UndefinedMultipliedUnit<LeftReciprocalAndRightLeftQuantity, LeftReciprocalAndRightLeftUnit, RightRightQuantity, RightRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftReciprocalAndRightLeftQuantity, RightRightQuantity>, RightUnit>,
) =
	right times this

