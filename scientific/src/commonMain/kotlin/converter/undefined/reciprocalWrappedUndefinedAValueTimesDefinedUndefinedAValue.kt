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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialReciprocalWrappedUndefinedAValueTimesMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalWrappedUndefinedAValueTimesMetricDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalWrappedUndefinedAValueTimesImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalWrappedUndefinedAValueTimesUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalWrappedUndefinedAValueTimesUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalWrappedUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalWrappedUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit,
	WrappedLeftReciprocalAndRightUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) where
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("genericReciprocalWrappedUndefinedAValueTimesGenericDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalAndRightUnit : AbstractScientificUnit<LeftReciprocalAndRightQuantity>,
	WrappedLeftReciprocalAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>, WrappedLeftReciprocalAndRightUnit>
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<LeftReciprocalAndRightQuantity>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalAndRightQuantity, LeftReciprocalAndRightUnit>,
) =
	right times this

