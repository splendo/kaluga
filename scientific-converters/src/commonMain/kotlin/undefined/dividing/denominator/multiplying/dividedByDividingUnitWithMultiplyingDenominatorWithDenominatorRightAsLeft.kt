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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, Mul<B, C>> / Div<D, Mul<C, E>> -> Div<Mul<A, E>, Mul<B, D>>

fun <
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorQuantity>,
    NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightQuantity : UndefinedQuantityType,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            DenominatorNumeratorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftQuantity,
                DenominatorNumeratorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingDenominatorWithDenominatorRightAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorRightAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorDenominatorRightUnit: NumeratorNumeratorUnit.(DenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
    numeratorDenominatorLeftUnitXDenominatorNumeratorUnit: NumeratorDenominatorLeftUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorRightUnit(
    right.unit.denominator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.left.numeratorDenominatorLeftUnitXDenominatorNumeratorUnit(
        right.unit.numerator,
    ),
).byDividing(this, right, factory)
