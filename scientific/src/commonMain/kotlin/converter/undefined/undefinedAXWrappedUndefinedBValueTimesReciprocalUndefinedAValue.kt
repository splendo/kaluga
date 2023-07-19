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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("undefinedAXWrappedUndefinedBValueTimesReciprocalUndefinedAValue")
fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit : ScientificUnit<LeftRightQuantity>,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	LeftRightValue : ScientificValue<LeftRightQuantity, LeftRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
	factory: (Decimal, LeftRightUnit) -> LeftRightValue
) = unit.right.wrapped.byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAXWrappedUndefinedBValueTimesMetricAndImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricUndefinedAXWrappedUndefinedBValueTimesMetricReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAXWrappedUndefinedBValueTimesImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAXWrappedUndefinedBValueTimesUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAXWrappedUndefinedBValueTimesUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAXWrappedUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAXWrappedUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit,
	WrappedLeftRightUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	LeftRightUnit : MeasurementUsage.UsedInMetric,
	LeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	WrappedLeftRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericUndefinedAXWrappedUndefinedBValueTimesGenericReciprocalUndefinedAValue")
infix operator fun <
	LeftLeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftLeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftLeftAndRightReciprocalQuantity>,
	LeftRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftRightUnit : AbstractScientificUnit<LeftRightQuantity>,
	WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<LeftRightQuantity, LeftRightUnit>,
	LeftUnit : UndefinedMultipliedUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit, UndefinedQuantityType.Extended<LeftRightQuantity>, WrappedLeftRightUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftLeftAndRightReciprocalQuantity, LeftLeftAndRightReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<LeftLeftAndRightReciprocalQuantity, UndefinedQuantityType.Extended<LeftRightQuantity>>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftLeftAndRightReciprocalQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: LeftRightUnit
		->
		DefaultScientificValue(value, unit)
	}

