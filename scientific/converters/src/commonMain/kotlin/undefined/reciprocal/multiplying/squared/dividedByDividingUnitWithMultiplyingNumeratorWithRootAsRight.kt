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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> / Div<Mul<B, A>, C> -> Div<C, Mul<Mul<A, A>, Mul<B, A>>>

fun <
    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity>,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftQuantity : UndefinedQuantityType,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorLeftQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        DenominatorNumeratorLeftQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorNumeratorLeftQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            DenominatorNumeratorLeftQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            >,
        DenominatorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorDenominatorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    DenominatorNumeratorLeftQuantity,
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithMultiplyingNumeratorWithRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorNumeratorLeftQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorRightQuantity,
                >,
            DenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXDenominatorNumeratorUnit: NumeratorReciprocalUnit.(DenominatorNumeratorUnit) -> TargetDenominatorUnit,
    denominatorDenominatorUnitPerTargetDenominatorUnit: DenominatorDenominatorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitPerTargetDenominatorUnit(
    unit.inverse.numeratorReciprocalUnitXDenominatorNumeratorUnit(
        right.unit.numerator,
    ),
).byDividing(this, right, factory)
