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

// Div<B, A> * Inv<Mul<A, A>> -> Div<B, Mul<Mul<A, A>, A>>

fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorQuantity>,
    LeftDenominatorAndRightReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalLeftAndRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
            LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
            LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                >,
            LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                    LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                    >,
                LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorQuantity,
        LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalSquaredWithDenominatorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                LeftDenominatorAndRightReciprocalLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightReciprocalUnitXLeftDenominatorUnit: RightReciprocalUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    leftNumeratorUnitPerTargetDenominatorUnit: LeftNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitPerTargetDenominatorUnit(
    right.unit.inverse.rightReciprocalUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
