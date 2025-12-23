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

// Inv<Mul<A, A>> / Div<Mul<A, A>, B> -> Div<B, Mul<Mul<A, A>, Mul<A, A>>>

fun <
    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity>,
    DenominatorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorLeftUnit,
        NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
        DenominatorNumeratorRightUnit,
        >,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        DenominatorNumeratorUnit,
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorDenominatorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                    >,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithSelfAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                NumeratorReciprocalLeftAndRightAndDenominatorNumeratorLeftAndRightQuantity,
                >,
            DenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalUnitXNumeratorReciprocalUnit: NumeratorReciprocalUnit.(NumeratorReciprocalUnit) -> TargetDenominatorUnit,
    denominatorDenominatorUnitPerTargetDenominatorUnit: DenominatorDenominatorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitPerTargetDenominatorUnit(
    unit.inverse.numeratorReciprocalUnitXNumeratorReciprocalUnit(
        unit.inverse,
    ),
).byDividing(this, right, factory)
