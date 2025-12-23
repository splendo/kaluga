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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<A, A> * Inv<Mul<B, A>> -> Div<A, B>

fun <
    LeftLeftAndRightAndRightReciprocalRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightReciprocalRightQuantity>,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightReciprocalRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightReciprocalRightQuantity,
        LeftLeftUnit,
        LeftLeftAndRightAndRightReciprocalRightQuantity,
        LeftRightUnit,
        >,
    RightReciprocalLeftQuantity : UndefinedQuantityType,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<RightReciprocalLeftQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightReciprocalRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        RightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        LeftLeftAndRightAndRightReciprocalRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            RightReciprocalLeftQuantity,
            LeftLeftAndRightAndRightReciprocalRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftLeftAndRightAndRightReciprocalRightQuantity,
        LeftLeftUnit,
        RightReciprocalLeftQuantity,
        RightReciprocalLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftLeftAndRightAndRightReciprocalRightQuantity,
            RightReciprocalLeftQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightAndRightReciprocalRightQuantity,
        LeftLeftAndRightAndRightReciprocalRightQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalMultiplyingWithRootAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                RightReciprocalLeftQuantity,
                LeftLeftAndRightAndRightReciprocalRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftLeftUnitPerRightReciprocalLeftUnit: LeftLeftUnit.(RightReciprocalLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.leftLeftUnitPerRightReciprocalLeftUnit(
    right.unit.inverse.left,
).byMultiplying(this, right, factory)
