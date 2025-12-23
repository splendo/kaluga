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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import kotlin.jvm.JvmName

// Inv<Wr<A>> / Inv<Mul<Ex<A>, Ex<A>>> -> A!

fun <
    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorReciprocalUnit : DefinedScientificUnit<NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity>,
    WrappedNumeratorReciprocalUnit : WrappedUndefinedExtendedUnit<
    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
    NumeratorReciprocalUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        WrappedNumeratorReciprocalUnit,
        >,
    ExtendedDenominatorReciprocalLeftUnit : UndefinedExtendedUnit<
        NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
        >,
    ExtendedDenominatorReciprocalRightUnit : UndefinedExtendedUnit<
        NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
        >,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorReciprocalUnit,
        >,
    NumeratorReciprocalValue : ScientificValue<NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity, NumeratorReciprocalUnit>,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSquaredWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                >,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorReciprocalUnit) -> NumeratorReciprocalValue,
) = unit.inverse.wrapped.byDividing(this, right, factory)
