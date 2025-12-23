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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> / Mul<A, C> -> Inv<Mul<Mul<A, B>, Mul<A, C>>>

fun <
    NumeratorReciprocalLeftAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorLeftQuantity>,
    NumeratorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorLeftQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorLeftQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorLeftQuantity>,
    DenominatorRightQuantity : UndefinedQuantityType,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorLeftQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorLeftQuantity,
                NumeratorReciprocalRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorLeftQuantity,
                DenominatorRightQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorLeftQuantity,
                    NumeratorReciprocalRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorLeftQuantity,
                    DenominatorRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorLeftQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithLeftAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXDenominatorUnit: NumeratorReciprocalUnit.(DenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXDenominatorUnit(
    right.unit,
).reciprocalTargetUnit().byDividing(this, right, factory)
