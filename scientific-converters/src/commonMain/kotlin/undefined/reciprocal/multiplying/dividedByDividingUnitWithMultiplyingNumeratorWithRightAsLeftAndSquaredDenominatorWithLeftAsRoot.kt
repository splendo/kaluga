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

// Inv<Mul<A, B>> / Div<Mul<B, C>, Mul<A, A>> -> Div<A, Mul<Mul<B, B>, C>>

fun <
    NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity>,
    NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightQuantity : UndefinedQuantityType,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
        NumeratorReciprocalRightUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
        NumeratorReciprocalRightUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            >,
        TargetDenominatorLeftUnit,
        DenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
        NumeratorReciprocalLeftUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
                >,
            DenominatorNumeratorRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
                    NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
                    >,
                DenominatorNumeratorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithRightAsLeftAndSquaredDenominatorWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
                NumeratorReciprocalLeftAndDenominatorDenominatorLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalRightUnitXNumeratorReciprocalRightUnit: NumeratorReciprocalRightUnit.(NumeratorReciprocalRightUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXDenominatorNumeratorRightUnit: TargetDenominatorLeftUnit.(DenominatorNumeratorRightUnit) -> TargetDenominatorUnit,
    numeratorReciprocalLeftUnitPerTargetDenominatorUnit: NumeratorReciprocalLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.left.numeratorReciprocalLeftUnitPerTargetDenominatorUnit(
    unit.inverse.right.numeratorReciprocalRightUnitXNumeratorReciprocalRightUnit(
        unit.inverse.right,
    ).targetDenominatorLeftUnitXDenominatorNumeratorRightUnit(
        right.unit.numerator.right,
    ),
).byDividing(this, right, factory)
