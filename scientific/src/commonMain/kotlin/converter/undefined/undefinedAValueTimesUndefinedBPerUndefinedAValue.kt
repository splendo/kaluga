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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAValueTimesUndefinedBPerUndefinedAValue")
fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightNumeratorValue : UndefinedScientificValue<RightNumeratorQuantity, RightNumeratorUnit>
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
	factory: (Decimal, RightNumeratorUnit) -> RightNumeratorValue
) = right.unit.numerator.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueTimesMetricAndImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueTimesMetricUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueTimesImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueTimesUKImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueTimesUSCustomaryUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueTimesMetricAndUKImperialUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueTimesMetricAndUSCustomaryUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericUndefinedAValueTimesGenericUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, LeftAndRightDenominatorQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

