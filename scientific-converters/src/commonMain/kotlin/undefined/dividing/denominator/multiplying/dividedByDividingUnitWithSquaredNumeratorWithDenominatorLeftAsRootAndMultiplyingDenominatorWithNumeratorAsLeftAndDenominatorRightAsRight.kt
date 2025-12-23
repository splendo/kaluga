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

// Div<A, Mul<B, C>> / Div<Mul<B, B>, Mul<A, C>> -> Div<Mul<A, A>, Mul<Mul<B, B>, B>>

fun <
    NumeratorNumeratorAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftQuantity>,
    NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorUnit,
        NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
            NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
                NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithDenominatorLeftAsRootAndMultiplyingDenominatorWithNumeratorAsLeftAndDenominatorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorLeftQuantity,
                NumeratorDenominatorRightAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorUnit: NumeratorNumeratorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    denominatorNumeratorUnitXNumeratorDenominatorLeftUnit: DenominatorNumeratorUnit.(NumeratorDenominatorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    right.unit.numerator.denominatorNumeratorUnitXNumeratorDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byDividing(this, right, factory)
