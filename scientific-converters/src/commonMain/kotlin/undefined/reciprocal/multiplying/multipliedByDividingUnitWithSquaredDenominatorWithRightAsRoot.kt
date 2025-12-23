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

// Inv<Mul<A, B>> * Div<C, Mul<B, B>> -> Div<C, Mul<Mul<A, B>, Mul<B, B>>>

fun <
    LeftReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftQuantity>,
    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<RightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftQuantity,
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftQuantity,
                    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                    LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredDenominatorWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                LeftReciprocalRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXRightDenominatorUnit: LeftReciprocalUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
    rightNumeratorUnitPerTargetDenominatorUnit: RightNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitPerTargetDenominatorUnit(
    unit.inverse.leftReciprocalUnitXRightDenominatorUnit(
        right.unit.denominator,
    ),
).byMultiplying(this, right, factory)
