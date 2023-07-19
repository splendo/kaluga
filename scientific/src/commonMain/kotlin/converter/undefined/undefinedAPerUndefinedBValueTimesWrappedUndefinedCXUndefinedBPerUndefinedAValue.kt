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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueTimesWrappedUndefinedCXUndefinedBPerUndefinedAValue")
fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit : ScientificUnit<RightNumeratorLeftQuantity>,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightNumeratorLeftValue : ScientificValue<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
	factory: (Decimal, RightNumeratorLeftUnit) -> RightNumeratorLeftValue
) = right.unit.numerator.left.wrapped.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueTimesMetricAndImperialWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueTimesMetricWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueTimesImperialWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueTimesUKImperialWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueTimesUSCustomaryWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueTimesMetricAndUKImperialWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueTimesMetricAndUSCustomaryWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit,
	LeftUnit,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit,
	WrappedRightNumeratorLeftUnit,
	RightNumeratorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) where
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightNumeratorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericUndefinedAPerUndefinedBValueTimesGenericWrappedUndefinedCXUndefinedBPerUndefinedAValue")
infix operator fun <
	LeftNumeratorAndRightDenominatorQuantity : UndefinedQuantityType,
	LeftNumeratorAndRightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorAndRightDenominatorQuantity>,
	LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
	LeftDenominatorAndRightNumeratorRightUnit : UndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightNumeratorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorLeftUnit : AbstractScientificUnit<RightNumeratorLeftQuantity>,
	WrappedRightNumeratorLeftUnit : WrappedUndefinedExtendedUnit<RightNumeratorLeftQuantity, RightNumeratorLeftUnit>,
	RightNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, WrappedRightNumeratorLeftUnit, LeftDenominatorAndRightNumeratorRightQuantity, LeftDenominatorAndRightNumeratorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, RightNumeratorUnit, LeftNumeratorAndRightDenominatorQuantity, LeftNumeratorAndRightDenominatorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorAndRightDenominatorQuantity, LeftDenominatorAndRightNumeratorRightQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<RightNumeratorLeftQuantity>, LeftDenominatorAndRightNumeratorRightQuantity>, LeftNumeratorAndRightDenominatorQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

