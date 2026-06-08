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

package com.splendo.kaluga.scientific.converter.undefined.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, B> / Div<A, B> -> Mul<B, B>

fun <
    NumeratorLeftAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorQuantity>,
    NumeratorRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndDenominatorNumeratorQuantity,
        NumeratorLeftUnit,
        NumeratorRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorLeftAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetUnit : UndefinedMultipliedUnit<
        NumeratorRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        NumeratorRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Multiplying<
            NumeratorRightAndDenominatorDenominatorQuantity,
            NumeratorRightAndDenominatorDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndDenominatorNumeratorQuantity,
        NumeratorRightAndDenominatorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithLeftAsNumeratorAndRightAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorLeftAndDenominatorNumeratorQuantity,
            NumeratorRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorRightUnitXNumeratorRightUnit: NumeratorRightUnit.(NumeratorRightUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.right.numeratorRightUnitXNumeratorRightUnit(
    unit.right,
).byDividing(this, right, factory)
