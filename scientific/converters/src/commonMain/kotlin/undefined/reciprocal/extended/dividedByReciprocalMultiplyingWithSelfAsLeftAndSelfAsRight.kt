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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.extended

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Inv<Ex<A>> / Inv<Mul<Wr<A>, Ex<A>>> -> A!

fun <
    ExtendedNumeratorReciprocalUnit,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedNumeratorReciprocalUnit,
        >,
    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorReciprocalLeftUnit : DefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity>,
    WrappedDenominatorReciprocalLeftUnit : WrappedUndefinedExtendedUnit<
        NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        >,
    ExtendedDenominatorReciprocalRightUnit,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        WrappedDenominatorReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorReciprocalUnit,
        >,
    DenominatorReciprocalLeftValue : ScientificValue<NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity, DenominatorReciprocalLeftUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithSelfAsLeftAndSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, DenominatorReciprocalLeftUnit) -> DenominatorReciprocalLeftValue,
) where
        ExtendedNumeratorReciprocalUnit : UndefinedExtendedUnit<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedNumeratorReciprocalUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        ExtendedDenominatorReciprocalRightUnit : UndefinedExtendedUnit<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            > =
    right.unit.inverse.left.wrapped.byDividing(this, right, factory)
