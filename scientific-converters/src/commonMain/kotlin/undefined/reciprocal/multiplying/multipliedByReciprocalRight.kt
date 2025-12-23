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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> * Inv<B> -> Inv<Mul<Mul<B, A>, B>>

fun <
    LeftReciprocalLeftQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftQuantity>,
    LeftReciprocalRightAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightAndRightReciprocalQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightReciprocalQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftReciprocalRightAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    TargetReciprocalLeftUnit : UndefinedMultipliedUnit<
        LeftReciprocalRightAndRightReciprocalQuantity,
        LeftReciprocalRightUnit,
        LeftReciprocalLeftQuantity,
        LeftReciprocalLeftUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalRightAndRightReciprocalQuantity,
            LeftReciprocalLeftQuantity,
            >,
        TargetReciprocalLeftUnit,
        LeftReciprocalRightAndRightReciprocalQuantity,
        LeftReciprocalRightUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalRightAndRightReciprocalQuantity,
                LeftReciprocalLeftQuantity,
                >,
            LeftReciprocalRightAndRightReciprocalQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalRightAndRightReciprocalQuantity,
                    LeftReciprocalLeftQuantity,
                    >,
                LeftReciprocalRightAndRightReciprocalQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftQuantity,
            LeftReciprocalRightAndRightReciprocalQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftReciprocalRightAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalRightUnitXLeftReciprocalLeftUnit: LeftReciprocalRightUnit.(LeftReciprocalLeftUnit) -> TargetReciprocalLeftUnit,
    targetReciprocalLeftUnitXLeftReciprocalRightUnit: TargetReciprocalLeftUnit.(LeftReciprocalRightUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.right.leftReciprocalRightUnitXLeftReciprocalLeftUnit(
    unit.inverse.left,
).targetReciprocalLeftUnitXLeftReciprocalRightUnit(
    unit.inverse.right,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
