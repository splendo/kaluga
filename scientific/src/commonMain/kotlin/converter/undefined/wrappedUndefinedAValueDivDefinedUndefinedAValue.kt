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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.Dimensionless
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.One
import kotlin.jvm.JvmName

@JvmName("wrappedUndefinedAValueDivDefinedUndefinedAValue")
fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit : ScientificUnit<NumeratorAndDenominatorQuantity>,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	TargetUnit : ScientificUnit<PhysicalQuantity.Dimensionless>,
	TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, TargetUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	getDimensionless: () -> TargetUnit,
	factory: (Decimal, TargetUnit) -> TargetValue
) = getDimensionless().byDividing(this, right, factory)

@JvmName("metricAndImperialWrappedUndefinedAValueDivMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricWrappedUndefinedAValueDivMetricDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("imperialWrappedUndefinedAValueDivImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("ukImperialWrappedUndefinedAValueDivUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("usCustomaryWrappedUndefinedAValueDivUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUKImperialWrappedUndefinedAValueDivMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUKImperial =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("metricAndUSCustomaryWrappedUndefinedAValueDivMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit,
	WrappedNumeratorAndDenominatorUnit
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) where
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	NumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInMetric,
	WrappedNumeratorAndDenominatorUnit : MeasurementUsage.UsedInUSCustomary =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

@JvmName("genericWrappedUndefinedAValueDivGenericDefinedUndefinedAValue")
infix operator fun <
	NumeratorAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	NumeratorAndDenominatorUnit : AbstractScientificUnit<NumeratorAndDenominatorQuantity>,
	WrappedNumeratorAndDenominatorUnit : WrappedUndefinedExtendedUnit<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorAndDenominatorQuantity>, WrappedNumeratorAndDenominatorUnit>.div(
	right: ScientificValue<NumeratorAndDenominatorQuantity, NumeratorAndDenominatorUnit>,
) =
	div(
		right,
		getDimensionless = { One },
	) {
		value: Decimal,
		unit: One
		->
		DefaultScientificValue(value, unit)
	}

