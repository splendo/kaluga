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

// Mul<B, A> * Div<Mul<A, A>, B> -> Mul<Mul<A, A>, A>

fun <
    LeftLeftAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
    LeftRightAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightDenominatorQuantity,
        LeftLeftUnit,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftLeftAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        LeftRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftRightAndRightNumeratorLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightDenominatorQuantity,
        LeftRightAndRightNumeratorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithRightAsRootAndLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                LeftRightAndRightNumeratorLeftAndRightQuantity,
                >,
            LeftLeftAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    rightNumeratorUnitXLeftRightUnit: RightNumeratorUnit.(LeftRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.numerator.rightNumeratorUnitXLeftRightUnit(
    unit.right,
).byMultiplying(this, right, factory)
