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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.reciprocal
import kotlin.jvm.JvmName

@JvmName("dimensionlessValueDivUndefinedAValue")
fun <
	NumeratorUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	TargetUnit : UndefinedReciprocalUnit<DenominatorQuantity, DenominatorUnit>,
	TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorQuantity>, TargetUnit>
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
	reciprocalTargetUnit: DenominatorUnit.() -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = right.unit.reciprocalTargetUnit().byDividing(this, right, factory)

@JvmName("metricAndImperialDimensionlessValueDivMetricAndImperialUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndImperial<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricDimensionlessValueDivMetricUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Metric<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("imperialDimensionlessValueDivImperialUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.Imperial<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("ukImperialDimensionlessValueDivUKImperialUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.UKImperial<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("usCustomaryDimensionlessValueDivUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.USCustomary<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialDimensionlessValueDivMetricAndUKImperialUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUKImperial,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUKImperial<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryDimensionlessValueDivMetricAndUSCustomaryUndefinedAValue")
infix operator fun <
	NumeratorUnit,
	DenominatorQuantity : UndefinedQuantityType,
	DenominatorUnit
	> ScientificValue<PhysicalQuantity.Dimensionless, NumeratorUnit>.div(
	right: UndefinedScientificValue<DenominatorQuantity, DenominatorUnit>,
) where
	NumeratorUnit : Dimensionless,
	NumeratorUnit : MeasurementUsage.UsedInMetric,
	NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
	DenominatorUnit : MeasurementUsage.UsedInMetric,
	DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		reciprocalTargetUnit = { reciprocal() },
	) {
		value: Decimal,
		unit: UndefinedReciprocalUnit.MetricAndUSCustomary<
			DenominatorQuantity,
							DenominatorUnit>
		->
		DefaultUndefinedScientificValue(value, unit)
	}

