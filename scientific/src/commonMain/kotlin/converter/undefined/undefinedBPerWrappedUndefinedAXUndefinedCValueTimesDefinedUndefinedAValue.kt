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

@JvmName("metricAndImperialUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesMetricDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBPerWrappedUndefinedAXUndefinedCValueTimesMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorLeftAndRightUnit,
	WrappedLeftDenominatorLeftAndRightUnit,
	LeftDenominatorRightQuantity : UndefinedQuantityType,
	LeftDenominatorRightUnit,
	LeftDenominatorUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorLeftAndRightUnit : AbstractScientificUnit<LeftDenominatorLeftAndRightQuantity>,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorLeftAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorLeftAndRightQuantity, LeftDenominatorLeftAndRightUnit>,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorLeftAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorRightUnit : UndefinedScientificUnit<LeftDenominatorRightQuantity>,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorUnit : UndefinedMultipliedUnit<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, WrappedLeftDenominatorLeftAndRightUnit, LeftDenominatorRightQuantity, LeftDenominatorRightUnit>,
	LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Multiplying<UndefinedQuantityType.Extended<LeftDenominatorLeftAndRightQuantity>, LeftDenominatorRightQuantity>, LeftDenominatorUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

