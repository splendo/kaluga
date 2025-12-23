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

// Div<Mul<A, B>, Mul<C, D>> / Mul<C, E> -> Div<Mul<A, B>, Mul<Mul<C, D>, Mul<C, E>>>

fun <
    NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
    NumeratorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorLeftQuantity>,
    NumeratorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorLeftQuantity>,
    DenominatorRightQuantity : UndefinedQuantityType,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                NumeratorDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                DenominatorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftQuantity,
                NumeratorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                    NumeratorDenominatorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorLeftQuantity,
                    DenominatorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXDenominatorUnit: NumeratorDenominatorUnit.(DenominatorUnit) -> TargetDenominatorUnit,
    numeratorNumeratorUnitPerTargetDenominatorUnit: NumeratorNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXDenominatorUnit(
        right.unit,
    ),
).byDividing(this, right, factory)
