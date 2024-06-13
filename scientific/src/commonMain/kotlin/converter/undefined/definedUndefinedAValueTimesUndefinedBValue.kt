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

@JvmName("definedUndefinedAValueTimesUndefinedBValue")
fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit : ScientificUnit<LeftQuantity>,
	RightQuantity : UndefinedQuantityType,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	WrappedLeftUnit : WrappedUndefinedExtendedUnit<LeftQuantity, LeftUnit>,
	TargetUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftQuantity>, WrappedLeftUnit, RightQuantity, RightUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftQuantity>, RightQuantity>, TargetUnit>
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
	leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
	wrappedLeftUnitXRightUnit: WrappedLeftUnit.(RightUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.leftAsUndefined().wrappedLeftUnitXRightUnit(right.unit).byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Metric<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.Imperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.UKImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.USCustomary<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightQuantity : UndefinedQuantityType,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<RightQuantity, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedScientificUnit<RightQuantity>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitXRightUnit = { x(it) },
	) {
		value: Decimal,
		unit: UndefinedMultipliedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
				RightQuantity,
				RightUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

