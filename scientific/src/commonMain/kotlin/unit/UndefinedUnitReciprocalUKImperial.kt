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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.UndefinedQuantityType
import kotlin.jvm.JvmName

/**
 * [InverseUnit] -> [UndefinedReciprocalUnit.UKImperial] ([InverseUnit])
 */
@JvmName("uKImperialUndefinedReciprocal")
fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>, InverseUnit : MeasurementUsage.UsedInUKImperial = UndefinedReciprocalUnit.UKImperial(this)

/**
 * [InverseUnit] -> [UndefinedReciprocalUnit.UKImperial] ([WrappedUndefinedExtendedUnit.UKImperial] ([InverseUnit]))
 */
@JvmName("uKImperialDefinedReciprocal")
fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>, InverseUnit : MeasurementUsage.UsedInUKImperial = asUndefined().reciprocal()

/**
 * [ReciprocalUnit] -> [InverseUnit]
 */
@JvmName("uKImperialReciprocalUndefined")
fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    ReciprocalUnit,
    > ReciprocalUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>, InverseUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedReciprocalUnit<InverseQuantity, InverseUnit>, ReciprocalUnit : MeasurementUsage.UsedInUKImperial =
    inverse

/**
 * [ReciprocalUnit] -> [WrappedUnit]
 */
@JvmName("uKImperialReciprocalDefined")
fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit,
    InverseUnit,
    ReciprocalUnit,
    > ReciprocalUnit.reciprocal() where
      WrappedUnit : ScientificUnit<WrappedQuantity>, WrappedUnit : MeasurementUsage.UsedInUKImperial,
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>, InverseUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>, ReciprocalUnit : MeasurementUsage.UsedInUKImperial =
    inverse.wrapped

/**
 * [UndefinedDividedUnit] ([NumeratorUnit], [DenominatorUnit]) -> [UndefinedDividedUnit.UKImperial] ([DenominatorUnit], [NumeratorUnit])
 */
@JvmName("uKImperialDividedUnitReciprocal")
fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    DividerUnit,
    > DividerUnit.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInUKImperial = denominator per numerator
