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

@JvmName("metricAndImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedAXUndefinedBValueTimesMetricUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedAXUndefinedBValueTimesImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedAXUndefinedBValueTimesUKImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
	LeftReciprocalLeftAndRightUnit,
	LeftReciprocalRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit>,
) where
	LeftReciprocalLeftAndRightUnit : UndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightUnit : UndefinedScientificUnit<LeftReciprocalRightQuantity>,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftAndRightQuantity, LeftReciprocalLeftAndRightUnit, LeftReciprocalRightQuantity, LeftReciprocalRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftAndRightQuantity, LeftReciprocalRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

