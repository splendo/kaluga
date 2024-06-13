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

@JvmName("undefinedAXUndefinedBValueTimesWrappedUndefinedCPerUndefinedBXUndefinedAValue")
fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit : ScientificUnit<RightNumeratorQuantity>,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightNumeratorValue : ScientificValue<RightNumeratorQuantity, RightNumeratorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
	factory: (Decimal, RightNumeratorUnit) -> RightNumeratorValue
) = right.unit.numerator.wrapped.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXUndefinedBValueTimesMetricAndImperialWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
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
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXUndefinedBValueTimesMetricWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXUndefinedBValueTimesImperialWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
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
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXUndefinedBValueTimesUKImperialWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXUndefinedBValueTimesUSCustomaryWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftRightAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXUndefinedBValueTimesMetricAndUKImperialWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
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
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit,
	LeftUnit,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit,
	WrappedRightNumeratorUnit,
	RightDenominatorUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
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
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedRightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXUndefinedBValueTimesGenericWrappedUndefinedCPerUndefinedBXUndefinedAValue")
infix operator fun <
	LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
	LeftLeftAndRightDenominatorRightUnit : UndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
	LeftRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
	LeftRightAndRightDenominatorLeftUnit : UndefinedScientificUnit<LeftRightAndRightDenominatorLeftQuantity>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit, LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit>,
	RightNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightNumeratorUnit : AbstractScientificUnit<RightNumeratorQuantity>,
	WrappedRightNumeratorUnit : WrappedUndefinedExtendedUnit<RightNumeratorQuantity, RightNumeratorUnit>,
	RightDenominatorUnit : UndefinedMultipliedUnit<LeftRightAndRightDenominatorLeftQuantity, LeftRightAndRightDenominatorLeftUnit, LeftLeftAndRightDenominatorRightQuantity, LeftLeftAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<RightNumeratorQuantity>, WrappedRightNumeratorUnit, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>, RightDenominatorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightDenominatorRightQuantity, LeftRightAndRightDenominatorLeftQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<RightNumeratorQuantity>, UndefinedQuantityType.Multiplying<LeftRightAndRightDenominatorLeftQuantity, LeftLeftAndRightDenominatorRightQuantity>>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: RightNumeratorUnit
		->
		DefaultScientificValue(value, unit)
	}

