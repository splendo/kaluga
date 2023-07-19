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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialUndefinedBPerUndefinedAXUndefinedDValueTimesMetricAndImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBPerUndefinedAXUndefinedDValueTimesMetricUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBPerUndefinedAXUndefinedDValueTimesImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBPerUndefinedAXUndefinedDValueTimesUKImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBPerUndefinedAXUndefinedDValueTimesUSCustomaryUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBPerUndefinedAXUndefinedDValueTimesMetricAndUKImperialUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBPerUndefinedAXUndefinedDValueTimesMetricAndUSCustomaryUndefinedAPerUndefinedBValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorLeftAndRightNumeratorQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightNumeratorUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorLeftAndRightNumeratorQuantity, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorQuantity>,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<LeftDenominatorLeftAndRightNumeratorQuantity, LeftDenominatorLeftAndRightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

