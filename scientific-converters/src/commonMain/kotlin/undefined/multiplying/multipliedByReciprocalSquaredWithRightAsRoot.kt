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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Mul<B, A> * Inv<Mul<A, A>> -> Div<B, A>

fun <
    LeftLeftQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftQuantity>,
    LeftRightAndRightReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightReciprocalLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftQuantity,
        LeftLeftUnit,
        LeftRightAndRightReciprocalLeftAndRightQuantity,
        LeftRightUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftRightAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftRightAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        LeftRightAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightReciprocalLeftAndRightQuantity,
            LeftRightAndRightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftLeftQuantity,
        LeftLeftUnit,
        LeftRightAndRightReciprocalLeftAndRightQuantity,
        LeftRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftLeftQuantity,
            LeftRightAndRightReciprocalLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftQuantity,
        LeftRightAndRightReciprocalLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalSquaredWithRightAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightReciprocalLeftAndRightQuantity,
                LeftRightAndRightReciprocalLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftLeftUnitPerLeftRightUnit: LeftLeftUnit.(LeftRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.left.leftLeftUnitPerLeftRightUnit(
    unit.right,
).byMultiplying(this, right, factory)
