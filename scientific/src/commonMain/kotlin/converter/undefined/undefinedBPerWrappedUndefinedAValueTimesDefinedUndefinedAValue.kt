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
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialUndefinedBPerWrappedUndefinedAValueTimesMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricUndefinedBPerWrappedUndefinedAValueTimesMetricDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialUndefinedBPerWrappedUndefinedAValueTimesImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialUndefinedBPerWrappedUndefinedAValueTimesUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryUndefinedBPerWrappedUndefinedAValueTimesUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialUndefinedBPerWrappedUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryUndefinedBPerWrappedUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit,
	WrappedLeftDenominatorAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) where
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
	LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftDenominatorAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericUndefinedBPerWrappedUndefinedAValueTimesGenericDefinedUndefinedAValue")
infix operator fun <
	LeftNumeratorQuantity : UndefinedQuantityType,
	LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
	LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftDenominatorAndRightUnit : AbstractScientificUnit<LeftDenominatorAndRightQuantity>,
	WrappedLeftDenominatorAndRightUnit : WrappedUndefinedExtendedUnit<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
	LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>, WrappedLeftDenominatorAndRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorQuantity, UndefinedQuantityType.Extended<LeftDenominatorAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftDenominatorAndRightQuantity, LeftDenominatorAndRightUnit>,
) =
	right times this

