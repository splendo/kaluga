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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, B>, Mul<C, C>> / Inv<Mul<A, B>> -> Div<Mul<Mul<A, B>, Mul<A, B>>, Mul<C, C>>

fun <
    NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity>,
    NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
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
            NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
            >,
        NumeratorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
                    NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
                    NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithNumeratorLeftAsLeftAndNumeratorRightAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorRightAndDenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorUnit: NumeratorNumeratorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorUnit: TargetNumeratorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerNumeratorDenominatorUnit(
    unit.denominator,
).byDividing(this, right, factory)
