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
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> * Mul<B, B> -> Div<Mul<B, B>, Mul<A, A>>

fun <
    LeftReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightQuantity,
            LeftReciprocalLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightLeftAndRightQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftAndRightQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<RightLeftAndRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftAndRightQuantity,
        RightLeftUnit,
        RightLeftAndRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            RightLeftAndRightQuantity,
            RightLeftAndRightQuantity,
            >,
        RightUnit,
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightQuantity,
            LeftReciprocalLeftAndRightQuantity,
            >,
        LeftReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                RightLeftAndRightQuantity,
                RightLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightQuantity,
                LeftReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightQuantity,
            LeftReciprocalLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedBySquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftAndRightQuantity,
            RightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    rightUnitPerLeftReciprocalUnit: RightUnit.(LeftReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.rightUnitPerLeftReciprocalUnit(
    unit.inverse,
).byMultiplying(this, right, factory)
