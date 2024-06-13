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

@JvmName("metricAndImperialUndefinedCPerUndefinedBXUndefinedAValueTimesMetricAndImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedCPerUndefinedBXUndefinedAValueTimesMetricUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedCPerUndefinedBXUndefinedAValueTimesImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedCPerUndefinedBXUndefinedAValueTimesUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedCPerUndefinedBXUndefinedAValueTimesUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedCPerUndefinedBXUndefinedAValueTimesMetricAndUKImperialUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedCPerUndefinedBXUndefinedAValueTimesMetricAndUSCustomaryUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit,
	LeftDenominatorUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericUndefinedCPerUndefinedBXUndefinedAValueTimesGenericUndefinedAXUndefinedBValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
	LeftDenominatorLeftAndRightRightUnit : UndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
	LeftDenominatorRightAndRightLeftQuantity : UndefinedQuantityType,
	LeftDenominatorRightAndRightLeftUnit : UndefinedScientificUnit<LeftDenominatorRightAndRightLeftQuantity>,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit, LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>, LeftDenominatorUnit>,
	RightUnit : UndefinedMultipliedUnit<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorRightAndRightLeftUnit, LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorLeftAndRightRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftAndRightRightQuantity, LeftDenominatorRightAndRightLeftQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftDenominatorRightAndRightLeftQuantity, LeftDenominatorLeftAndRightRightQuantity>, RightUnit>,
) =
	right times this

