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

// Div<Mul<A, A>, Mul<B, C>> / Div<Mul<D, C>, Mul<B, E>> -> Div<Mul<Mul<A, A>, E>, Mul<Mul<C, D>, C>>

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
    NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightQuantity : UndefinedQuantityType,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorNumeratorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
        NumeratorDenominatorRightUnit,
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
            DenominatorNumeratorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightQuantity,
                NumeratorNumeratorLeftAndRightQuantity,
                >,
            DenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
                DenominatorNumeratorLeftQuantity,
                >,
            NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightQuantity,
                    NumeratorNumeratorLeftAndRightQuantity,
                    >,
                DenominatorDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
                    DenominatorNumeratorLeftQuantity,
                    >,
                NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
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
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithDenominatorRightAsRightAndMultiplyingDenominatorWithDenominatorLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorDenominatorRightAndDenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorDenominatorRightUnit: NumeratorNumeratorUnit.(DenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
    numeratorDenominatorRightUnitXDenominatorNumeratorLeftUnit: NumeratorDenominatorRightUnit.(DenominatorNumeratorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXNumeratorDenominatorRightUnit: TargetDenominatorLeftUnit.(NumeratorDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorRightUnit(
    right.unit.denominator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.right.numeratorDenominatorRightUnitXDenominatorNumeratorLeftUnit(
        right.unit.numerator.left,
    ).targetDenominatorLeftUnitXNumeratorDenominatorRightUnit(
        unit.denominator.right,
    ),
).byDividing(this, right, factory)
