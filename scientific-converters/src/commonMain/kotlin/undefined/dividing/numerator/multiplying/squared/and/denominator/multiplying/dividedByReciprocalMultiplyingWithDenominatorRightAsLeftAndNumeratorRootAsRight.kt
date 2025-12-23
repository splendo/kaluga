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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared.and.denominator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, A>, Mul<C, B>> / Inv<Mul<B, A>> -> Div<Mul<Mul<A, A>, A>, C>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftQuantity>,
    NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
        NumeratorNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
                >,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorLeftQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
                    >,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
                >,
            NumeratorDenominatorLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftQuantity,
            NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithDenominatorRightAsLeftAndNumeratorRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorRightAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorLeftUnit: NumeratorNumeratorUnit.(NumeratorNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorLeftUnit: TargetNumeratorUnit.(NumeratorDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerNumeratorDenominatorLeftUnit(
    unit.denominator.left,
).byDividing(this, right, factory)
