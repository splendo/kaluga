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

// Mul<A, B> * A -> Mul<Mul<A, B>, A>

fun <
    LeftLeftAndRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightQuantity>,
    LeftRightQuantity : UndefinedQuantityType,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightQuantity,
        LeftLeftUnit,
        LeftRightQuantity,
        LeftRightUnit,
        >,
    RightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightQuantity>,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightQuantity,
            LeftRightQuantity,
            >,
        LeftUnit,
        LeftLeftAndRightQuantity,
        LeftLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightQuantity,
                LeftRightQuantity,
                >,
            LeftLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightQuantity,
        LeftRightQuantity,
        >,
    LeftUnit,
    >.multipliedByLeft(
    right: UndefinedScientificValue<
        LeftLeftAndRightQuantity,
        RightUnit,
        >,
    leftUnitXLeftLeftUnit: LeftUnit.(LeftLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXLeftLeftUnit(
    unit.left,
).byMultiplying(this, right, factory)
