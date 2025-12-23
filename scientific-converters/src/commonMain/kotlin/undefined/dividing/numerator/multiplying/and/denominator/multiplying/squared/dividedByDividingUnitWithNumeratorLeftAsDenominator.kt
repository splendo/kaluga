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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, B>, Mul<C, C>> / Div<D, A> -> Div<Mul<Mul<A, B>, A>, Mul<Mul<C, C>, D>>

fun <
    NumeratorNumeratorLeftAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorDenominatorQuantity>,
    NumeratorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
        NumeratorNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
                NumeratorNumeratorRightQuantity,
                >,
            NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightQuantity,
                >,
            DenominatorNumeratorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
                    NumeratorNumeratorRightQuantity,
                    >,
                NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndRightQuantity,
                    NumeratorDenominatorLeftAndRightQuantity,
                    >,
                DenominatorNumeratorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
            NumeratorNumeratorRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithNumeratorLeftAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            NumeratorNumeratorLeftAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorLeftUnit: NumeratorNumeratorUnit.(NumeratorNumeratorLeftUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXDenominatorNumeratorUnit: NumeratorDenominatorUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXDenominatorNumeratorUnit(
        right.unit.numerator,
    ),
).byDividing(this, right, factory)
