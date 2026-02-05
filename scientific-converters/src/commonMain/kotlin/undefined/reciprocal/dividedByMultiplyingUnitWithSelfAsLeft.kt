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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<A> / Mul<A, B> -> Inv<Mul<Mul<A, A>, B>>

fun <
    NumeratorReciprocalAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalAndDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedReciprocalUnit<
        NumeratorReciprocalAndDenominatorLeftQuantity,
        NumeratorReciprocalUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalAndDenominatorLeftQuantity>,
    DenominatorRightQuantity : UndefinedQuantityType,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<DenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalAndDenominatorLeftQuantity,
        NumeratorReciprocalUnit,
        NumeratorReciprocalAndDenominatorLeftQuantity,
        NumeratorReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalAndDenominatorLeftQuantity,
            NumeratorReciprocalAndDenominatorLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        DenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalAndDenominatorLeftQuantity,
                NumeratorReciprocalAndDenominatorLeftQuantity,
                >,
            DenominatorRightQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalAndDenominatorLeftQuantity,
                    NumeratorReciprocalAndDenominatorLeftQuantity,
                    >,
                DenominatorRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        NumeratorReciprocalAndDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalAndDenominatorLeftQuantity,
            DenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXNumeratorReciprocalUnit: NumeratorReciprocalUnit.(NumeratorReciprocalUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXDenominatorRightUnit: TargetReciprocalLeftUnit.(DenominatorRightUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXNumeratorReciprocalUnit(
    unit.inverse,
).targetReciprocalLeftUnitXDenominatorRightUnit(
    right.unit.right,
).reciprocalTargetUnit().byDividing(this, right, factory)
