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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<A> * Inv<A> -> Inv<Mul<A, A>>

fun <
    LeftReciprocalAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightReciprocalQuantity>,
    LeftUnit : UndefinedReciprocalUnit<
        LeftReciprocalAndRightReciprocalQuantity,
        LeftReciprocalUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftReciprocalAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalAndRightReciprocalQuantity,
        LeftReciprocalUnit,
        LeftReciprocalAndRightReciprocalQuantity,
        LeftReciprocalUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalAndRightReciprocalQuantity,
            LeftReciprocalAndRightReciprocalQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalAndRightReciprocalQuantity,
                LeftReciprocalAndRightReciprocalQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        LeftReciprocalAndRightReciprocalQuantity,
        >,
    LeftUnit,
    >.multipliedBySelf(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftReciprocalAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXLeftReciprocalUnit: LeftReciprocalUnit.(LeftReciprocalUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitXLeftReciprocalUnit(
    unit.inverse,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
