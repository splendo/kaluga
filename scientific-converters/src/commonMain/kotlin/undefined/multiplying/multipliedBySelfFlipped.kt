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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, B> * Mul<B, A> -> Mul<Mul<A, B>, Mul<B, A>>

fun <
    LeftLeftAndRightRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightRightQuantity>,
    LeftRightAndRightLeftQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightLeftQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightRightQuantity,
        LeftLeftUnit,
        LeftRightAndRightLeftQuantity,
        LeftRightUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftRightAndRightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftRightAndRightLeftQuantity,
        RightLeftUnit,
        LeftLeftAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightRightQuantity,
            LeftRightAndRightLeftQuantity,
            >,
        LeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightLeftQuantity,
            LeftLeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightRightQuantity,
                LeftRightAndRightLeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftRightAndRightLeftQuantity,
                LeftLeftAndRightRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightRightQuantity,
        LeftRightAndRightLeftQuantity,
        >,
    LeftUnit,
    >.multipliedBySelfFlipped(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftRightAndRightLeftQuantity,
            LeftLeftAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightUnit: LeftUnit.(RightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightUnit(
    right.unit,
).byMultiplying(this, right, factory)
