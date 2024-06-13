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
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.One
import kotlin.jvm.JvmName

@JvmName("definedUndefinedAValueTimesReciprocalWrappedUndefinedAValue")
fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit : ScientificUnit<LeftAndRightReciprocalQuantity>,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	TargetUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, TargetUnit>
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
	getDimensionless: () -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = getDimensionless().byMultiplying(this, right, factory)

@JvmName("metricAndImperialDefinedUndefinedAValueTimesMetricAndImperialReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricDefinedUndefinedAValueTimesMetricReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialDefinedUndefinedAValueTimesImperialReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialDefinedUndefinedAValueTimesUKImperialReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryDefinedUndefinedAValueTimesUSCustomaryReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDefinedUndefinedAValueTimesMetricAndUKImperialReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUKImperial =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDefinedUndefinedAValueTimesMetricAndUSCustomaryReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit,
	WrappedLeftAndRightReciprocalUnit,
	RightUnit
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>,
	RightUnit : MeasurementUsage.UsedInMetric,
	RightUnit : MeasurementUsage.UsedInUSCustomary =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericDefinedUndefinedAValueTimesGenericReciprocalWrappedUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftAndRightReciprocalUnit : AbstractScientificUnit<LeftAndRightReciprocalQuantity>,
	WrappedLeftAndRightReciprocalUnit : WrappedUndefinedExtendedUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	RightUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>, WrappedLeftAndRightReciprocalUnit>
	> ScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftAndRightReciprocalQuantity>>, RightUnit>,
) =
	times(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

