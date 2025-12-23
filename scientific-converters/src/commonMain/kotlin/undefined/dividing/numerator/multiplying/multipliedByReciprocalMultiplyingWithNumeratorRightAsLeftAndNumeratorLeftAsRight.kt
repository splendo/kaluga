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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, B>, C> * Inv<Mul<B, A>> -> Inv<C>

fun <
    LeftNumeratorLeftAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
    LeftNumeratorRightAndRightReciprocalLeftQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightReciprocalRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightReciprocalLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightReciprocalRightQuantity,
            LeftNumeratorRightAndRightReciprocalLeftQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftNumeratorLeftAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightReciprocalLeftQuantity,
            LeftNumeratorLeftAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightReciprocalRightQuantity,
            LeftNumeratorRightAndRightReciprocalLeftQuantity,
            >,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithNumeratorRightAsLeftAndNumeratorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightReciprocalLeftQuantity,
                LeftNumeratorLeftAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    reciprocalTargetUnit: LeftDenominatorUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.reciprocalTargetUnit().byMultiplying(this, right, factory)
