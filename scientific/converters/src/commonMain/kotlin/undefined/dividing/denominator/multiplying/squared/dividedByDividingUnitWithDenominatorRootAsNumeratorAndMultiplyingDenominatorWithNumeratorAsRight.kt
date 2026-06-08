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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, Mul<B, B>> / Div<B, Mul<C, A>> -> Div<Mul<Mul<A, C>, A>, Mul<Mul<B, B>, B>>

fun <
    NumeratorNumeratorAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorRightQuantity>,
    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            DenominatorDenominatorLeftQuantity,
            NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorUnit,
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
            DenominatorDenominatorLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorDenominatorUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
                DenominatorDenominatorLeftQuantity,
                >,
            NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
                    DenominatorDenominatorLeftQuantity,
                    >,
                NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
                    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
                    >,
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithDenominatorRootAsNumeratorAndMultiplyingDenominatorWithNumeratorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                DenominatorDenominatorLeftQuantity,
                NumeratorNumeratorAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorDenominatorLeftUnit: NumeratorNumeratorUnit.(DenominatorDenominatorLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXNumeratorNumeratorUnit: TargetNumeratorLeftUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXNumeratorDenominatorLeftUnit: NumeratorDenominatorUnit.(NumeratorDenominatorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorLeftUnit(
    right.unit.denominator.left,
).targetNumeratorLeftUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byDividing(this, right, factory)
