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

// A! / Inv<Ex<A>> -> Mul<Wr<A>, Ex<A>>

fun <
    NumeratorAndDenominatorReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorReciprocalQuantity>,
    ExtendedDenominatorReciprocalUnit,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalQuantity,
            >,
        ExtendedDenominatorReciprocalUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
        NumeratorAndDenominatorReciprocalQuantity,
        NumeratorUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalQuantity,
            >,
        WrappedNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalQuantity,
            >,
        ExtendedDenominatorReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<NumeratorAndDenominatorReciprocalQuantity, NumeratorUnit>.dividedByReciprocalSelf(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXExtendedDenominatorReciprocalUnit: WrappedNumeratorUnit.(ExtendedDenominatorReciprocalUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedDenominatorReciprocalUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorReciprocalQuantity,
            >,
        ExtendedDenominatorReciprocalUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalQuantity,
                >,
            > =
    unit.numeratorAsUndefined().wrappedNumeratorUnitXExtendedDenominatorReciprocalUnit(
        right.unit.inverse,
    ).byDividing(this, right, factory)
