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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.One
import kotlin.jvm.JvmName

@JvmName("undefinedAValueTimesReciprocalUndefinedAValue")
fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
	TargetUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, TargetUnit>
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
	getDimensionless: () -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = getDimensionless().byMultiplying(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueTimesMetricAndImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("metricUndefinedAValueTimesMetricReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("imperialUndefinedAValueTimesImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("ukImperialUndefinedAValueTimesUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("usCustomaryUndefinedAValueTimesUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("metricAndUKImperialUndefinedAValueTimesMetricAndUKImperialReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("metricAndUSCustomaryUndefinedAValueTimesMetricAndUSCustomaryReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit,
	RightUnit
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
) where
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftAndRightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>,
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

@JvmName("genericUndefinedAValueTimesGenericReciprocalUndefinedAValue")
infix operator fun <
	LeftAndRightReciprocalQuantity : UndefinedQuantityType,
	LeftAndRightReciprocalUnit : UndefinedScientificUnit<LeftAndRightReciprocalQuantity>,
	RightUnit : UndefinedReciprocalUnit<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>
	> UndefinedScientificValue<LeftAndRightReciprocalQuantity, LeftAndRightReciprocalUnit>.times(
	right: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<LeftAndRightReciprocalQuantity>, RightUnit>,
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

