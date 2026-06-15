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

package com.splendo.kaluga.scientific.converter.undefined.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> / Div<B, A> -> Div<Mul<Mul<A, A>, A>, B>

fun <
    NumeratorLeftAndRightAndDenominatorDenominatorQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorDenominatorQuantity>,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorDenominatorQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorRightUnit,
        >,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<DenominatorNumeratorQuantity>,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        NumeratorUnit,
        NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                >,
            NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        TargetNumeratorUnit,
        DenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                    NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                    >,
                NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
                >,
            DenominatorNumeratorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithRootAsDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorNumeratorQuantity,
            NumeratorLeftAndRightAndDenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorUnitXNumeratorLeftUnit: NumeratorUnit.(NumeratorLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerDenominatorNumeratorUnit: TargetNumeratorUnit.(DenominatorNumeratorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitXNumeratorLeftUnit(
    unit.left,
).targetNumeratorUnitPerDenominatorNumeratorUnit(
    right.unit.numerator,
).byDividing(this, right, factory)
