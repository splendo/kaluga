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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueTimesReciprocalUndefinedBXWrappedUndefinedAValue")
fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit : ScientificUnit<LeftAndRightReciprocalRightQuantity>,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	TargetUnit : UndefinedReciprocalUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<RightReciprocalLeftQuantity>, TargetUnit>
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
	reciprocalTargetUnit: RightReciprocalLeftUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.inverse.left.reciprocalTargetUnit().byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUKImperial,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryReciprocalUndefinedBXWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalRightUnit,
	RightReciprocalLeftQuantity : UndefinedQuantityType,
	RightReciprocalLeftUnit,
	WrappedLeftAndRightReciprocalRightUnit,
	RightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>>, RightUnit>,
) where
	LeftAndRightReciprocalRightUnit : AbstractScientificUnit<LeftAndRightReciprocalRightQuantity>,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalLeftUnit : UndefinedScientificUnit<RightReciprocalLeftQuantity>,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalRightUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalRightQuantity, LeftAndRightReciprocalRightUnit>,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalRightUnit : MeasurementUsage.UsedInUSCustomary,
	RightReciprocalUnit : UndefinedMultipliedUnit<RightReciprocalLeftQuantity, RightReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>, WrappedLeftAndRightReciprocalRightUnit>,
	RightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<RightReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftAndRightReciprocalRightQuantity>>, RightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			RightReciprocalLeftQuantity,
							RightReciprocalLeftUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

