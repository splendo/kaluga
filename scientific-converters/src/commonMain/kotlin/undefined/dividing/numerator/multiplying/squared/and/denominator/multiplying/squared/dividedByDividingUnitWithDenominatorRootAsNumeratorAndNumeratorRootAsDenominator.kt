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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<Mul<A, A>, Mul<B, B>> / Div<B, A> -> Div<Mul<Mul<A, A>, A>, Mul<Mul<B, B>, B>>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorNumeratorRightUnit,
        >,
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
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorNumeratorLeftUnit,
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
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                >,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
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
                    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                    NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                    >,
                NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
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
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithDenominatorRootAsNumeratorAndNumeratorRootAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorLeftUnit: NumeratorNumeratorUnit.(NumeratorNumeratorLeftUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXNumeratorDenominatorLeftUnit: NumeratorDenominatorUnit.(NumeratorDenominatorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorLeftUnit(
        unit.denominator.left,
    ),
).byDividing(this, right, factory)
