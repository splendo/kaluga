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

// Div<Mul<A, B>, Mul<C, D>> / Div<B, Mul<C, C>> -> Div<Mul<A, C>, D>

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
    NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftQuantity,
                NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            NumeratorDenominatorRightQuantity,
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
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorRightAsNumeratorAndSquaredDenominatorWithDenominatorLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorRightAndDenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorLeftUnitXNumeratorDenominatorLeftUnit: NumeratorNumeratorLeftUnit.(NumeratorDenominatorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorRightUnit: TargetNumeratorUnit.(NumeratorDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.numeratorNumeratorLeftUnitXNumeratorDenominatorLeftUnit(
    unit.denominator.left,
).targetNumeratorUnitPerNumeratorDenominatorRightUnit(
    unit.denominator.right,
).byDividing(this, right, factory)
