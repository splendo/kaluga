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

// Mul<B, A> * Div<Mul<A, A>, Mul<B, C>> -> Div<Mul<Mul<A, A>, A>, C>

fun <
    LeftLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorLeftQuantity>,
    LeftRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightDenominatorLeftQuantity,
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
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorLeftQuantity>,
    RightDenominatorRightQuantity : UndefinedQuantityType,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<RightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
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
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
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
            RightDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightDenominatorLeftQuantity,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithRightAsRootAndMultiplyingDenominatorWithLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightDenominatorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftRightUnit: RightNumeratorUnit.(LeftRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorRightUnit: TargetNumeratorUnit.(RightDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftRightUnit(
    unit.right,
).targetNumeratorUnitPerRightDenominatorRightUnit(
    right.unit.denominator.right,
).byMultiplying(this, right, factory)
