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

// Div<A, Mul<B, B>> / Div<C, Mul<A, A>> -> Div<Mul<Mul<A, A>, A>, Mul<Mul<B, B>, C>>

fun <
    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
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
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
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
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
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
                    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
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
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredDenominatorWithNumeratorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorDenominatorUnitXNumeratorNumeratorUnit: DenominatorDenominatorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXDenominatorNumeratorUnit: NumeratorDenominatorUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXDenominatorNumeratorUnit(
        right.unit.numerator,
    ),
).byDividing(this, right, factory)
