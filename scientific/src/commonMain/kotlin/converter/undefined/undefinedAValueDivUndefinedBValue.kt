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

@JvmName("undefinedAValueDivUndefinedBValue")
fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	TargetUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>, TargetUnit>
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
	numeratorUnitPerDenominatorUnit: NumeratorUnit.(DenominatorUnit) -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = unit.numeratorUnitPerDenominatorUnit(right.unit).byDividing(this, right, factory)

@JvmName("metricAndImperialUndefinedAValueDivMetricAndImperialUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndImperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricUndefinedAValueDivMetricUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Metric<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialUndefinedAValueDivImperialUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.Imperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialUndefinedAValueDivUKImperialUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.UKImperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryUndefinedAValueDivUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.USCustomary<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialUndefinedAValueDivMetricAndUKImperialUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUKImperial<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryUndefinedAValueDivMetricAndUSCustomaryUndefinedBValue")
infix operator fun <
	NumeratorQuantity : UndefinedQuantityType,
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		numeratorUnitPerDenominatorUnit = { per(it) },
	) {
		value: Decimal,
		unit: UndefinedDividedUnit.MetricAndUSCustomary<
				NumeratorQuantity,
				NumeratorUnit,
				DenominatorQuantity,
				DenominatorUnit
			>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

