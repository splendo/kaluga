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

// Inv<Mul<A, B>> / Div<Mul<B, B>, Mul<A, C>> -> Div<C, Mul<Mul<B, B>, B>>

fun <
    NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity>,
    NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity>,
    DenominatorDenominatorRightQuantity : UndefinedQuantityType,
    DenominatorDenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorRightQuantity>,
    DenominatorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
        DenominatorDenominatorLeftUnit,
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        >,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
            DenominatorDenominatorRightQuantity,
            >,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        DenominatorDenominatorRightQuantity,
        DenominatorDenominatorRightUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorDenominatorRightQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
            NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSquaredNumeratorWithRightAsRootAndMultiplyingDenominatorWithLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorDenominatorLeftQuantity,
                DenominatorDenominatorRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorNumeratorUnitXNumeratorReciprocalRightUnit: DenominatorNumeratorUnit.(NumeratorReciprocalRightUnit) -> TargetDenominatorUnit,
    denominatorDenominatorRightUnitPerTargetDenominatorUnit: DenominatorDenominatorRightUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.right.denominatorDenominatorRightUnitPerTargetDenominatorUnit(
    right.unit.numerator.denominatorNumeratorUnitXNumeratorReciprocalRightUnit(
        unit.inverse.right,
    ),
).byDividing(this, right, factory)
