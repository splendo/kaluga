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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// B! / Inv<Mul<A, A>> -> Mul<Mul<Wr<B>, A>, A>

fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorQuantity>,
    DenominatorReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorReciprocalLeftAndRightQuantity,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
        NumeratorQuantity,
        NumeratorUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorQuantity,
            >,
        WrappedNumeratorUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorQuantity,
                >,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        TargetLeftUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorQuantity,
                    >,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        TargetUnit,
        >,
    > ScientificValue<NumeratorQuantity, NumeratorUnit>.dividedByReciprocalSquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                DenominatorReciprocalLeftAndRightQuantity,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXDenominatorReciprocalLeftUnit: WrappedNumeratorUnit.(DenominatorReciprocalLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXDenominatorReciprocalLeftUnit: TargetLeftUnit.(DenominatorReciprocalLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorAsUndefined().wrappedNumeratorUnitXDenominatorReciprocalLeftUnit(
    right.unit.inverse.left,
).targetLeftUnitXDenominatorReciprocalLeftUnit(
    right.unit.inverse.left,
).byDividing(this, right, factory)
