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

@JvmName("metricAndImperialReciprocalUndefinedCXUndefinedBValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedCXUndefinedBValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedCXUndefinedBValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedCXUndefinedBValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedCXUndefinedBValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedCXUndefinedBValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedCXUndefinedBValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightRightQuantity : UndefinedQuantityType,
	LeftReciprocalRightAndRightRightUnit,
	LeftReciprocalUnit,
	LeftUnit,
	RightLeftQuantity : UndefinedQuantityType,
	RightLeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<RightLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, RightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightRightUnit : UndefinedScientificUnit<LeftReciprocalRightAndRightRightQuantity>,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, LeftReciprocalRightAndRightRightQuantity>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightLeftUnit : UndefinedScientificUnit<RightLeftQuantity>,
	RightLeftUnit : MeasurementUsage.UsedInMetric,
	RightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<RightLeftQuantity, RightLeftUnit, LeftReciprocalRightAndRightRightQuantity, LeftReciprocalRightAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

