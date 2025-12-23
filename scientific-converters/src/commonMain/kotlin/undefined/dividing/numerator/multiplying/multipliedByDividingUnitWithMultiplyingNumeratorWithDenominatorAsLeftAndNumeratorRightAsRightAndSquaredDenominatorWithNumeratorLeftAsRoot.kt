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

// Div<Mul<A, B>, C> * Div<Mul<C, B>, Mul<A, A>> -> Div<Mul<B, B>, A>

fun <
    LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity>,
    LeftNumeratorRightAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightNumeratorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightNumeratorLeftQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightNumeratorLeftQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        LeftNumeratorRightAndRightNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        TargetNumeratorUnit,
        LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightNumeratorRightQuantity,
                LeftNumeratorRightAndRightNumeratorRightQuantity,
                >,
            LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorRightAndRightNumeratorRightQuantity,
            >,
        LeftDenominatorAndRightNumeratorLeftQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorAsLeftAndNumeratorRightAsRightAndSquaredDenominatorWithNumeratorLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightNumeratorLeftQuantity,
                LeftNumeratorRightAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorRightUnitXLeftNumeratorRightUnit: LeftNumeratorRightUnit.(LeftNumeratorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftNumeratorLeftUnit: TargetNumeratorUnit.(LeftNumeratorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.leftNumeratorRightUnitXLeftNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerLeftNumeratorLeftUnit(
    unit.numerator.left,
).byMultiplying(this, right, factory)
