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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> * Inv<A> -> Inv<Mul<Mul<A, A>, A>>

fun <
    LeftReciprocalLeftAndRightAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightReciprocalQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightReciprocalQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            >,
        LeftReciprocalUnit,
        LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
                LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
                >,
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
                    LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
                    >,
                LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftReciprocalLeftAndRightAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXLeftReciprocalLeftUnit: LeftReciprocalUnit.(LeftReciprocalLeftUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitXLeftReciprocalLeftUnit(
    unit.inverse.left,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
