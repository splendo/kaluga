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

package com.splendo.kaluga.scientific.converter.undefined.dividing

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Div<A, B> / Div<Mul<C, B>, Mul<A, A>> -> Div<Mul<Mul<A, A>, A>, Mul<Mul<B, C>, B>>

fun <
    NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorNumeratorRightQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
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
            DenominatorNumeratorLeftQuantity,
            NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
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
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
        NumeratorDenominatorUnit,
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
            DenominatorNumeratorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
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
                NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
                DenominatorNumeratorLeftQuantity,
                >,
            NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
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
                    NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
                    DenominatorNumeratorLeftQuantity,
                    >,
                NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithDenominatorAsRightAndSquaredDenominatorWithNumeratorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorDenominatorAndDenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorDenominatorUnitXNumeratorNumeratorUnit: DenominatorDenominatorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXDenominatorNumeratorLeftUnit: NumeratorDenominatorUnit.(DenominatorNumeratorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXNumeratorDenominatorUnit: TargetDenominatorLeftUnit.(NumeratorDenominatorUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXDenominatorNumeratorLeftUnit(
        right.unit.numerator.left,
    ).targetDenominatorLeftUnitXNumeratorDenominatorUnit(
        unit.denominator,
    ),
).byDividing(this, right, factory)
