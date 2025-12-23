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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, Mul<C, D>> * Div<Mul<E, A>, Mul<C, B>> -> Div<Mul<Mul<A, E>, A>, Mul<Mul<C, D>, C>>

fun <
    LeftNumeratorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorRightQuantity>,
    LeftNumeratorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightDenominatorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorLeftQuantity>,
    LeftDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightDenominatorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftQuantity,
            LeftNumeratorRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            RightNumeratorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        LeftDenominatorLeftAndRightDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                RightNumeratorLeftQuantity,
                >,
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightDenominatorLeftQuantity,
                LeftDenominatorRightQuantity,
                >,
            LeftDenominatorLeftAndRightDenominatorLeftQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightNumeratorRightQuantity,
                    RightNumeratorLeftQuantity,
                    >,
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightDenominatorLeftQuantity,
                    LeftDenominatorRightQuantity,
                    >,
                LeftDenominatorLeftAndRightDenominatorLeftQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightDenominatorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorLeftAsRightAndMultiplyingDenominatorWithDenominatorLeftAsLeftAndNumeratorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightDenominatorLeftQuantity,
                LeftNumeratorRightAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitXRightNumeratorLeftUnit: LeftNumeratorLeftUnit.(RightNumeratorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXLeftNumeratorLeftUnit: TargetNumeratorLeftUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorLeftUnit: LeftDenominatorUnit.(LeftDenominatorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorLeftUnit(
    right.unit.numerator.left,
).targetNumeratorLeftUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byMultiplying(this, right, factory)
