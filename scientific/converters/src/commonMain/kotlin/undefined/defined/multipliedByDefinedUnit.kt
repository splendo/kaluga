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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// A! * B! -> Mul<Wr<A>, Wr<B>>

fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<RightQuantity>,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftQuantity,
        LeftUnit,
        >,
    WrappedRightUnit : WrappedUndefinedExtendedUnit<
        RightQuantity,
        RightUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftQuantity,
            >,
        WrappedLeftUnit,
        UndefinedQuantityType.Extended<
            RightQuantity,
            >,
        WrappedRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                RightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.multipliedByDefinedUnit(
    right: ScientificValue<RightQuantity, RightUnit>,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    rightAsUndefined: RightUnit.() -> WrappedRightUnit,
    wrappedLeftUnitXWrappedRightUnit: WrappedLeftUnit.(WrappedRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftAsUndefined().wrappedLeftUnitXWrappedRightUnit(
    right.unit.rightAsUndefined(),
).byMultiplying(this, right, factory)
