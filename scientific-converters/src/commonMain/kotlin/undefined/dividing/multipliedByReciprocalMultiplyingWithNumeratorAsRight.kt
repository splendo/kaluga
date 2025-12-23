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

package com.splendo.kaluga.scientific.converter.undefined.dividing

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<B, C> * Inv<Mul<A, B>> -> Inv<Mul<C, A>>

fun <
    LeftNumeratorAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightReciprocalRightQuantity>,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightReciprocalRightQuantity,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightReciprocalLeftQuantity : UndefinedQuantityType,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<RightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        RightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftNumeratorAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            RightReciprocalLeftQuantity,
            LeftNumeratorAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        RightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorQuantity,
            RightReciprocalLeftQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorQuantity,
                RightReciprocalLeftQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightReciprocalRightQuantity,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithNumeratorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                RightReciprocalLeftQuantity,
                LeftNumeratorAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftDenominatorUnitXRightReciprocalLeftUnit: LeftDenominatorUnit.(RightReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.leftDenominatorUnitXRightReciprocalLeftUnit(
    right.unit.inverse.left,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
