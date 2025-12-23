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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<A, A> / Inv<B> -> Mul<Mul<A, A>, B>

fun <
    NumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightQuantity>,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndRightQuantity,
        NumeratorRightUnit,
        >,
    DenominatorReciprocalQuantity : UndefinedQuantityType,
    DenominatorReciprocalUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalQuantity>,
    DenominatorUnit : UndefinedReciprocalUnit<
        DenominatorReciprocalQuantity,
        DenominatorReciprocalUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndRightQuantity,
            NumeratorLeftAndRightQuantity,
            >,
        NumeratorUnit,
        DenominatorReciprocalQuantity,
        DenominatorReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndRightQuantity,
                NumeratorLeftAndRightQuantity,
                >,
            DenominatorReciprocalQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndRightQuantity,
        NumeratorLeftAndRightQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalUndefinedUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            DenominatorReciprocalQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXDenominatorReciprocalUnit: NumeratorUnit.(DenominatorReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXDenominatorReciprocalUnit(
    right.unit.inverse,
).byDividing(this, right, factory)
