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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, Mul<C, D>> / Div<B, D> -> Div<A, C>

fun <
    NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
    NumeratorNumeratorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorLeftQuantity,
            NumeratorDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorRightAsNumeratorAndDenominatorRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorLeftUnitPerNumeratorDenominatorLeftUnit: NumeratorNumeratorLeftUnit.(NumeratorDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.numeratorNumeratorLeftUnitPerNumeratorDenominatorLeftUnit(
    unit.denominator.left,
).byDividing(this, right, factory)
