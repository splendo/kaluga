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

// Div<Mul<A, A>, Mul<B, C>> / Div<Mul<B, D>, Mul<A, E>> -> Div<Mul<Mul<A, A>, Mul<A, E>>, Mul<Mul<B, C>, Mul<B, D>>>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity>,
    NumeratorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightQuantity : UndefinedQuantityType,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightQuantity : UndefinedQuantityType,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
                NumeratorDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                    DenominatorDenominatorRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
                    NumeratorDenominatorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
                    DenominatorNumeratorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithDenominatorLeftAsLeftAndMultiplyingDenominatorWithNumeratorRootAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorDenominatorUnit: NumeratorNumeratorUnit.(DenominatorDenominatorUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXDenominatorNumeratorUnit: NumeratorDenominatorUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorUnit(
    right.unit.denominator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXDenominatorNumeratorUnit(
        right.unit.numerator,
    ),
).byDividing(this, right, factory)
