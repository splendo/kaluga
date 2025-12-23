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

// Div<Mul<A, A>, B> * Div<Mul<B, B>, Mul<C, A>> -> Div<Mul<A, B>, C>

fun <
    LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftAndRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
            LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
            >,
        LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithDenominatorAsRootAndMultiplyingDenominatorWithNumeratorRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                LeftDenominatorAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftNumeratorLeftAndRightAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitXLeftDenominatorUnit: LeftNumeratorLeftUnit.(LeftDenominatorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorLeftUnit: TargetNumeratorUnit.(RightDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitXLeftDenominatorUnit(
    unit.denominator,
).targetNumeratorUnitPerRightDenominatorLeftUnit(
    right.unit.denominator.left,
).byMultiplying(this, right, factory)
