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
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAValueTimesWrappedUndefinedBPerUndefinedAValue")
fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit : ScientificUnit<RightNumeratorQuantity>,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightNumeratorValue : ScientificValue<RightNumeratorQuantity, RightNumeratorUnit>
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
	factory: (Decimal, RightNumeratorUnit) -> RightNumeratorValue
) = right.unit.numerator.wrapped.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueTimesMetricAndImperialWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueTimesMetricWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueTimesImperialWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueTimesUKImperialWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueTimesUSCustomaryWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueTimesMetricAndUKImperialWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueTimesMetricAndUSCustomaryWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericUndefinedAValueTimesGenericWrappedUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftAndRightDenominatorUnit : UndefinedScientificUnit<LeftAndRightDenominatorQuantity>,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>
	> UndefinedScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, LeftAndRightDenominatorQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

