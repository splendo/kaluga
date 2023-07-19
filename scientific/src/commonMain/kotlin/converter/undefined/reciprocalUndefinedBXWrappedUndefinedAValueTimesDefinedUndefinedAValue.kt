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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

@JvmName("metricAndImperialReciprocalUndefinedBXWrappedUndefinedAValueTimesMetricAndImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricReciprocalUndefinedBXWrappedUndefinedAValueTimesMetricDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric =
	right times this

@JvmName("imperialReciprocalUndefinedBXWrappedUndefinedAValueTimesImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("ukImperialReciprocalUndefinedBXWrappedUndefinedAValueTimesUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("usCustomaryReciprocalUndefinedBXWrappedUndefinedAValueTimesUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

@JvmName("metricAndUKImperialReciprocalUndefinedBXWrappedUndefinedAValueTimesMetricAndUKImperialDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUKImperial,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUKImperial =
	right times this

@JvmName("metricAndUSCustomaryReciprocalUndefinedBXWrappedUndefinedAValueTimesMetricAndUSCustomaryDefinedUndefinedAValue")
infix operator fun <
	LeftReciprocalLeftQuantity : UndefinedQuantityType,
	LeftReciprocalLeftUnit,
	LeftReciprocalRightAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
	LeftReciprocalRightAndRightUnit,
	WrappedLeftReciprocalRightAndRightUnit,
	LeftReciprocalUnit,
	LeftUnit
	> UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>>, LeftUnit>.times(
	right: ScientificValue<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
) where
	LeftReciprocalLeftUnit : UndefinedScientificUnit<LeftReciprocalLeftQuantity>,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalLeftUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalRightAndRightUnit : AbstractScientificUnit<LeftReciprocalRightAndRightQuantity>,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	WrappedLeftReciprocalRightAndRightUnit : WrappedUndefinedExtendedUnit<LeftReciprocalRightAndRightQuantity, LeftReciprocalRightAndRightUnit>,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInMetric,
	WrappedLeftReciprocalRightAndRightUnit : MeasurementUsage.UsedInUSCustomary,
	LeftReciprocalUnit : UndefinedMultipliedUnit<LeftReciprocalLeftQuantity, LeftReciprocalLeftUnit, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>, WrappedLeftReciprocalRightAndRightUnit>,
	LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
	LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
	LeftUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Multiplying<LeftReciprocalLeftQuantity, UndefinedQuantityType.Extended<LeftReciprocalRightAndRightQuantity>>, LeftReciprocalUnit>,
	LeftUnit : MeasurementUsage.UsedInMetric,
	LeftUnit : MeasurementUsage.UsedInUSCustomary =
	right times this

