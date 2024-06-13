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

@JvmName("definedUndefinedAValueTimesUndefinedBPerUndefinedCValue")
fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit : ScientificUnit<LeftQuantity>,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	WrappedLeftUnit : WrappedUndefinedExtendedUnit<LeftQuantity, LeftUnit>,
	TargetNumeratorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftQuantity>, WrappedLeftUnit, RightNumeratorQuantity, RightNumeratorUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>, TargetNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>, RightDenominatorQuantity>, TargetUnit>
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
	leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
	wrappedLeftUnitXRightNumeratorUnit: WrappedLeftUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
	targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.leftAsUndefined().wrappedLeftUnitXRightNumeratorUnit(right.unit.numerator).targetNumeratorUnitPerRightDenominatorUnit(right.unit.denominator).byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndImperial<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.Metric<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.Imperial<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.UKImperial<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.USCustomary<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUKImperial<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryUndefinedBPerUndefinedCValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightNumeratorQuantity : UndefinedQuantityType,
	RightNumeratorUnit,
	RightDenominatorQuantity : UndefinedQuantityType,
	RightDenominatorUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<RightNumeratorQuantity, RightDenominatorQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
	RightNumeratorUnit : MeasurementUsage.UsedInMetric,
	RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
	RightDenominatorUnit : MeasurementUsage.UsedInMetric,
	RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightNumeratorUnit = { x(it) },
		targetNumeratorUnitPerRightDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightNumeratorQuantity>,
				UndefinedMultipliedUnit.MetricAndUSCustomary<
					UndefinedQuantityType.Extended<LeftQuantity>,
					WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
					RightNumeratorQuantity,
					RightNumeratorUnit
				>,
				RightDenominatorQuantity,
				RightDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

