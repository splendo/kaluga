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

// Div<Mul<A, A>, Mul<B, C>> * Mul<A, C> -> Div<Mul<Mul<A, A>, A>, B>

fun <
    LeftNumeratorLeftAndRightAndRightLeftQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightLeftQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightLeftQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftQuantity>,
    LeftDenominatorRightAndRightRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightLeftQuantity,
        RightLeftUnit,
        LeftDenominatorRightAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            >,
        LeftNumeratorUnit,
        LeftNumeratorLeftAndRightAndRightLeftQuantity,
        LeftNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightLeftQuantity,
                LeftNumeratorLeftAndRightAndRightLeftQuantity,
                >,
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            >,
        TargetNumeratorUnit,
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightAndRightLeftQuantity,
                    LeftNumeratorLeftAndRightAndRightLeftQuantity,
                    >,
                LeftNumeratorLeftAndRightAndRightLeftQuantity,
                >,
            LeftDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightAndRightRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithNumeratorRootAsLeftAndDenominatorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightLeftQuantity,
            LeftDenominatorRightAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXLeftNumeratorLeftUnit: LeftNumeratorUnit.(LeftNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorLeftUnit: TargetNumeratorUnit.(LeftDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXLeftNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerLeftDenominatorLeftUnit(
    unit.denominator.left,
).byMultiplying(this, right, factory)
