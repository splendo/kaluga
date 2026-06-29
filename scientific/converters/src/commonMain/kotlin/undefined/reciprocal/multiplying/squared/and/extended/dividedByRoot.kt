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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.squared.and.extended

import com.splendo.kaluga.base.decimal.Decimal
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

// Inv<Mul<Ex<A>, Ex<A>>> / A! -> Inv<Mul<Mul<Ex<A>, Ex<A>>, Wr<A>>>

fun <
    ExtendedNumeratorReciprocalLeftUnit,
    ExtendedNumeratorReciprocalRightUnit,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalLeftUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            >,
        NumeratorReciprocalUnit,
        >,
    NumeratorReciprocalLeftAndRightAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit : DefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorQuantity>,
    WrappedDenominatorUnit : WrappedUndefinedExtendedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
        DenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Extended<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        WrappedDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                    >,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    UndefinedQuantityType.Extended<
                        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                        >,
                    UndefinedQuantityType.Extended<
                        NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                        >,
                    >,
                UndefinedQuantityType.Extended<
                    NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            >,
        >,
    NumeratorUnit,
    >.dividedByRoot(
    right: ScientificValue<NumeratorReciprocalLeftAndRightAndDenominatorQuantity, DenominatorUnit>,
    denominatorAsUndefined: DenominatorUnit.() -> WrappedDenominatorUnit,
    numeratorReciprocalUnitXWrappedDenominatorUnit: NumeratorReciprocalUnit.(WrappedDenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedNumeratorReciprocalLeftUnit : UndefinedExtendedUnit<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            >,
        ExtendedNumeratorReciprocalRightUnit : UndefinedExtendedUnit<
            NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
            >,
        ExtendedNumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                NumeratorReciprocalLeftAndRightAndDenominatorQuantity,
                >,
            > =
    unit.inverse.numeratorReciprocalUnitXWrappedDenominatorUnit(
        right.unit.denominatorAsUndefined(),
    ).reciprocalTargetUnit().byDividing(this, right, factory)
