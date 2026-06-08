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

// Mul<A, B> * Div<A, Mul<B, B>> -> Div<Mul<A, A>, B>

fun <
    LeftLeftAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorQuantity>,
    LeftRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightNumeratorQuantity,
        LeftLeftUnit,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorQuantity>,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        LeftLeftAndRightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightDenominatorLeftAndRightQuantity,
            LeftRightAndRightDenominatorLeftAndRightQuantity,
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
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightNumeratorQuantity,
                LeftLeftAndRightNumeratorQuantity,
                >,
            LeftRightAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightNumeratorQuantity,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithLeftAsNumeratorAndSquaredDenominatorWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftLeftAndRightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightDenominatorLeftAndRightQuantity,
                LeftRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftLeftUnitXLeftLeftUnit: LeftLeftUnit.(LeftLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftRightUnit: TargetNumeratorUnit.(LeftRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.leftLeftUnitXLeftLeftUnit(
    unit.left,
).targetNumeratorUnitPerLeftRightUnit(
    unit.right,
).byMultiplying(this, right, factory)
