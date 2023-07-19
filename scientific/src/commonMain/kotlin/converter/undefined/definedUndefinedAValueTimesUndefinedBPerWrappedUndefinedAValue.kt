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

@JvmName("definedUndefinedAValueTimesUndefinedBPerWrappedUndefinedAValue")
fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit : ScientificUnit<LeftAndRightDenominatorQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightNumeratorValue : UndefinedScientificValue<RightNumeratorQuantity, RightNumeratorUnit>
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
	factory: (Decimal, RightNumeratorUnit) -> RightNumeratorValue
) = right.unit.numerator.byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("genericDefinedUndefinedAValueTimesGenericUndefinedBPerWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorUnit : AbstractScientificUnit<LeftAndRightDenominatorQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	WrappedLeftAndRightDenominatorUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>, WrappedLeftAndRightDenominatorUnit>
	> ScientificValue<LeftAndRightDenominatorQuantity, LeftAndRightDenominatorUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorQuantity>>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultUndefinedScientificValue(value, unit)
	}

