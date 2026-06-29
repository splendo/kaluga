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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.defined

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

// Inv<Wr<A>> * Mul<Ex<A>, Ex<A>> -> A!

fun <
    LeftReciprocalAndRightLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftReciprocalUnit : DefinedScientificUnit<LeftReciprocalAndRightLeftAndRightQuantity>,
    WrappedLeftReciprocalUnit : WrappedUndefinedExtendedUnit<
        LeftReciprocalAndRightLeftAndRightQuantity,
        LeftReciprocalUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Extended<
            LeftReciprocalAndRightLeftAndRightQuantity,
            >,
        WrappedLeftReciprocalUnit,
        >,
    ExtendedRightLeftUnit,
    ExtendedRightRightUnit,
    RightUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftReciprocalAndRightLeftAndRightQuantity,
            >,
        ExtendedRightLeftUnit,
        UndefinedQuantityType.Extended<
            LeftReciprocalAndRightLeftAndRightQuantity,
            >,
        ExtendedRightRightUnit,
        >,
    LeftReciprocalValue : ScientificValue<LeftReciprocalAndRightLeftAndRightQuantity, LeftReciprocalUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Extended<
            LeftReciprocalAndRightLeftAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedBySquaredUnitWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftReciprocalAndRightLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftReciprocalAndRightLeftAndRightQuantity,
                >,
            >,
        RightUnit,
        >,
    factory: (Decimal, LeftReciprocalUnit) -> LeftReciprocalValue,
) where
        ExtendedRightLeftUnit : UndefinedExtendedUnit<
            LeftReciprocalAndRightLeftAndRightQuantity,
            >,
        ExtendedRightLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftReciprocalAndRightLeftAndRightQuantity,
                >,
            >,
        ExtendedRightRightUnit : UndefinedExtendedUnit<
            LeftReciprocalAndRightLeftAndRightQuantity,
            >,
        ExtendedRightRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftReciprocalAndRightLeftAndRightQuantity,
                >,
            > =
    unit.inverse.wrapped.byMultiplying(this, right, factory)
