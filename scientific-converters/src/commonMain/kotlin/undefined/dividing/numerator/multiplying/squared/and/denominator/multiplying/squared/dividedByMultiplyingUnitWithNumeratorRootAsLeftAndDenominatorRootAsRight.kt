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

// Div<Mul<A, A>, Mul<B, B>> / Mul<A, B> -> Div<A, Mul<Mul<B, B>, B>>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
                >,
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
                    NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
                    >,
                NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithNumeratorRootAsLeftAndDenominatorRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorLeftQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXNumeratorDenominatorLeftUnit: NumeratorDenominatorUnit.(NumeratorDenominatorLeftUnit) -> TargetDenominatorUnit,
    numeratorNumeratorLeftUnitPerTargetDenominatorUnit: NumeratorNumeratorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.left.numeratorNumeratorLeftUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byDividing(this, right, factory)
