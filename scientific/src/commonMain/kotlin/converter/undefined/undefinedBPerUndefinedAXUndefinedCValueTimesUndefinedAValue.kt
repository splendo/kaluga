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

@JvmName("metricAndImperialUndefinedBPerUndefinedAXUndefinedCValueTimesMetricAndImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBPerUndefinedAXUndefinedCValueTimesMetricUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBPerUndefinedAXUndefinedCValueTimesImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBPerUndefinedAXUndefinedCValueTimesUKImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBPerUndefinedAXUndefinedCValueTimesUSCustomaryUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBPerUndefinedAXUndefinedCValueTimesMetricAndUKImperialUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBPerUndefinedAXUndefinedCValueTimesMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightQuantity, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

