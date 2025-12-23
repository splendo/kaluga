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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.right.and.extended

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

// Inv<Mul<A, Ex<B>>> / B! -> Inv<Mul<Mul<A, Ex<B>>, Wr<B>>>

fun <
    NumeratorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftQuantity>,
    ExtendedNumeratorReciprocalRightUnit : UndefinedExtendedUnit<
        NumeratorReciprocalRightAndDenominatorQuantity,
        >,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftQuantity,
        NumeratorReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftQuantity,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalRightAndDenominatorQuantity,
                >,
            >,
        NumeratorReciprocalUnit,
        >,
    NumeratorReciprocalRightAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorReciprocalRightAndDenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
    NumeratorReciprocalRightAndDenominatorQuantity,
    DenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftQuantity,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalRightAndDenominatorQuantity,
                >,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalRightAndDenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftQuantity,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalRightAndDenominatorQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalRightAndDenominatorQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftQuantity,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalRightAndDenominatorQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalRightAndDenominatorQuantity,
                >,
            >,
        >,
    TargetUnit,
    >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftQuantity,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalRightAndDenominatorQuantity,
                >,
            >,
        >,
    NumeratorUnit,
    >.dividedByRight(
    right: ScientificValue<NumeratorReciprocalRightAndDenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    numeratorReciprocalUnitXWrappedDenominatorUnit: NumeratorReciprocalUnit.(WrappedDenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.numeratorReciprocalUnitXWrappedDenominatorUnit(
    right.unit.denominatorAsUndefined(),
).reciprocalTargetUnit().byDividing(this, right, factory)
