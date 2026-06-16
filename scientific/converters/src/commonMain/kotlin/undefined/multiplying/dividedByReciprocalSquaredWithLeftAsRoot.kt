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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<A, B> / Inv<Mul<A, A>> -> Mul<Mul<A, B>, Mul<A, A>>

fun <
    NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity>,
    NumeratorRightQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorLeftUnit,
        NumeratorRightQuantity,
        NumeratorRightUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorRightQuantity,
            >,
        NumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSquaredWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorLeftAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXDenominatorReciprocalUnit: NumeratorUnit.(DenominatorReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXDenominatorReciprocalUnit(
    right.unit.inverse,
).byDividing(this, right, factory)
