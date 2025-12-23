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

// Div<Mul<A, B>, C> / Div<Mul<A, A>, Mul<B, B>> -> Div<Mul<Mul<B, B>, B>, Mul<C, A>>

fun <
    NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
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
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
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
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorQuantity,
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                    NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                    >,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorQuantity,
                NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithNumeratorLeftAsRootAndSquaredDenominatorWithNumeratorRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorNumeratorLeftAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorNumeratorRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorDenominatorUnitXNumeratorNumeratorRightUnit: DenominatorDenominatorUnit.(NumeratorNumeratorRightUnit) -> TargetNumeratorUnit,
    numeratorDenominatorUnitXNumeratorNumeratorLeftUnit: NumeratorDenominatorUnit.(NumeratorNumeratorLeftUnit) -> TargetDenominatorUnit,
    targetNumeratorUnitPerTargetDenominatorUnit: TargetNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitXNumeratorNumeratorRightUnit(
    unit.numerator.right,
).targetNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.numeratorDenominatorUnitXNumeratorNumeratorLeftUnit(
        unit.numerator.left,
    ),
).byDividing(this, right, factory)
