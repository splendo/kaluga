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

// Inv<Mul<A, B>> / Div<Mul<B, C>, Mul<D, A>> -> Div<D, Mul<Mul<B, B>, C>>

fun <
    NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity>,
    NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity,
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
    DenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            DenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            DenominatorDenominatorLeftQuantity,
            NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity,
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
        DenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
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
            DenominatorDenominatorLeftQuantity,
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
            NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithRightAsLeftAndMultiplyingDenominatorWithLeftAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftQuantity,
                DenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                DenominatorDenominatorLeftQuantity,
                NumeratorReciprocalLeftAndDenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalRightUnitXNumeratorReciprocalRightUnit: NumeratorReciprocalRightUnit.(NumeratorReciprocalRightUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXDenominatorNumeratorRightUnit: TargetDenominatorLeftUnit.(DenominatorNumeratorRightUnit) -> TargetDenominatorUnit,
    denominatorDenominatorLeftUnitPerTargetDenominatorUnit: DenominatorDenominatorLeftUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.left.denominatorDenominatorLeftUnitPerTargetDenominatorUnit(
    unit.inverse.right.numeratorReciprocalRightUnitXNumeratorReciprocalRightUnit(
        unit.inverse.right,
    ).targetDenominatorLeftUnitXDenominatorNumeratorRightUnit(
        right.unit.numerator.right,
    ),
).byDividing(this, right, factory)
