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

// Inv<Mul<A, B>> * Div<Mul<B, B>, Mul<C, A>> -> Div<B, Mul<Mul<A, C>, A>>

fun <
    LeftReciprocalLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorRightQuantity>,
    LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorRightQuantity,
            LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalLeftAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftReciprocalLeftAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightDenominatorRightQuantity,
        LeftReciprocalLeftUnit,
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorRightQuantity,
            RightDenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        LeftReciprocalLeftAndRightDenominatorRightQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightDenominatorRightQuantity,
                RightDenominatorLeftQuantity,
                >,
            LeftReciprocalLeftAndRightDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightDenominatorRightQuantity,
                    RightDenominatorLeftQuantity,
                    >,
                LeftReciprocalLeftAndRightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightDenominatorRightQuantity,
            LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithRightAsRootAndMultiplyingDenominatorWithLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
                LeftReciprocalRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftReciprocalLeftAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalLeftUnitXRightDenominatorLeftUnit: LeftReciprocalLeftUnit.(RightDenominatorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXLeftReciprocalLeftUnit: TargetDenominatorLeftUnit.(LeftReciprocalLeftUnit) -> TargetDenominatorUnit,
    leftReciprocalRightUnitPerTargetDenominatorUnit: LeftReciprocalRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.right.leftReciprocalRightUnitPerTargetDenominatorUnit(
    unit.inverse.left.leftReciprocalLeftUnitXRightDenominatorLeftUnit(
        right.unit.denominator.left,
    ).targetDenominatorLeftUnitXLeftReciprocalLeftUnit(
        unit.inverse.left,
    ),
).byMultiplying(this, right, factory)
