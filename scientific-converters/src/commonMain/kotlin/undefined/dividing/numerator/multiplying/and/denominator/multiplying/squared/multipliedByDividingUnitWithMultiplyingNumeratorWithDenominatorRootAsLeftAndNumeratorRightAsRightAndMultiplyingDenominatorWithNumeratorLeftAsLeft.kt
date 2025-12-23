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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, Mul<C, C>> * Div<Mul<C, B>, Mul<A, D>> -> Div<Mul<B, B>, Mul<C, D>>

fun <
    LeftNumeratorLeftAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
    LeftNumeratorRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftQuantity>,
    RightDenominatorRightQuantity : UndefinedQuantityType,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<RightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
        LeftDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorRightQuantity,
                LeftNumeratorRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorRootAsLeftAndNumeratorRightAsRightAndMultiplyingDenominatorWithNumeratorLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightNumeratorLeftQuantity,
                LeftNumeratorRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightDenominatorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorRightUnitXLeftNumeratorRightUnit: LeftNumeratorRightUnit.(LeftNumeratorRightUnit) -> TargetNumeratorUnit,
    leftDenominatorLeftUnitXRightDenominatorRightUnit: LeftDenominatorLeftUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.leftNumeratorRightUnitXLeftNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.left.leftDenominatorLeftUnitXRightDenominatorRightUnit(
        right.unit.denominator.right,
    ),
).byMultiplying(this, right, factory)
