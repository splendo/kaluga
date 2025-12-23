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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, B> * Div<C, A> -> Div<Mul<A, C>, B>

fun <
    LeftNumeratorLeftAndRightAndRightDenominatorQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<RightNumeratorQuantity>,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightAndRightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
        LeftNumeratorLeftUnit,
        RightNumeratorQuantity,
        RightNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            RightNumeratorQuantity,
            >,
        TargetNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
                RightNumeratorQuantity,
                >,
            LeftDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithNumeratorRootAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            LeftNumeratorLeftAndRightAndRightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorLeftUnitXRightNumeratorUnit: LeftNumeratorLeftUnit.(RightNumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerLeftDenominatorUnit: TargetNumeratorUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.leftNumeratorLeftUnitXRightNumeratorUnit(
    right.unit.numerator,
).targetNumeratorUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
