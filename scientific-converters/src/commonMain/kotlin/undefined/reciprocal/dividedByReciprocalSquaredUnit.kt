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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<B> / Inv<Mul<A, A>> -> Div<Mul<A, A>, B>

fun <
    NumeratorReciprocalQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalQuantity>,
    NumeratorUnit : UndefinedReciprocalUnit<
        NumeratorReciprocalQuantity,
        NumeratorReciprocalUnit,
        >,
    DenominatorReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorReciprocalLeftAndRightQuantity,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorReciprocalLeftAndRightQuantity,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        NumeratorReciprocalQuantity,
        NumeratorReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                DenominatorReciprocalLeftAndRightQuantity,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            NumeratorReciprocalQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        NumeratorReciprocalQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                DenominatorReciprocalLeftAndRightQuantity,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorReciprocalUnitPerNumeratorReciprocalUnit: DenominatorReciprocalUnit.(NumeratorReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.inverse.denominatorReciprocalUnitPerNumeratorReciprocalUnit(
    unit.inverse,
).byDividing(this, right, factory)
