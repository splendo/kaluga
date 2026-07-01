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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared.and.left.and.extended.and.right.and.defined

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// Mul<Ex<A>, Wr<A>> * Inv<Ex<A>> -> A!

fun <
    ExtendedLeftLeftUnit,
    LeftLeftAndRightAndRightReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftRightUnit : DefinedScientificUnit<LeftLeftAndRightAndRightReciprocalQuantity>,
    WrappedLeftRightUnit : WrappedUndefinedExtendedUnit<
        LeftLeftAndRightAndRightReciprocalQuantity,
        LeftRightUnit,
        >,
    LeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        ExtendedLeftLeftUnit,
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        WrappedLeftRightUnit,
        >,
    ExtendedRightReciprocalUnit,
    RightUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        ExtendedRightReciprocalUnit,
        >,
    LeftRightValue : ScientificValue<LeftLeftAndRightAndRightReciprocalQuantity, LeftRightUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        UndefinedQuantityType.Extended<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByReciprocalLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightReciprocalQuantity,
                >,
            >,
        RightUnit,
        >,
    factory: (Decimal, LeftRightUnit) -> LeftRightValue,
) where
        ExtendedLeftLeftUnit : UndefinedExtendedUnit<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        ExtendedLeftLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightReciprocalQuantity,
                >,
            >,
        ExtendedRightReciprocalUnit : UndefinedExtendedUnit<
            LeftLeftAndRightAndRightReciprocalQuantity,
            >,
        ExtendedRightReciprocalUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftLeftAndRightAndRightReciprocalQuantity,
                >,
            > =
    unit.right.wrapped.byMultiplying(this, right, factory)
