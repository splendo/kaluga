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

// Mul<A, B> * Div<Mul<A, A>, Mul<B, B>> -> Div<Mul<Mul<A, A>, A>, B>

fun <
    LeftLeftAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorLeftAndRightQuantity>,
    LeftRightAndRightDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightNumeratorLeftAndRightQuantity,
        LeftLeftUnit,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftLeftAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightDenominatorLeftAndRightQuantity>,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorLeftUnit,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        RightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightNumeratorLeftAndRightQuantity,
            LeftLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightDenominatorLeftAndRightQuantity,
            LeftRightAndRightDenominatorLeftAndRightQuantity,
            >,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightNumeratorLeftAndRightQuantity,
            LeftLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftLeftAndRightNumeratorLeftAndRightQuantity,
        LeftLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightNumeratorLeftAndRightQuantity,
                LeftLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftLeftAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftLeftAndRightNumeratorLeftAndRightQuantity,
                    LeftLeftAndRightNumeratorLeftAndRightQuantity,
                    >,
                LeftLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftRightAndRightDenominatorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightNumeratorLeftAndRightQuantity,
        LeftRightAndRightDenominatorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithLeftAsRootAndSquaredDenominatorWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightNumeratorLeftAndRightQuantity,
                LeftLeftAndRightNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightDenominatorLeftAndRightQuantity,
                LeftRightAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftLeftUnit: RightNumeratorUnit.(LeftLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftRightUnit: TargetNumeratorUnit.(LeftRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftLeftUnit(
    unit.left,
).targetNumeratorUnitPerLeftRightUnit(
    unit.right,
).byMultiplying(this, right, factory)
