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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesMetricDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBPerUndefinedCXWrappedUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftQuantity : UndefinedQuantityType,
	LeftDenominatorLeftUnit,
	LeftDenominatorRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorRightAndRightUnit,
	WrappedLeftDenominatorRightAndRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftUnit : UndefinedScientificUnit<LeftDenominatorLeftQuantity>,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightAndRightUnit : AbstractScientificUnit<LeftDenominatorRightAndRightQuantity>,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorRightAndRightQuantity, LeftDenominatorRightAndRightUnit>,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<LeftDenominatorLeftQuantity, LeftDenominatorLeftUnit, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>, WrappedLeftDenominatorRightAndRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<LeftDenominatorLeftQuantity, UndefinedQuantityType.Extended<LeftDenominatorRightAndRightQuantity>>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

