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

// Inv<Mul<A, B>> * Div<Mul<A, C>, Mul<B, D>> -> Div<C, Mul<Mul<B, B>, D>>

fun <
    LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
    LeftReciprocalRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightNumeratorLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightDenominatorLeftQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
    RightNumeratorRightQuantity : UndefinedQuantityType,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<RightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftQuantity>,
    RightDenominatorRightQuantity : UndefinedQuantityType,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<RightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightDenominatorLeftQuantity,
        LeftReciprocalRightUnit,
        LeftReciprocalRightAndRightDenominatorLeftQuantity,
        LeftReciprocalRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorLeftQuantity,
                LeftReciprocalRightAndRightDenominatorLeftQuantity,
                >,
            RightDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalRightAndRightDenominatorLeftQuantity,
                    LeftReciprocalRightAndRightDenominatorLeftQuantity,
                    >,
                RightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithLeftAsLeftAndMultiplyingDenominatorWithRightAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalRightUnitXLeftReciprocalRightUnit: LeftReciprocalRightUnit.(LeftReciprocalRightUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXRightDenominatorRightUnit: TargetDenominatorLeftUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
    rightNumeratorRightUnitPerTargetDenominatorUnit: RightNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.right.rightNumeratorRightUnitPerTargetDenominatorUnit(
    unit.inverse.right.leftReciprocalRightUnitXLeftReciprocalRightUnit(
        unit.inverse.right,
    ).targetDenominatorLeftUnitXRightDenominatorRightUnit(
        right.unit.denominator.right,
    ),
).byMultiplying(this, right, factory)
