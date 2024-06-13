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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("undefinedAValueTimesDefinedUndefinedBValue")
fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit : ScientificUnit<RightQuantity>,
	WrappedRightUnit : WrappedUndefinedExtendedUnit<RightQuantity, RightUnit>,
	TargetUnit : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, UndefinedQuantityType.Extended<RightQuantity>, WrappedRightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftQuantity, UndefinedQuantityType.Extended<RightQuantity>>, TargetUnit>
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
	rightAsUndefined: RightUnit.() -> WrappedRightUnit,
	leftUnitXWrappedRightUnit: LeftUnit.(WrappedRightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.leftUnitXWrappedRightUnit(right.unit.rightAsUndefined()).byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueTimesMetricAndImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
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
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueTimesMetricDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.Metric<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueTimesImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueTimesUKImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueTimesUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedBValue")
infix operator fun <
	LeftQuantity : UndefinedQuantityType,
	LeftUnit,
	RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	RightUnit
	> UndefinedScientificValue<LeftQuantity, LeftUnit>.times(
	right: ScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : UndefinedScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : AbstractScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		rightAsUndefined = { asUndefined() },
		leftUnitXWrappedRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				LeftQuantity,
				LeftUnit,
				UndefinedQuantityType.Extended<RightQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<RightQuantity, RightUnit>
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

