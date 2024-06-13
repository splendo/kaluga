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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueTimesUndefinedBPerWrappedUndefinedAXUndefinedCValue")
fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit : ScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorRightQuantity>, TargetUnit>
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
	rightNumeratorUnitPerRightDenominatorRightUnit: RightNumeratorUnit.(RightDenominatorRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.rightNumeratorUnitPerRightDenominatorRightUnit(right.unit.denominator.right).byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryUndefinedBPerWrappedUndefinedAXUndefinedCValue")
infix operator fun <
	LeftAndRightDenominatorLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorLeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	WrappedLeftAndRightDenominatorLeftUnit,
	RightDenominatorRightQuantity : UndefinedQuantityType,
	RightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>>, RightUnit>,
) where
	LeftAndRightDenominatorLeftUnit : AbstractScientificUnit<LeftAndRightDenominatorLeftQuantity>,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorLeftUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorLeftQuantity, LeftAndRightDenominatorLeftUnit>,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorRightUnit : UndefinedScientificUnit<RightDenominatorRightQuantity>,
	RightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, WrappedLeftAndRightDenominatorLeftUnit, RightDenominatorRightQuantity, RightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftAndRightDenominatorLeftQuantity>, RightDenominatorRightQuantity>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorRightUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorRightQuantity,
				RightDenominatorRightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

