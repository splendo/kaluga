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

// Div<Mul<A, B>, C> * Div<Mul<D, C>, Mul<B, B>> -> Div<Mul<A, D>, B>

fun <
    LeftNumeratorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftQuantity>,
    LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightNumeratorRightQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightNumeratorRightQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftQuantity : UndefinedQuantityType,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<RightNumeratorLeftQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorAndRightNumeratorRightQuantity,
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
            RightNumeratorLeftQuantity,
            LeftDenominatorAndRightNumeratorRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftQuantity,
        LeftNumeratorLeftUnit,
        RightNumeratorLeftQuantity,
        RightNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            RightNumeratorLeftQuantity,
            >,
        TargetNumeratorUnit,
        LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftQuantity,
                RightNumeratorLeftQuantity,
                >,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftQuantity,
            LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
            >,
        LeftDenominatorAndRightNumeratorRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithMultiplyingNumeratorWithDenominatorAsRightAndSquaredDenominatorWithNumeratorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightNumeratorLeftQuantity,
                LeftDenominatorAndRightNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitXRightNumeratorLeftUnit: LeftNumeratorLeftUnit.(RightNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftNumeratorRightUnit: TargetNumeratorUnit.(LeftNumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorLeftUnit(
    right.unit.numerator.left,
).targetNumeratorUnitPerLeftNumeratorRightUnit(
    unit.numerator.right,
).byMultiplying(this, right, factory)
