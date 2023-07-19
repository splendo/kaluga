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

@JvmName("wrappedUndefinedAXUndefinedBValueTimesReciprocalUndefinedBValue")
fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit : ScientificUnit<LeftLeftQuantity>,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftLeftValue : ScientificValue<LeftLeftQuantity, LeftLeftUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
	factory: (Decimal, LeftLeftUnit) -> LeftLeftValue
) = unit.left.wrapped.byMultiplying(this, right, factory)

@JvmName("metricAndImperialWrappedUndefinedAXUndefinedBValueTimesMetricAndImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricWrappedUndefinedAXUndefinedBValueTimesMetricReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialWrappedUndefinedAXUndefinedBValueTimesImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialWrappedUndefinedAXUndefinedBValueTimesUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryWrappedUndefinedAXUndefinedBValueTimesUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialWrappedUndefinedAXUndefinedBValueTimesMetricAndUKImperialReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryWrappedUndefinedAXUndefinedBValueTimesMetricAndUSCustomaryReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit,
	WrappedLeftLeftUnit,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit,
	LeftUnit,
	RightUnit
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	LeftLeftUnit : MeasurementUsage.UsedInMetric,
	LeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftRightAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericWrappedUndefinedAXUndefinedBValueTimesGenericReciprocalUndefinedBValue")
infix operator fun <
	LeftLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftLeftUnit : AbstractScientificUnit<LeftLeftQuantity>,
	WrappedLeftLeftUnit : WrappedUndefinedExtendedUnit<LeftLeftQuantity, LeftLeftUnit>,
	LeftRightAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftRightAndRightReciprocalUnit : UndefinedScientificUnit<LeftRightAndRightReciprocalQuantity>,
	LeftUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftLeftQuantity>, WrappedLeftLeftUnit, LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>,
	RightUnit : UndefinedReciprocalUnit<LeftRightAndRightReciprocalQuantity, LeftRightAndRightReciprocalUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftLeftQuantity>, LeftRightAndRightReciprocalQuantity>, LeftUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftRightAndRightReciprocalQuantity>, RightUnit>,
) =
	times(right) {
		value: Decimal,
		unit: LeftLeftUnit
		->
		DefaultScientificValue(value, unit)
	}

