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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Inv<A> * Mul<Wr<B>, A> -> B!

fun <
    LeftReciprocalAndRightRightQuantity : UndefinedQuantityType,
    LeftReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightRightQuantity>,
    LeftUnit : UndefinedReciprocalUnit<
        LeftReciprocalAndRightRightQuantity,
        LeftReciprocalUnit,
        >,
    RightLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightLeftUnit : DefinedScientificUnit<RightLeftQuantity>,
    WrappedRightLeftUnit : WrappedUndefinedExtendedUnit<
        RightLeftQuantity,
        RightLeftUnit,
        >,
    RightRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightRightQuantity>,
    RightUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            RightLeftQuantity,
            >,
        WrappedRightLeftUnit,
        LeftReciprocalAndRightRightQuantity,
        RightRightUnit,
        >,
    RightLeftValue : ScientificValue<RightLeftQuantity, RightLeftUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        LeftReciprocalAndRightRightQuantity,
        >,
    LeftUnit,
    >.multipliedByMultiplyingUnitWithDefinedLeftAndSelfAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                RightLeftQuantity,
                >,
            LeftReciprocalAndRightRightQuantity,
            >,
        RightUnit,
        >,
    factory: (Decimal, RightLeftUnit) -> RightLeftValue,
) = right.unit.left.wrapped.byMultiplying(this, right, factory)
