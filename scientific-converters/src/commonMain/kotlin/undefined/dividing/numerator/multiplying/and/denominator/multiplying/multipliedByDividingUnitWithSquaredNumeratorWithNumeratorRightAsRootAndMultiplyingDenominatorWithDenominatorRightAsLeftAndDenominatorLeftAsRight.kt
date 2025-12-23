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

// Div<Mul<A, B>, Mul<C, D>> * Div<Mul<B, B>, Mul<D, C>> -> Div<Mul<Mul<A, B>, Mul<B, B>>, Mul<Mul<C, D>, Mul<D, C>>>

fun <
    LeftNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftQuantity>,
    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorRightQuantity>,
    LeftDenominatorRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorLeftQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightDenominatorRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightDenominatorLeftQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorRightQuantity,
            LeftDenominatorRightAndRightDenominatorLeftQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorRightAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftDenominatorLeftAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorRightQuantity,
            LeftDenominatorRightAndRightDenominatorLeftQuantity,
            >,
        LeftDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftQuantity,
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightDenominatorRightQuantity,
                LeftDenominatorRightAndRightDenominatorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightAndRightDenominatorLeftQuantity,
                LeftDenominatorLeftAndRightDenominatorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftQuantity,
                    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                    LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightDenominatorRightQuantity,
                    LeftDenominatorRightAndRightDenominatorLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorRightAndRightDenominatorLeftQuantity,
                    LeftDenominatorLeftAndRightDenominatorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorRightQuantity,
            LeftDenominatorRightAndRightDenominatorLeftQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithNumeratorRightAsRootAndMultiplyingDenominatorWithDenominatorRightAsLeftAndDenominatorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                LeftNumeratorRightAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightAndRightDenominatorLeftQuantity,
                LeftDenominatorLeftAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightNumeratorUnit: LeftNumeratorUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXRightDenominatorUnit: LeftDenominatorUnit.(RightDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightNumeratorUnit(
    right.unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXRightDenominatorUnit(
        right.unit.denominator,
    ),
).byMultiplying(this, right, factory)
