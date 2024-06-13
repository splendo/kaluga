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
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueTimesDefinedUndefinedCValue")
fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit : ScientificUnit<RightQuantity>,
	WrappedRightUnit : WrappedUndefinedExtendedUnit<RightQuantity, RightUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<RightQuantity>, WrappedRightUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>, TargetNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>, LeftDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
	rightAsUndefined: RightUnit.() -> WrappedRightUnit,
	leftNumeratorUnitXWrappedRightUnit: LeftNumeratorUnit.(WrappedRightUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerLeftDenominatorUnit: TargetNumeratorUnit.(LeftDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numerator.leftNumeratorUnitXWrappedRightUnit(right.unit.rightAsUndefined()).targetNumeratorUnitPerLeftDenominatorUnit(unit.denominator).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueTimesMetricAndImperialDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.MetricAndImperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueTimesMetricDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.Metric<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueTimesImperialDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.Imperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueTimesUKImperialDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.UKImperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueTimesUSCustomaryDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.USCustomary<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueTimesMetricAndUKImperialDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueTimesMetricAndUSCustomaryDefinedUndefinedCValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorQuantity : UndefinedQuantityType,
	LeftDenominatorUnit,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, LeftDenominatorQuantity>, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftNumeratorUnitXWrappedRightUnit = { x(it) },
		targetNumeratorUnitPerLeftDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<LeftNumeratorQuantity, UndefinedQuantityType.Extended<RightQuantity>>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					LeftNumeratorQuantity,
					LeftNumeratorUnit,
					UndefinedQuantityType.Extended<RightQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
				>,
				LeftDenominatorQuantity,
				LeftDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

