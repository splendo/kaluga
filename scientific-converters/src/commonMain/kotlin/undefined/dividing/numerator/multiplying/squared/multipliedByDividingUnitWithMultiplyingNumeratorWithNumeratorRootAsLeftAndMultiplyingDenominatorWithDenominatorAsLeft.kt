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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, B> * Div<Mul<A, C>, Mul<B, D>> -> Div<Mul<Mul<A, A>, Mul<A, C>>, Mul<Mul<B, B>, D>>

fun <
    LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightDenominatorLeftQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity>,
    RightNumeratorRightQuantity : UndefinedQuantityType,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<RightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        RightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightDenominatorLeftQuantity>,
    RightDenominatorRightQuantity : UndefinedQuantityType,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<RightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            RightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightDenominatorLeftQuantity,
        LeftDenominatorUnit,
        LeftDenominatorAndRightDenominatorLeftQuantity,
        LeftDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightDenominatorLeftQuantity,
            LeftDenominatorAndRightDenominatorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorLeftQuantity,
                LeftDenominatorAndRightDenominatorLeftQuantity,
                >,
            RightDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                    RightNumeratorRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorAndRightDenominatorLeftQuantity,
                    LeftDenominatorAndRightDenominatorLeftQuantity,
                    >,
                RightDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
            >,
        LeftDenominatorAndRightDenominatorLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorRootAsLeftAndMultiplyingDenominatorWithDenominatorAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorLeftQuantity,
                RightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightDenominatorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightNumeratorUnit: LeftNumeratorUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
    leftDenominatorUnitXLeftDenominatorUnit: LeftDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXRightDenominatorRightUnit: TargetDenominatorLeftUnit.(RightDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightNumeratorUnit(
    right.unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ).targetDenominatorLeftUnitXRightDenominatorRightUnit(
        right.unit.denominator.right,
    ),
).byMultiplying(this, right, factory)
