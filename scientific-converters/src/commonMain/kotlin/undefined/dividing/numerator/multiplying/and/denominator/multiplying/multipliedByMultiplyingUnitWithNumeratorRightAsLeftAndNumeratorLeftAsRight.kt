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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, Mul<C, D>> * Mul<B, A> -> Div<Mul<Mul<A, B>, Mul<B, A>>, Mul<C, D>>

fun <
    LeftNumeratorLeftAndRightRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightRightQuantity>,
    LeftNumeratorRightAndRightLeftQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightLeftQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightAndRightLeftQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftQuantity>,
    LeftDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightRightQuantity,
            LeftNumeratorRightAndRightLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightAndRightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftNumeratorRightAndRightLeftQuantity,
        RightLeftUnit,
        LeftNumeratorLeftAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightRightQuantity,
            LeftNumeratorRightAndRightLeftQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightLeftQuantity,
            LeftNumeratorLeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightRightQuantity,
                LeftNumeratorRightAndRightLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftNumeratorRightAndRightLeftQuantity,
                LeftNumeratorLeftAndRightRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorLeftAndRightRightQuantity,
                    LeftNumeratorRightAndRightLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftNumeratorRightAndRightLeftQuantity,
                    LeftNumeratorLeftAndRightRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorLeftQuantity,
                LeftDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightRightQuantity,
            LeftNumeratorRightAndRightLeftQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithNumeratorRightAsLeftAndNumeratorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorRightAndRightLeftQuantity,
            LeftNumeratorLeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightUnit: LeftNumeratorUnit.(RightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorUnit: TargetNumeratorUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightUnit(
    right.unit,
).targetNumeratorUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
