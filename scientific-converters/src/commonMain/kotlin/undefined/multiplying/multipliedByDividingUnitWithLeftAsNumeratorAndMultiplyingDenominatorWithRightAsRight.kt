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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, B> * Div<A, Mul<C, B>> -> Div<Mul<A, A>, C>

fun <
    LeftLeftAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorQuantity>,
    LeftRightAndRightDenominatorRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightNumeratorQuantity,
        LeftLeftUnit,
        LeftRightAndRightDenominatorRightQuantity,
        LeftRightUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorQuantity>,
    RightDenominatorLeftQuantity : UndefinedQuantityType,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<RightDenominatorLeftQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        LeftRightAndRightDenominatorRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftLeftAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            RightDenominatorLeftQuantity,
            LeftRightAndRightDenominatorRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightNumeratorQuantity,
        LeftLeftUnit,
        LeftLeftAndRightNumeratorQuantity,
        LeftLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightNumeratorQuantity,
            LeftLeftAndRightNumeratorQuantity,
            >,
        TargetNumeratorUnit,
        RightDenominatorLeftQuantity,
        RightDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightNumeratorQuantity,
                LeftLeftAndRightNumeratorQuantity,
                >,
            RightDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightNumeratorQuantity,
        LeftRightAndRightDenominatorRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithLeftAsNumeratorAndMultiplyingDenominatorWithRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftLeftAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                RightDenominatorLeftQuantity,
                LeftRightAndRightDenominatorRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftLeftUnitXLeftLeftUnit: LeftLeftUnit.(LeftLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorLeftUnit: TargetNumeratorUnit.(RightDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.leftLeftUnitXLeftLeftUnit(
    unit.left,
).targetNumeratorUnitPerRightDenominatorLeftUnit(
    right.unit.denominator.left,
).byMultiplying(this, right, factory)
