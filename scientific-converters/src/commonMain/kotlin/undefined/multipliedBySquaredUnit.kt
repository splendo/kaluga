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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// A * Mul<B, B> -> Mul<Mul<A, B>, B>

fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit : AbstractUndefinedScientificUnit<LeftQuantity>,
    RightLeftAndRightQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftAndRightQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<RightLeftAndRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftAndRightQuantity,
        RightLeftUnit,
        RightLeftAndRightQuantity,
        RightRightUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        LeftQuantity,
        LeftUnit,
        RightLeftAndRightQuantity,
        RightLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftQuantity,
            RightLeftAndRightQuantity,
            >,
        TargetLeftUnit,
        RightLeftAndRightQuantity,
        RightLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftQuantity,
                RightLeftAndRightQuantity,
                >,
            RightLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    LeftQuantity,
    LeftUnit,
    >.multipliedBySquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftAndRightQuantity,
            RightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXRightLeftUnit: LeftUnit.(RightLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXRightLeftUnit: TargetLeftUnit.(RightLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightLeftUnit(
    right.unit.left,
).targetLeftUnitXRightLeftUnit(
    right.unit.left,
).byMultiplying(this, right, factory)
