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

// Div<Mul<A, A>, Mul<B, C>> * Mul<B, C> -> Mul<A, A>

fun <
    LeftNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightQuantity>,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorLeftAndRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorLeftAndRightLeftQuantity : UndefinedQuantityType,
    LeftDenominatorLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightLeftQuantity>,
    LeftDenominatorRightAndRightRightQuantity : UndefinedQuantityType,
    LeftDenominatorRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightRightQuantity>,
    LeftDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightLeftQuantity,
        LeftDenominatorLeftUnit,
        LeftDenominatorRightAndRightRightQuantity,
        LeftDenominatorRightUnit,
        >,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightLeftQuantity,
            LeftDenominatorRightAndRightRightQuantity,
            >,
        LeftDenominatorUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftDenominatorLeftAndRightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftDenominatorRightAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftDenominatorLeftAndRightLeftQuantity,
        RightLeftUnit,
        LeftDenominatorRightAndRightRightQuantity,
        RightRightUnit,
        >,
    LeftNumeratorValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        LeftNumeratorUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightQuantity,
            LeftNumeratorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightLeftQuantity,
            LeftDenominatorRightAndRightRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithDenominatorLeftAsLeftAndDenominatorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftDenominatorLeftAndRightLeftQuantity,
            LeftDenominatorRightAndRightRightQuantity,
            >,
        RightUnit,
        >,
    factory: (Decimal, LeftNumeratorUnit) -> LeftNumeratorValue,
) = unit.numerator.byMultiplying(this, right, factory)
