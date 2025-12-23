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

// Div<Mul<A, A>, Mul<B, C>> / Inv<Mul<A, B>> -> Div<Mul<Mul<A, A>, A>, C>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity>,
    NumeratorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                >,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                    >,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                >,
            NumeratorDenominatorRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity,
            NumeratorDenominatorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithNumeratorRootAsLeftAndDenominatorLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                NumeratorDenominatorLeftAndDenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorLeftUnit: NumeratorNumeratorUnit.(NumeratorNumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorRightUnit: TargetNumeratorUnit.(NumeratorDenominatorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorLeftUnit(
    unit.numerator.left,
).targetNumeratorUnitPerNumeratorDenominatorRightUnit(
    unit.denominator.right,
).byDividing(this, right, factory)
