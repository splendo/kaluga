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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> / Div<Mul<C, A>, Mul<B, B>> -> Div<B, Mul<Mul<A, C>, A>>

fun <
    NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity>,
    NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorNumeratorLeftQuantity,
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            DenominatorNumeratorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                DenominatorNumeratorLeftQuantity,
                >,
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                    DenominatorNumeratorLeftQuantity,
                    >,
                NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithLeftAsRightAndSquaredDenominatorWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorReciprocalLeftAndDenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalLeftUnitXDenominatorNumeratorLeftUnit: NumeratorReciprocalLeftUnit.(DenominatorNumeratorLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXNumeratorReciprocalLeftUnit: TargetDenominatorLeftUnit.(NumeratorReciprocalLeftUnit) -> TargetDenominatorUnit,
    numeratorReciprocalRightUnitPerTargetDenominatorUnit: NumeratorReciprocalRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.right.numeratorReciprocalRightUnitPerTargetDenominatorUnit(
    unit.inverse.left.numeratorReciprocalLeftUnitXDenominatorNumeratorLeftUnit(
        right.unit.numerator.left,
    ).targetDenominatorLeftUnitXNumeratorReciprocalLeftUnit(
        unit.inverse.left,
    ),
).byDividing(this, right, factory)
