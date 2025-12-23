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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, Mul<B, B>> / Inv<Mul<A, A>> -> Div<Mul<Mul<A, A>, A>, Mul<B, B>>

fun <
    NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity>,
    NumeratorDenominatorLeftAndRightQuantity : UndefinedQuantityType,
    NumeratorDenominatorLeftUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorRightUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorLeftAndRightQuantity>,
    NumeratorDenominatorUnit : UndefinedMultipliedUnit<
        NumeratorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorLeftUnit,
        NumeratorDenominatorLeftAndRightQuantity,
        NumeratorDenominatorRightUnit,
        >,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        NumeratorDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                    NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                    >,
                NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Multiplying<
                NumeratorDenominatorLeftAndRightQuantity,
                NumeratorDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
        UndefinedQuantityType.Multiplying<
            NumeratorDenominatorLeftAndRightQuantity,
            NumeratorDenominatorLeftAndRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSquaredWithNumeratorAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                NumeratorNumeratorAndDenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    denominatorReciprocalUnitXNumeratorNumeratorUnit: DenominatorReciprocalUnit.(NumeratorNumeratorUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorUnit: TargetNumeratorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.inverse.denominatorReciprocalUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorUnitPerNumeratorDenominatorUnit(
    unit.denominator,
).byDividing(this, right, factory)
