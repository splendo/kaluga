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

// Div<Mul<A, A>, B> * Div<B, Mul<A, C>> -> Div<A, C>

fun <
    LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorAndRightNumeratorQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity>,
    RightDenominatorRightQuantity : UndefinedQuantityType,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<RightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftDenominatorAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
        LeftNumeratorLeftUnit,
        RightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
            RightDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
            >,
        LeftDenominatorAndRightNumeratorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithDenominatorAsNumeratorAndMultiplyingDenominatorWithNumeratorRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftDenominatorAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightDenominatorLeftQuantity,
                RightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitPerRightDenominatorRightUnit: LeftNumeratorLeftUnit.(RightDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitPerRightDenominatorRightUnit(
    right.unit.denominator.right,
).byMultiplying(this, right, factory)
