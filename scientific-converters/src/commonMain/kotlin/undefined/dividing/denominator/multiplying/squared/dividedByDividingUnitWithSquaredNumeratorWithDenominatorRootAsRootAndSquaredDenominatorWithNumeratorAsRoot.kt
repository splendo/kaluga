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

// Div<A, Mul<B, B>> / Div<Mul<B, B>, Mul<A, A>> -> Div<Mul<Mul<A, A>, A>, Mul<Mul<B, B>, Mul<B, B>>>

fun <
    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithDenominatorRootAsRootAndSquaredDenominatorWithNumeratorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorDenominatorUnitXNumeratorNumeratorUnit: DenominatorDenominatorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXNumeratorDenominatorUnit: NumeratorDenominatorUnit.(NumeratorDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorUnit(
        unit.denominator,
    ),
).byDividing(this, right, factory)
