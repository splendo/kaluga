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
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialWrappedUndefinedBPerWrappedUndefinedAValueTimesMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricWrappedUndefinedBPerWrappedUndefinedAValueTimesMetricDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialWrappedUndefinedBPerWrappedUndefinedAValueTimesImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialWrappedUndefinedBPerWrappedUndefinedAValueTimesUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryWrappedUndefinedBPerWrappedUndefinedAValueTimesUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialWrappedUndefinedBPerWrappedUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryWrappedUndefinedBPerWrappedUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit,
	WrappedLeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericWrappedUndefinedBPerWrappedUndefinedAValueTimesGenericDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftNumeratorUnit : AbstractScientificUnit<LeftNumeratorQuantity>,
	WrappedLeftNumeratorUnit : WrappedUndefinedExtendedUnit<LeftNumeratorQuantity, LeftNumeratorUnit>,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, WrappedLeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<LeftNumeratorQuantity>, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) =
	right times this

