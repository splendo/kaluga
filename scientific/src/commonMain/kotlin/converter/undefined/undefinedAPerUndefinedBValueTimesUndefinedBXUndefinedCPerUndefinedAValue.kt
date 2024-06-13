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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultUndefinedScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueTimesUndefinedBXUndefinedCPerUndefinedAValue")
fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightNumeratorRightValue : UndefinedScientificValue<RightNumeratorRightQuantity, RightNumeratorRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
	factory: (Decimal, RightNumeratorRightUnit) -> RightNumeratorRightValue
) = right.unit.numerator.right.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueTimesMetricAndImperialUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueTimesMetricUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueTimesImperialUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueTimesUKImperialUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueTimesUSCustomaryUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueTimesMetricAndUKImperialUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueTimesMetricAndUSCustomaryUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit,
	LeftUnit,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAPerUndefinedBValueTimesGenericUndefinedBXUndefinedCPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorLeftUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit>,
	RightNumeratorRightQuantity : UndefinedQuantityType,
	RightNumeratorRightUnit : UndefinedScientificUnit<RightNumeratorRightQuantity>,
	RightNumeratorUnit : UndefinedMultipliedUnit<LeftDenominatorAndRightNumeratorLeftQuantity, LeftDenominatorAndRightNumeratorLeftUnit, RightNumeratorRightQuantity, RightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftDenominatorAndRightNumeratorLeftQuantity, RightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorRightUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

