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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> * Div<Mul<B, C>, Mul<A, A>> -> Div<C, Mul<Mul<A, A>, A>>

fun <
    LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity>,
    LeftReciprocalRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightNumeratorLeftQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalRightAndRightNumeratorLeftQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftQuantity>,
    RightNumeratorRightQuantity : UndefinedQuantityType,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<RightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                    LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                    >,
                LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalRightAndRightNumeratorLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithRightAsLeftAndSquaredDenominatorWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightDenominatorUnitXLeftReciprocalLeftUnit: RightDenominatorUnit.(LeftReciprocalLeftUnit) -> TargetDenominatorUnit,
    rightNumeratorRightUnitPerTargetDenominatorUnit: RightNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.right.rightNumeratorRightUnitPerTargetDenominatorUnit(
    right.unit.denominator.rightDenominatorUnitXLeftReciprocalLeftUnit(
        unit.inverse.left,
    ),
).byMultiplying(this, right, factory)
