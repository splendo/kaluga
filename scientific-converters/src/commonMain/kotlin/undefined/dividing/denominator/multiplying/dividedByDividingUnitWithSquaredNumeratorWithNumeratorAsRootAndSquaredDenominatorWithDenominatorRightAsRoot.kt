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

// Div<A, Mul<C, B>> / Div<Mul<A, A>, Mul<B, B>> -> Div<B, Mul<C, A>>

fun <
    NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftQuantity,
                NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithNumeratorAsRootAndSquaredDenominatorWithDenominatorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorLeftUnitXNumeratorNumeratorUnit: NumeratorDenominatorLeftUnit.(NumeratorNumeratorUnit) -> TargetDenominatorUnit,
    numeratorDenominatorRightUnitPerTargetDenominatorUnit: NumeratorDenominatorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.right.numeratorDenominatorRightUnitPerTargetDenominatorUnit(
    unit.denominator.left.numeratorDenominatorLeftUnitXNumeratorNumeratorUnit(
        unit.numerator,
    ),
).byDividing(this, right, factory)
