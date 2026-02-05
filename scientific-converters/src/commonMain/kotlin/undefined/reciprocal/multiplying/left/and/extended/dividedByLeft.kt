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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.left.and.extended

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

// Inv<Mul<Ex<A>, B>> / A! -> Inv<Mul<Mul<Ex<A>, B>, Wr<A>>>

fun <
    ExtendedNumeratorReciprocalLeftUnit,
    NumeratorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalLeftAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndDenominatorQuantity,
                >,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    NumeratorReciprocalLeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
        NumeratorReciprocalLeftAndDenominatorQuantity,
        DenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndDenominatorQuantity,
                >,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalLeftAndDenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalLeftAndDenominatorQuantity,
                    >,
                NumeratorReciprocalRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndDenominatorQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    UndefinedQuantityType.Extended<
                        NumeratorReciprocalLeftAndDenominatorQuantity,
                        >,
                    NumeratorReciprocalRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalLeftAndDenominatorQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndDenominatorQuantity,
                >,
            NumeratorReciprocalRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByLeft(
    right: ScientificValue<NumeratorReciprocalLeftAndDenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    numeratorReciprocalUnitXWrappedDenominatorUnit: NumeratorReciprocalUnit.(WrappedDenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedNumeratorReciprocalLeftUnit : UndefinedExtendedUnit<
            NumeratorReciprocalLeftAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndDenominatorQuantity,
                >,
            > =
    unit.inverse.numeratorReciprocalUnitXWrappedDenominatorUnit(
        right.unit.denominatorAsUndefined(),
    ).reciprocalTargetUnit().byDividing(this, right, factory)
