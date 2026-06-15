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

// Mul<A, B> * Inv<Mul<A, A>> -> Div<B, A>

fun <
    LeftLeftAndRightReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightReciprocalLeftAndRightQuantity>,
    LeftRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightReciprocalLeftAndRightQuantity,
        LeftLeftUnit,
        LeftRightQuantity,
        LeftRightUnit,
        >,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightReciprocalLeftAndRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        LeftLeftAndRightReciprocalLeftAndRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightReciprocalLeftAndRightQuantity,
            LeftLeftAndRightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftRightQuantity,
        LeftRightUnit,
        LeftLeftAndRightReciprocalLeftAndRightQuantity,
        LeftLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftRightQuantity,
            LeftLeftAndRightReciprocalLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightReciprocalLeftAndRightQuantity,
        LeftRightQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalSquaredWithLeftAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightReciprocalLeftAndRightQuantity,
                LeftLeftAndRightReciprocalLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftRightUnitPerLeftLeftUnit: LeftRightUnit.(LeftLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.leftRightUnitPerLeftLeftUnit(
    unit.left,
).byMultiplying(this, right, factory)
