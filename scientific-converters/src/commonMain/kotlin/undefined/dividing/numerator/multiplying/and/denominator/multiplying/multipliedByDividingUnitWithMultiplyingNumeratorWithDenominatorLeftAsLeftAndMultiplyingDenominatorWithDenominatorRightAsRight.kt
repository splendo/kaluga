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

// Div<Mul<A, B>, Mul<C, D>> * Div<Mul<C, E>, Mul<F, D>> -> Div<Mul<Mul<A, B>, E>, Mul<Mul<D, F>, D>>

fun <
    LeftNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftQuantity>,
    LeftNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
    LeftDenominatorRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftQuantity>,
    RightNumeratorRightQuantity : UndefinedQuantityType,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<RightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        LeftDenominatorRightAndRightDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightDenominatorRightQuantity,
            RightDenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftQuantity,
                LeftNumeratorRightQuantity,
                >,
            RightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightAndRightDenominatorRightQuantity,
                RightDenominatorLeftQuantity,
                >,
            LeftDenominatorRightAndRightDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftQuantity,
                    LeftNumeratorRightQuantity,
                    >,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorRightAndRightDenominatorRightQuantity,
                    RightDenominatorLeftQuantity,
                    >,
                LeftDenominatorRightAndRightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftQuantity,
            LeftDenominatorRightAndRightDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorLeftAsLeftAndMultiplyingDenominatorWithDenominatorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftDenominatorRightAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightNumeratorRightUnit: LeftNumeratorUnit.(RightNumeratorRightUnit) -> TargetNumeratorUnit,
    leftDenominatorRightUnitXRightDenominatorLeftUnit: LeftDenominatorRightUnit.(RightDenominatorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXLeftDenominatorRightUnit: TargetDenominatorLeftUnit.(LeftDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightNumeratorRightUnit(
    right.unit.numerator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.right.leftDenominatorRightUnitXRightDenominatorLeftUnit(
        right.unit.denominator.left,
    ).targetDenominatorLeftUnitXLeftDenominatorRightUnit(
        unit.denominator.right,
    ),
).byMultiplying(this, right, factory)
