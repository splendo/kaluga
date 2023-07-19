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
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueDivUndefinedAValue")
fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetUnit : UndefinedReciprocalUnit<NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
	reciprocalTargetUnit: NumeratorDenominatorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.denominator.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueDivMetricAndImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueDivMetricUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueDivImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueDivUKImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueDivUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueDivMetricAndUKImperialUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorQuantity>,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorQuantity, NumeratorNumeratorAndDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			NumeratorDenominatorQuantity,
							NumeratorDenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

