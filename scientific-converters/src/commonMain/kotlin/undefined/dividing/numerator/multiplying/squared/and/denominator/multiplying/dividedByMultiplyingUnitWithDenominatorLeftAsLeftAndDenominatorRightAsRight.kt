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

// Div<Mul<A, A>, Mul<B, C>> / Mul<B, C> -> Div<Mul<A, A>, Mul<Mul<B, C>, Mul<B, C>>>

fun <
    NumeratorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                NumeratorDenominatorRightAndDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                NumeratorDenominatorRightAndDenominatorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightQuantity,
                NumeratorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                    NumeratorDenominatorRightAndDenominatorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                    NumeratorDenominatorRightAndDenominatorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorLeftAsLeftAndDenominatorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXNumeratorDenominatorUnit: NumeratorDenominatorUnit.(NumeratorDenominatorUnit) -> TargetDenominatorUnit,
    numeratorNumeratorUnitPerTargetDenominatorUnit: NumeratorNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorUnit(
        unit.denominator,
    ),
).byDividing(this, right, factory)
