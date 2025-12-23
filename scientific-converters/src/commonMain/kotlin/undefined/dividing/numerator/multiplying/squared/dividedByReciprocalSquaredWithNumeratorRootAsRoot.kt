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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, A>, B> / Inv<Mul<A, A>> -> Div<Mul<Mul<A, A>, Mul<A, A>>, B>

fun <
    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity>,
    NumeratorNumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity>,
    NumeratorNumeratorUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorNumeratorLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorNumeratorRightUnit,
        >,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        NumeratorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                    NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                >,
            NumeratorDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        NumeratorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSquaredWithNumeratorRootAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorNumeratorLeftAndRightAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorUnit: NumeratorNumeratorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorUnit: TargetNumeratorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerNumeratorDenominatorUnit(
    unit.denominator,
).byDividing(this, right, factory)
