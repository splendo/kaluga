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

@JvmName("metricAndImperialReciprocalUndefinedAValueTimesMetricAndImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedAValueTimesMetricUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedAValueTimesImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedAValueTimesUKImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedAValueTimesUSCustomaryUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedAValueTimesMetricAndUKImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedAValueTimesMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericReciprocalUndefinedAValueTimesGenericUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalAndRightUnit : UndefinedScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftUnit : UndefinedReciprocalUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftReciprocalAndRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) =
	right times this

