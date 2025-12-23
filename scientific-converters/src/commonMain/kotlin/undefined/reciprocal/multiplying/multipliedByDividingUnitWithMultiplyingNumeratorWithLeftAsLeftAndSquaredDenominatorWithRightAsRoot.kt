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

// Inv<Mul<A, B>> * Div<Mul<A, C>, Mul<B, B>> -> Div<C, Mul<Mul<B, B>, B>>

fun <
    LeftReciprocalLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightNumeratorLeftQuantity>,
    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightNumeratorLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
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
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                >,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightNumeratorLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithLeftAsLeftAndSquaredDenominatorWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightDenominatorUnitXLeftReciprocalRightUnit: RightDenominatorUnit.(LeftReciprocalRightUnit) -> TargetDenominatorUnit,
    rightNumeratorRightUnitPerTargetDenominatorUnit: RightNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.right.rightNumeratorRightUnitPerTargetDenominatorUnit(
    right.unit.denominator.rightDenominatorUnitXLeftReciprocalRightUnit(
        unit.inverse.right,
    ),
).byMultiplying(this, right, factory)
