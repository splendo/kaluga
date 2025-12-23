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
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

// A! / Inv<Mul<Ex<A>, B>> -> Mul<Mul<Wr<A>, Ex<A>>, B>

fun <
    NumeratorAndDenominatorReciprocalLeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorReciprocalLeftQuantity>,
    ExtendedDenominatorReciprocalLeftUnit,
    DenominatorReciprocalRightQuantity : UndefinedQuantityType,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit,
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftQuantity,
                >,
            DenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
        NumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftQuantity,
            >,
        WrappedNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftQuantity,
                >,
            >,
        TargetLeftUnit,
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftQuantity,
                    >,
                >,
            DenominatorReciprocalRightQuantity,
            >,
        TargetUnit,
        >,
    > ScientificValue<NumeratorAndDenominatorReciprocalLeftQuantity, NumeratorUnit>.dividedByReciprocalMultiplyingWithSelfAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftQuantity,
                    >,
                DenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXExtendedDenominatorReciprocalLeftUnit: WrappedNumeratorUnit.(ExtendedDenominatorReciprocalLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXDenominatorReciprocalRightUnit: TargetLeftUnit.(DenominatorReciprocalRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedDenominatorReciprocalLeftUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorReciprocalLeftQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftQuantity,
                >,
            > =
    unit.numeratorAsUndefined().wrappedNumeratorUnitXExtendedDenominatorReciprocalLeftUnit(
        right.unit.inverse.left,
    ).targetLeftUnitXDenominatorReciprocalRightUnit(
        right.unit.inverse.right,
    ).byDividing(this, right, factory)
