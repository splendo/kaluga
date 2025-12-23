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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, Mul<C, C>> / Mul<C, A> -> Div<B, Mul<Mul<C, C>, C>>

fun <
    NumeratorNumeratorLeftAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
    NumeratorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorRightQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorNumeratorLeftAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
                >,
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
                    NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
                    >,
                NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorRightQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorRootAsLeftAndNumeratorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXNumeratorDenominatorLeftUnit: NumeratorDenominatorUnit.(NumeratorDenominatorLeftUnit) -> TargetDenominatorUnit,
    numeratorNumeratorRightUnitPerTargetDenominatorUnit: NumeratorNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.numeratorNumeratorRightUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byDividing(this, right, factory)
