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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, Mul<B, C>> * Mul<C, C> -> Div<Mul<Mul<A, A>, C>, B>

fun <
    LeftNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftQuantity>,
    LeftDenominatorRightAndRightLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightLeftAndRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightLeftAndRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightLeftAndRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightLeftAndRightQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightLeftAndRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftDenominatorRightAndRightLeftAndRightQuantity,
        RightLeftUnit,
        LeftDenominatorRightAndRightLeftAndRightQuantity,
        RightRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorRightAndRightLeftAndRightQuantity,
        LeftDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightQuantity,
                LeftNumeratorLeftAndRightQuantity,
                >,
            LeftDenominatorRightAndRightLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightQuantity,
                    LeftNumeratorLeftAndRightQuantity,
                    >,
                LeftDenominatorRightAndRightLeftAndRightQuantity,
                >,
            LeftDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedBySquaredUnitWithDenominatorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightAndRightLeftAndRightQuantity,
            LeftDenominatorRightAndRightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftDenominatorRightUnit: LeftNumeratorUnit.(LeftDenominatorRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorLeftUnit: TargetNumeratorUnit.(LeftDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftDenominatorRightUnit(
    unit.denominator.right,
).targetNumeratorUnitPerLeftDenominatorLeftUnit(
    unit.denominator.left,
).byMultiplying(this, right, factory)
