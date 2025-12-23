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

// A! / Inv<Mul<Ex<A>, Ex<A>>> -> Mul<Mul<Wr<A>, Ex<A>>, Ex<A>>

fun <
    NumeratorAndDenominatorReciprocalLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : DefinedScientificUnit<NumeratorAndDenominatorReciprocalLeftAndRightQuantity>,
    ExtendedDenominatorReciprocalLeftUnit,
    ExtendedDenominatorReciprocalRightUnit,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorReciprocalUnit,
        >,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<
        NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorUnit,
        >,
    TargetLeftUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        WrappedNumeratorUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<NumeratorAndDenominatorReciprocalLeftAndRightQuantity, NumeratorUnit>.dividedByReciprocalSquaredWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorAsUndefined: NumeratorUnit.() -> WrappedNumeratorUnit,
    wrappedNumeratorUnitXExtendedDenominatorReciprocalLeftUnit: WrappedNumeratorUnit.(ExtendedDenominatorReciprocalLeftUnit) -> TargetLeftUnit,
    targetLeftUnitXExtendedDenominatorReciprocalLeftUnit: TargetLeftUnit.(ExtendedDenominatorReciprocalLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedDenominatorReciprocalLeftUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        ExtendedDenominatorReciprocalRightUnit : UndefinedExtendedUnit<
            NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        ExtendedDenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            > =
    unit.numeratorAsUndefined().wrappedNumeratorUnitXExtendedDenominatorReciprocalLeftUnit(
        right.unit.inverse.left,
    ).targetLeftUnitXExtendedDenominatorReciprocalLeftUnit(
        right.unit.inverse.left,
    ).byDividing(this, right, factory)
