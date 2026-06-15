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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// B! * Inv<Mul<A, A>> -> Div<Wr<B>, Mul<A, A>>

fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftQuantity>,
    RightReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    RightReciprocalLeftUnit : AbstractUndefinedScientificUnit<RightReciprocalLeftAndRightQuantity>,
    RightReciprocalRightUnit : AbstractUndefinedScientificUnit<RightReciprocalLeftAndRightQuantity>,
    RightReciprocalUnit : UndefinedMultipliedUnit<
        RightReciprocalLeftAndRightQuantity,
        RightReciprocalLeftUnit,
        RightReciprocalLeftAndRightQuantity,
        RightReciprocalRightUnit,
        >,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            RightReciprocalLeftAndRightQuantity,
            RightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    WrappedLeftUnit : WrappedUndefinedExtendedUnit<
        LeftQuantity,
        LeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<
            LeftQuantity,
            >,
        WrappedLeftUnit,
        UndefinedQuantityType.Multiplying<
            RightReciprocalLeftAndRightQuantity,
            RightReciprocalLeftAndRightQuantity,
            >,
        RightReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<
                LeftQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                RightReciprocalLeftAndRightQuantity,
                RightReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftQuantity, LeftUnit>.multipliedByReciprocalSquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                RightReciprocalLeftAndRightQuantity,
                RightReciprocalLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    leftAsUndefined: LeftUnit.() -> WrappedLeftUnit,
    wrappedLeftUnitPerRightReciprocalUnit: WrappedLeftUnit.(RightReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftAsUndefined().wrappedLeftUnitPerRightReciprocalUnit(
    right.unit.inverse,
).byMultiplying(this, right, factory)
