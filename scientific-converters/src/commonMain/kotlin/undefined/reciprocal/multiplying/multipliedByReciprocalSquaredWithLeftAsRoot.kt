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

// Inv<Mul<A, B>> * Inv<Mul<A, A>> -> Inv<Mul<Mul<A, B>, Mul<A, A>>>

fun <
    LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity>,
    LeftReciprocalRightQuantity : UndefinedQuantityType,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        LeftReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                LeftReciprocalRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                    LeftReciprocalRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                    LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
            LeftReciprocalRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalSquaredWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                LeftReciprocalLeftAndRightReciprocalLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftReciprocalUnitXRightReciprocalUnit: LeftReciprocalUnit.(RightReciprocalUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitXRightReciprocalUnit(
    right.unit.inverse,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
