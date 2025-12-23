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

// Div<Mul<A, B>, Mul<C, D>> / Mul<D, A> -> Div<B, Mul<Mul<C, D>, D>>

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
    NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorLeftQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorLeftQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorRightQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorRightAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorNumeratorLeftAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        NumeratorDenominatorRightAndDenominatorLeftQuantity,
        NumeratorDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftQuantity,
                NumeratorDenominatorRightAndDenominatorLeftQuantity,
                >,
            NumeratorDenominatorRightAndDenominatorLeftQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorNumeratorRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftQuantity,
                    NumeratorDenominatorRightAndDenominatorLeftQuantity,
                    >,
                NumeratorDenominatorRightAndDenominatorLeftQuantity,
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
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorRightAsLeftAndNumeratorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorRightAndDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXNumeratorDenominatorRightUnit: NumeratorDenominatorUnit.(NumeratorDenominatorRightUnit) -> TargetDenominatorUnit,
    numeratorNumeratorRightUnitPerTargetDenominatorUnit: NumeratorNumeratorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.numeratorNumeratorRightUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorRightUnit(
        unit.denominator.right,
    ),
).byDividing(this, right, factory)
