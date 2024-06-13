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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueTimesReciprocalUndefinedBValue")
fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit : ScientificUnit<LeftQuantity>,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	WrappedLeftUnit : WrappedUndefinedExtendedUnit<LeftQuantity, LeftUnit>,
	TargetUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftQuantity>, WrappedLeftUnit, RightReciprocalQuantity, RightReciprocalUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftQuantity>, RightReciprocalQuantity>, TargetUnit>
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
	leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
	wrappedLeftUnitPerRightReciprocalUnit: WrappedLeftUnit.(RightReciprocalUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.leftAsUndefined().wrappedLeftUnitPerRightReciprocalUnit(right.unit.inverse).byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndImperial<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.Metric<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.Imperial<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.UKImperial<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.USCustomary<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUKImperial<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftUnit,
	RightReciprocalQuantity : UndefinedQuantityType,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftQuantity, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalQuantity>, RightUnit>,
) where
	LeftUnit : AbstractScientificUnit<LeftQuantity>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		leftAsUndefined = { asUndefined() },
		wrappedLeftUnitPerRightReciprocalUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				UndefinedQuantityType.Extended<LeftQuantity>,
				WrappedUndefinedExtendedUnit.MetricAndUSCustomary<LeftQuantity, LeftUnit>,
				RightReciprocalQuantity,
				RightReciprocalUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

