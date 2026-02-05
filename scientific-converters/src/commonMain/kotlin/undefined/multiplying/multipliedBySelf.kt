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

// Mul<A, B> * Mul<A, B> -> Mul<Mul<A, B>, Mul<A, B>>

fun <
    LeftLeftAndRightLeftQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightLeftQuantity>,
    LeftRightAndRightRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightLeftQuantity,
        LeftLeftUnit,
        LeftRightAndRightRightQuantity,
        LeftRightUnit,
        >,
    RightLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightLeftQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftRightAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightLeftQuantity,
        RightLeftUnit,
        LeftRightAndRightRightQuantity,
        RightRightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightLeftQuantity,
            LeftRightAndRightRightQuantity,
            >,
        LeftUnit,
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightLeftQuantity,
            LeftRightAndRightRightQuantity,
            >,
        LeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightLeftQuantity,
                LeftRightAndRightRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightLeftQuantity,
                LeftRightAndRightRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightLeftQuantity,
        LeftRightAndRightRightQuantity,
        >,
    LeftUnit,
    >.multipliedBySelf(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightLeftQuantity,
            LeftRightAndRightRightQuantity,
            >,
        RightUnit,
        >,
    leftUnitXLeftUnit: LeftUnit.(LeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXLeftUnit(
    unit,
).byMultiplying(this, right, factory)
