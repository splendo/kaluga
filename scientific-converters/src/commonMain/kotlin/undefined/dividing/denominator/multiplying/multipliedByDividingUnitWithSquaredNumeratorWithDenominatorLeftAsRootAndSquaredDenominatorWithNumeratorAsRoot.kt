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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, Mul<B, C>> * Div<Mul<B, B>, Mul<A, A>> -> Div<B, Mul<C, A>>

fun <
    LeftNumeratorAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftAndRightQuantity>,
    LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity>,
    LeftDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
            LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        LeftNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorRightQuantity,
            LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorRightQuantity,
                LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithDenominatorLeftAsRootAndSquaredDenominatorWithNumeratorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
                LeftDenominatorLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
                LeftNumeratorAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftDenominatorRightUnitXLeftNumeratorUnit: LeftDenominatorRightUnit.(LeftNumeratorUnit) -> TargetDenominatorUnit,
    leftDenominatorLeftUnitPerTargetDenominatorUnit: LeftDenominatorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.left.leftDenominatorLeftUnitPerTargetDenominatorUnit(
    unit.denominator.right.leftDenominatorRightUnitXLeftNumeratorUnit(
        unit.numerator,
    ),
).byMultiplying(this, right, factory)
