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

package com.splendo.kaluga.scientific.converter.undefined.dividing

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, B> / Mul<B, A> -> Inv<Mul<B, B>>

fun <
    NumeratorNumeratorAndDenominatorRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
    NumeratorDenominatorAndDenominatorLeftQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorLeftQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorRightQuantity,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorLeftQuantity>,
    DenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorRightQuantity>,
    DenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorLeftQuantity,
        DenominatorLeftUnit,
        NumeratorNumeratorAndDenominatorRightQuantity,
        DenominatorRightUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorAndDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        NumeratorDenominatorAndDenominatorLeftQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorLeftQuantity,
            NumeratorDenominatorAndDenominatorLeftQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorAndDenominatorLeftQuantity,
                NumeratorDenominatorAndDenominatorLeftQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorRightQuantity,
        NumeratorDenominatorAndDenominatorLeftQuantity,
        >,
    NumeratorUnit,
    >.dividedByMultiplyingUnitWithDenominatorAsLeftAndNumeratorAsRight(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorAndDenominatorLeftQuantity,
            NumeratorNumeratorAndDenominatorRightQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorDenominatorUnitXNumeratorDenominatorUnit: NumeratorDenominatorUnit.(NumeratorDenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.denominator.numeratorDenominatorUnitXNumeratorDenominatorUnit(
    unit.denominator,
).reciprocalTargetUnit().byDividing(this, right, factory)
