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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("undefinedAPerUndefinedBValueDivUndefinedAPerUndefinedCValue")
fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	TargetUnit : UndefinedDividedUnit<DenominatorDenominatorQuantity, DenominatorDenominatorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<DenominatorDenominatorQuantity, NumeratorDenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
	denominatorDenominatorUnitPerNumeratorDenominatorUnit: DenominatorDenominatorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.denominator.denominatorDenominatorUnitPerNumeratorDenominatorUnit(unit.denominator).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAPerUndefinedBValueDivMetricAndImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAPerUndefinedBValueDivMetricUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAPerUndefinedBValueDivImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAPerUndefinedBValueDivUKImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAPerUndefinedBValueDivUSCustomaryUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAPerUndefinedBValueDivMetricAndUKImperialUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAPerUndefinedBValueDivMetricAndUSCustomaryUndefinedAPerUndefinedCValue")
infix operator fun <
	NumeratorNumeratorAndDenominatorNumeratorQuantity : UndefinedQuantityType,
	NumeratorNumeratorAndDenominatorNumeratorUnit,
	NumeratorDenominatorQuantity : UndefinedQuantityType,
	NumeratorDenominatorUnit,
	NumeratorUnit,
	DenominatorDenominatorQuantity : UndefinedQuantityType,
	DenominatorDenominatorUnit,
	DenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorDenominatorQuantity>, NumeratorUnit>.div(
	right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorNumeratorAndDenominatorNumeratorQuantity, DenominatorDenominatorQuantity>, DenominatorUnit>,
) where
	NumeratorNumeratorAndDenominatorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity>,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorNumeratorAndDenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedDividedUnit<NumeratorNumeratorAndDenominatorNumeratorQuantity, NumeratorNumeratorAndDenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		denominatorDenominatorUnitPerNumeratorDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				DenominatorDenominatorQuantity,
				DenominatorDenominatorUnit,
				NumeratorDenominatorQuantity,
				NumeratorDenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

