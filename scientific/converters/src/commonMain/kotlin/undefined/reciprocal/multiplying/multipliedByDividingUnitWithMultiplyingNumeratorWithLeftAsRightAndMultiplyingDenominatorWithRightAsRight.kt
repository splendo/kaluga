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

// Inv<Mul<A, B>> * Div<Mul<C, A>, Mul<D, B>> -> Div<C, Mul<Mul<B, D>, B>>

fun <
    LeftReciprocalLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
    LeftReciprocalRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightNumeratorRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightDenominatorRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorRightQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftReciprocalLeftAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            LeftReciprocalLeftAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightDenominatorRightQuantity,
        LeftReciprocalRightUnit,
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            RightDenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        LeftReciprocalRightAndRightDenominatorRightQuantity,
        LeftReciprocalRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorRightQuantity,
                RightDenominatorLeftQuantity,
                >,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorLeftQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalRightAndRightDenominatorRightQuantity,
                    RightDenominatorLeftQuantity,
                    >,
                LeftReciprocalRightAndRightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorRightQuantity,
            LeftReciprocalRightAndRightDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithLeftAsRightAndMultiplyingDenominatorWithRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftReciprocalLeftAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftReciprocalRightAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalRightUnitXRightDenominatorLeftUnit: LeftReciprocalRightUnit.(RightDenominatorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXLeftReciprocalRightUnit: TargetDenominatorLeftUnit.(LeftReciprocalRightUnit) -> TargetDenominatorUnit,
    rightNumeratorLeftUnitPerTargetDenominatorUnit: RightNumeratorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.left.rightNumeratorLeftUnitPerTargetDenominatorUnit(
    unit.inverse.right.leftReciprocalRightUnitXRightDenominatorLeftUnit(
        right.unit.denominator.left,
    ).targetDenominatorLeftUnitXLeftReciprocalRightUnit(
        unit.inverse.right,
    ),
).byMultiplying(this, right, factory)
