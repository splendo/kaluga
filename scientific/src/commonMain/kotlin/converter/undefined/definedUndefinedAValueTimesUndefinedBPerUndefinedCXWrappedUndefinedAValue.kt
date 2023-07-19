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

@JvmName("definedUndefinedAValueTimesUndefinedBPerUndefinedCXWrappedUndefinedAValue")
fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit : ScientificUnit<LeftAndRightDenominatorRightQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorLeftQuantity, RightDenominatorLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorLeftQuantity>, TargetUnit>
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
	rightNumeratorUnitPerRightDenominatorLeftUnit: RightNumeratorUnit.(RightDenominatorLeftUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.numerator.rightNumeratorUnitPerRightDenominatorLeftUnit(right.unit.denominator.left).byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryUndefinedBPerUndefinedCXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightDenominatorRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightDenominatorRightUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorLeftQuantity : UndefinedQuantityType,
	RightDenominatorLeftUnit,
	WrappedLeftAndRightDenominatorRightUnit,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>>, RightUnit>,
) where
	LeftAndRightDenominatorRightUnit : AbstractScientificUnit<LeftAndRightDenominatorRightQuantity>,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorLeftUnit : UndefinedScientificUnit<RightDenominatorLeftQuantity>,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightDenominatorRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightDenominatorRightQuantity, LeftAndRightDenominatorRightUnit>,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedMultipliedUnit<RightDenominatorLeftQuantity, RightDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>, WrappedLeftAndRightDenominatorRightUnit>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, UndefinedQuantityType.Multiplying<RightDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightDenominatorRightQuantity>>, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightNumeratorUnitPerRightDenominatorLeftUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				RightNumeratorQuantity,
				RightNumeratorUnit,
				RightDenominatorLeftQuantity,
				RightDenominatorLeftUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

