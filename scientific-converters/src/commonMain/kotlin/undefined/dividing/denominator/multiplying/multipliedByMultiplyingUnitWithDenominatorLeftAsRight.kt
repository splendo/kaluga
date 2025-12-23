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

// Div<A, Mul<B, C>> * Mul<D, B> -> Div<Mul<A, D>, C>

fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorQuantity>,
    LeftDenominatorLeftAndRightRightQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
    LeftDenominatorRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightRightQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightLeftQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        LeftDenominatorLeftAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        RightLeftQuantity,
        RightLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorQuantity,
            RightLeftQuantity,
            >,
        TargetNumeratorUnit,
        LeftDenominatorRightQuantity,
        LeftDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorQuantity,
                RightLeftQuantity,
                >,
            LeftDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorQuantity,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightRightQuantity,
            LeftDenominatorRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithDenominatorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftQuantity,
            LeftDenominatorLeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorUnitXRightLeftUnit: LeftNumeratorUnit.(RightLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorRightUnit: TargetNumeratorUnit.(LeftDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitXRightLeftUnit(
    right.unit.left,
).targetNumeratorUnitPerLeftDenominatorRightUnit(
    unit.denominator.right,
).byMultiplying(this, right, factory)
