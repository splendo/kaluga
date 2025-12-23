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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, Mul<B, B>> / Div<A, B> -> Div<A, B>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    DenominatorValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorRootAsNumeratorAndDenominatorRootAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, DenominatorUnit) -> DenominatorValue,
) = right.unit.byDividing(this, right, factory)
