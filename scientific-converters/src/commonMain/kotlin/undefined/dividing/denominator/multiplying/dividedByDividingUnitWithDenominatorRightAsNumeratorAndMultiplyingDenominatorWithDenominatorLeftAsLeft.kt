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

// Div<A, Mul<B, C>> / Div<C, Mul<B, D>> -> Div<Mul<A, D>, Mul<C, C>>

fun <
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorQuantity>,
    NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorNumeratorQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorNumeratorQuantity>,
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
        NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
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
        NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
        NumeratorDenominatorRightUnit,
        NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
        NumeratorDenominatorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
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
                NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
                NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithDenominatorRightAsNumeratorAndMultiplyingDenominatorWithDenominatorLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorDenominatorRightAndDenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorDenominatorRightUnit: NumeratorNumeratorUnit.(DenominatorDenominatorRightUnit) -> TargetNumeratorUnit,
    numeratorDenominatorRightUnitXNumeratorDenominatorRightUnit: NumeratorDenominatorRightUnit.(NumeratorDenominatorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorRightUnit(
    right.unit.denominator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.right.numeratorDenominatorRightUnitXNumeratorDenominatorRightUnit(
        unit.denominator.right,
    ),
).byDividing(this, right, factory)
