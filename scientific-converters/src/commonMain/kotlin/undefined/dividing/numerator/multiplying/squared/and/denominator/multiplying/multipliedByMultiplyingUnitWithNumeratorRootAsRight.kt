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

// Div<Mul<A, A>, Mul<B, C>> * Mul<D, A> -> Div<Mul<Mul<A, A>, Mul<D, A>>, Mul<B, C>>

fun <
    LeftNumeratorLeftAndRightAndRightRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightRightQuantity,
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
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightLeftQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        LeftNumeratorLeftAndRightAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightLeftQuantity,
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            >,
        RightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightRightQuantity,
                LeftNumeratorLeftAndRightAndRightRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightLeftQuantity,
                LeftNumeratorLeftAndRightAndRightRightQuantity,
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
                    LeftNumeratorLeftAndRightAndRightRightQuantity,
                    LeftNumeratorLeftAndRightAndRightRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    RightLeftQuantity,
                    LeftNumeratorLeftAndRightAndRightRightQuantity,
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
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            LeftNumeratorLeftAndRightAndRightRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithNumeratorRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftQuantity,
            LeftNumeratorLeftAndRightAndRightRightQuantity,
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
