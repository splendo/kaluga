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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, C> / Div<Mul<C, D>, Mul<B, B>> -> Div<Mul<Mul<A, B>, Mul<B, B>>, Mul<Mul<C, C>, D>>

fun <
    NumeratorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftQuantity>,
    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorNumeratorLeftQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightQuantity : UndefinedQuantityType,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
        NumeratorDenominatorUnit,
        NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
            NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
                NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
                >,
            DenominatorNumeratorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftQuantity,
                    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
                    NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
                    >,
                DenominatorNumeratorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithDenominatorAsLeftAndSquaredDenominatorWithNumeratorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorDenominatorUnit: NumeratorNumeratorUnit.(DenominatorDenominatorUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXNumeratorDenominatorUnit: NumeratorDenominatorUnit.(NumeratorDenominatorUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXDenominatorNumeratorRightUnit: TargetDenominatorLeftUnit.(DenominatorNumeratorRightUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorDenominatorUnit(
    right.unit.denominator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorUnit(
        unit.denominator,
    ).targetDenominatorLeftUnitXDenominatorNumeratorRightUnit(
        right.unit.numerator.right,
    ),
).byDividing(this, right, factory)
