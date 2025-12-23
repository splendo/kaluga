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

// Div<Mul<A, B>, Mul<C, D>> * Div<Mul<A, D>, Mul<B, B>> -> Div<Mul<A, A>, Mul<C, B>>

fun <
    LeftNumeratorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorLeftQuantity>,
    LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftQuantity>,
    LeftDenominatorRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightNumeratorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightNumeratorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorRightAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftQuantity,
            LeftDenominatorRightAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightNumeratorLeftQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightNumeratorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftQuantity,
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorLeftQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightNumeratorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorLeftAsLeftAndDenominatorRightAsRightAndSquaredDenominatorWithNumeratorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorLeftQuantity,
                LeftDenominatorRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitXLeftNumeratorLeftUnit: LeftNumeratorLeftUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    leftDenominatorLeftUnitXLeftNumeratorRightUnit: LeftDenominatorLeftUnit.(LeftNumeratorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.left.leftDenominatorLeftUnitXLeftNumeratorRightUnit(
        unit.numerator.right,
    ),
).byMultiplying(this, right, factory)
