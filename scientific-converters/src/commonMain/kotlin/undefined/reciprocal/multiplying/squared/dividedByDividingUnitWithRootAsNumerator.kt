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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> / Div<A, B> -> Div<B, Mul<Mul<A, A>, A>>

fun <
    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity>,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        NumeratorReciprocalUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorDenominatorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
                    >,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithRootAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorQuantity,
            DenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXNumeratorReciprocalLeftUnit: NumeratorReciprocalUnit.(NumeratorReciprocalLeftUnit) -> TargetDenominatorUnit,
    denominatorDenominatorUnitPerTargetDenominatorUnit: DenominatorDenominatorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitPerTargetDenominatorUnit(
    unit.inverse.numeratorReciprocalUnitXNumeratorReciprocalLeftUnit(
        unit.inverse.left,
    ),
).byDividing(this, right, factory)
