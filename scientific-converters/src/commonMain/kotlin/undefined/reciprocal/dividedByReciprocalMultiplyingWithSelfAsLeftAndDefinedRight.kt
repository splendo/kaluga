@file:Suppress("ktlint:standard:wrapping")
/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Inv<A> / Inv<Mul<A, Wr<B>>> -> B!

fun <
    NumeratorReciprocalAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
    NumeratorUnit : UndefinedReciprocalUnit<
        NumeratorReciprocalAndDenominatorReciprocalLeftQuantity,
        NumeratorReciprocalUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorReciprocalRightUnit : DefinedScientificUnit<DenominatorReciprocalRightQuantity>,
    WrappedDenominatorReciprocalRightUnit : WrappedUndefinedExtendedUnit<
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            DenominatorReciprocalRightQuantity,
            >,
        WrappedDenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalAndDenominatorReciprocalLeftQuantity,
            UndefinedQuantityType.Extended<
                DenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorReciprocalUnit,
        >,
    DenominatorReciprocalRightValue : ScientificValue<DenominatorReciprocalRightQuantity, DenominatorReciprocalRightUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        NumeratorReciprocalAndDenominatorReciprocalLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithSelfAsLeftAndDefinedRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalAndDenominatorReciprocalLeftQuantity,
                UndefinedQuantityType.Extended<
                    DenominatorReciprocalRightQuantity,
                    >,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, DenominatorReciprocalRightUnit) -> DenominatorReciprocalRightValue,
) = right.unit.inverse.right.wrapped.byDividing(this, right, factory)
