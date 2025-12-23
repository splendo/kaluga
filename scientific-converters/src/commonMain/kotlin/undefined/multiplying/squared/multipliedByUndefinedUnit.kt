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
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> * B -> Mul<Mul<A, A>, B>

fun <
    LeftLeftAndRightQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightQuantity>,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightQuantity,
        LeftLeftUnit,
        LeftLeftAndRightQuantity,
        LeftRightUnit,
        >,
    RightQuantity : UndefinedQuantityType,
    RightUnit : AbstractUndefinedScientificUnit<RightQuantity>,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightQuantity,
            LeftLeftAndRightQuantity,
            >,
        LeftUnit,
        RightQuantity,
        RightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightQuantity,
                LeftLeftAndRightQuantity,
                >,
            RightQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightQuantity,
        LeftLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByUndefinedUnit(
    right: UndefinedScientificValue<
        RightQuantity,
        RightUnit,
        >,
    leftUnitXRightUnit: LeftUnit.(RightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXRightUnit(
    right.unit,
).byMultiplying(this, right, factory)
