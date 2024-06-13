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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialReciprocalUndefinedBValueTimesMetricAndImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedBValueTimesMetricUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedBValueTimesImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedBValueTimesUKImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedBValueTimesUSCustomaryUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedBValueTimesMetricAndUKImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedBValueTimesMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	LeftReciprocalQuantity : UndefinedQuantityType,
	LeftReciprocalUnit,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

