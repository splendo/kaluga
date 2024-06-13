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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueTimesDefinedUndefinedBValue")
fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit : ScientificUnit<LeftQuantity>,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit : ScientificUnit<RightQuantity>,
	WrappedLeftUnit : WrappedUndefinedExtendedUnit<LeftQuantity, LeftUnit>,
	WrappedRightUnit : WrappedUndefinedExtendedUnit<RightQuantity, RightUnit>,
	TargetUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftQuantity>, WrappedLeftUnit, UndefinedQuantityType.Extended<RightQuantity>, WrappedRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, UndefinedQuantityType.Extended<RightQuantity>>, TargetUnit>
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
	leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
	rightAsUndefined: RightUnit.() -> WrappedRightUnit,
	wrappedLeftUnitXWrappedRightUnit: WrappedLeftUnit.(WrappedRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.leftAsUndefined().wrappedLeftUnitXWrappedRightUnit(right.unit.rightAsUndefined()).byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		rightAsUndefined = { asUndefined() },
		wrappedLeftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

