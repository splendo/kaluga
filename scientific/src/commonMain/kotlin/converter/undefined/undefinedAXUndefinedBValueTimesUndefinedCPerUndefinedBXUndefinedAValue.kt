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

@JvmName("undefinedAXUndefinedBValueTimesUndefinedCPerUndefinedBXUndefinedAValue")
fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightNumeratorValue : UndefinedScientificValue<RightNumeratorQuantity, RightNumeratorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
	factory: (Decimal, RightNumeratorUnit) -> RightNumeratorValue
) = right.unit.numerator.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXUndefinedBValueTimesGenericUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

