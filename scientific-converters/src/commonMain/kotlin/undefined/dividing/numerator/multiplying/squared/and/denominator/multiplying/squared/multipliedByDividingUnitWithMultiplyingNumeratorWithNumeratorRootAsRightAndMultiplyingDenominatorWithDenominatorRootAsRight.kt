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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, Mul<B, B>> * Div<Mul<C, A>, Mul<D, B>> -> Div<Mul<Mul<A, A>, Mul<C, A>>, Mul<Mul<B, B>, Mul<D, B>>>

fun <
    LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity>,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
                LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                    LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    RightNumeratorLeftQuantity,
                    LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
                    LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    RightDenominatorLeftQuantity,
                    LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithNumeratorRootAsRightAndMultiplyingDenominatorWithDenominatorRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftDenominatorLeftAndRightAndRightDenominatorRightQuantity,
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
