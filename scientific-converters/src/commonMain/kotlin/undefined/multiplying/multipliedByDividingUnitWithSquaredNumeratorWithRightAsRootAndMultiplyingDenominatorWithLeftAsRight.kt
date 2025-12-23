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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<B, A> * Div<Mul<A, A>, Mul<C, B>> -> Div<Mul<Mul<A, A>, A>, C>

fun <
    LeftLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
    LeftRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightDenominatorRightQuantity,
        LeftLeftUnit,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftLeftAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftLeftAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftRightAndRightNumeratorLeftAndRightQuantity,
                    LeftRightAndRightNumeratorLeftAndRightQuantity,
                    >,
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightDenominatorRightQuantity,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithRightAsRootAndMultiplyingDenominatorWithLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftLeftAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftRightUnit: RightNumeratorUnit.(LeftRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorLeftUnit: TargetNumeratorUnit.(RightDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftRightUnit(
    unit.right,
).targetNumeratorUnitPerRightDenominatorLeftUnit(
    right.unit.denominator.left,
).byMultiplying(this, right, factory)
