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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, Mul<B, C>> / Div<A, B> -> Div<A, C>

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
    NumeratorDenominatorLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorQuantity>,
    NumeratorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorDenominatorLeftAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorRootAsNumeratorAndDenominatorLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorLeftUnitPerNumeratorDenominatorRightUnit: NumeratorNumeratorLeftUnit.(NumeratorDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.numeratorNumeratorLeftUnitPerNumeratorDenominatorRightUnit(
    unit.denominator.right,
).byDividing(this, right, factory)
