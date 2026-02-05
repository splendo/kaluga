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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> / Mul<B, A> -> Div<A, B>

fun <
    NumeratorLeftAndRightAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorRightQuantity>,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorRightQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightAndDenominatorRightQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndRightAndDenominatorRightQuantity,
        NumeratorRightUnit,
        >,
    DenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorLeftAndRightAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorLeftAndRightAndDenominatorRightQuantity,
        NumeratorLeftUnit,
        DenominatorLeftQuantity,
        DenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorLeftAndRightAndDenominatorRightQuantity,
            DenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndRightAndDenominatorRightQuantity,
        NumeratorLeftAndRightAndDenominatorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            DenominatorLeftQuantity,
            NumeratorLeftAndRightAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorLeftUnitPerDenominatorLeftUnit: NumeratorLeftUnit.(DenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.numeratorLeftUnitPerDenominatorLeftUnit(
    right.unit.left,
).byDividing(this, right, factory)
