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

// Div<Mul<A, B>, Mul<C, C>> * Div<Mul<B, D>, Mul<C, A>> -> Div<Mul<Mul<B, B>, D>, Mul<Mul<C, C>, C>>

fun <
    LeftNumeratorLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorRightQuantity>,
    LeftNumeratorRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftQuantity>,
    RightNumeratorRightQuantity : UndefinedQuantityType,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<RightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorLeftAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            LeftNumeratorLeftAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorLeftQuantity,
        LeftNumeratorRightUnit,
        LeftNumeratorRightAndRightNumeratorLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            >,
        LeftDenominatorUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftQuantity,
                LeftNumeratorRightAndRightNumeratorLeftQuantity,
                >,
            RightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
                LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
                >,
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorRightAndRightNumeratorLeftQuantity,
                    LeftNumeratorRightAndRightNumeratorLeftQuantity,
                    >,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
                    LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
                    >,
                LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorRightAsLeftAndMultiplyingDenominatorWithDenominatorRootAsLeftAndNumeratorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightDenominatorLeftQuantity,
                LeftNumeratorLeftAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorRightUnitXLeftNumeratorRightUnit: LeftNumeratorRightUnit.(LeftNumeratorRightUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXRightNumeratorRightUnit: TargetNumeratorLeftUnit.(RightNumeratorRightUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorLeftUnit: LeftDenominatorUnit.(LeftDenominatorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.leftNumeratorRightUnitXLeftNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorLeftUnitXRightNumeratorRightUnit(
    right.unit.numerator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byMultiplying(this, right, factory)
