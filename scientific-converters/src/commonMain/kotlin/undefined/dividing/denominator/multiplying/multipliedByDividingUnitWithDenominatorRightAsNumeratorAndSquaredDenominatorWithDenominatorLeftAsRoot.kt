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

// Div<A, Mul<B, C>> * Div<C, Mul<B, B>> -> Div<A, Mul<Mul<B, B>, B>>

fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorQuantity>,
    LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity>,
    LeftDenominatorRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightNumeratorQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorRightAndRightNumeratorQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftDenominatorRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                    LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                    >,
                LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorQuantity,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
            LeftDenominatorRightAndRightNumeratorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithDenominatorRightAsNumeratorAndSquaredDenominatorWithDenominatorLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftDenominatorRightAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                LeftDenominatorLeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightDenominatorUnitXLeftDenominatorLeftUnit: RightDenominatorUnit.(LeftDenominatorLeftUnit) -> TargetDenominatorUnit,
    leftNumeratorUnitPerTargetDenominatorUnit: LeftNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitPerTargetDenominatorUnit(
    right.unit.denominator.rightDenominatorUnitXLeftDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byMultiplying(this, right, factory)
