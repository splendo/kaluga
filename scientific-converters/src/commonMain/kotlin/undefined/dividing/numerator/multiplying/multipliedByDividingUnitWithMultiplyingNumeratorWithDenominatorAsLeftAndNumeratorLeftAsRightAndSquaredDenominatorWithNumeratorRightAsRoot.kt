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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, C> * Div<Mul<C, A>, Mul<B, B>> -> Div<Mul<A, A>, B>

fun <
    LeftNumeratorLeftAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorRightQuantity>,
    LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightNumeratorLeftQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
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
            LeftDenominatorAndRightNumeratorLeftQuantity,
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightNumeratorRightQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                >,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftDenominatorAndRightNumeratorLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorAsLeftAndNumeratorLeftAsRightAndSquaredDenominatorWithNumeratorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightNumeratorLeftQuantity,
                LeftNumeratorLeftAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitXLeftNumeratorLeftUnit: LeftNumeratorLeftUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftNumeratorRightUnit: TargetNumeratorUnit.(LeftNumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerLeftNumeratorRightUnit(
    unit.numerator.right,
).byMultiplying(this, right, factory)
