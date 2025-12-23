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
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, Mul<B, B>> / Inv<Mul<B, A>> -> Div<Mul<A, A>, B>

fun <
    NumeratorNumeratorAndDenominatorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalRightQuantity>,
    NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
        NumeratorNumeratorUnit,
        NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
            NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
        NumeratorDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
                NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
                >,
            NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithDenominatorRootAsLeftAndNumeratorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorAndDenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorUnit: NumeratorNumeratorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorLeftUnit: TargetNumeratorUnit.(NumeratorDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerNumeratorDenominatorLeftUnit(
    unit.denominator.left,
).byDividing(this, right, factory)
