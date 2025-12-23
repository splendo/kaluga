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

package com.splendo.kaluga.scientific.converter.undefined.defined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// A! * Mul<B, B> -> Mul<Mul<Wr<A>, B>, B>

fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftQuantity>,
    RightLeftAndRightQuantity : UndefinedQuantityType,
    RightLeftUnit : AbstractUndefinedScientificUnit<RightLeftAndRightQuantity>,
    RightRightUnit : AbstractUndefinedScientificUnit<RightLeftAndRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        RightLeftAndRightQuantity,
        RightLeftUnit,
        RightLeftAndRightQuantity,
        RightRightUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
    LeftQuantity,
    LeftUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftQuantity,
            >,
        WrappedLeftUnit,
        RightLeftAndRightQuantity,
        RightLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftQuantity,
                >,
            RightLeftAndRightQuantity,
            >,
        TargetLeftUnit,
        RightLeftAndRightQuantity,
        RightLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftQuantity,
                >,
            RightLeftAndRightQuantity,
            >,
        RightLeftAndRightQuantity,
        >,
    TargetUnit,
    >,
    > ScientificValue<LeftQuantity, LeftUnit>.multipliedBySquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            RightLeftAndRightQuantity,
            RightLeftAndRightQuantity,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitXRightLeftUnit: WrappedLeftUnit.(RightLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXRightLeftUnit: TargetLeftUnit.(RightLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftAsUndefined().wrappedLeftUnitXRightLeftUnit(
    right.unit.left,
).targetLeftUnitXRightLeftUnit(
    right.unit.left,
).byMultiplying(this, right, factory)
