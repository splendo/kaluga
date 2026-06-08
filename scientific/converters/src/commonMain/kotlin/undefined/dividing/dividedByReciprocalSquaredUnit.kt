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

// Div<B, C> / Inv<Mul<A, A>> -> Div<Mul<Mul<B, A>, A>, C>

fun <
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorQuantity>,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftAndRightQuantity : UndefinedQuantityType,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalLeftAndRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            DenominatorReciprocalLeftAndRightQuantity,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorQuantity,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        TargetNumeratorLeftUnit,
        DenominatorReciprocalLeftAndRightQuantity,
        DenominatorReciprocalLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorQuantity,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            DenominatorReciprocalLeftAndRightQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorQuantity,
                    DenominatorReciprocalLeftAndRightQuantity,
                    >,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            NumeratorDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorQuantity,
        NumeratorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalSquaredUnit(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                DenominatorReciprocalLeftAndRightQuantity,
                DenominatorReciprocalLeftAndRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXDenominatorReciprocalLeftUnit: NumeratorNumeratorUnit.(DenominatorReciprocalLeftUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXDenominatorReciprocalLeftUnit: TargetNumeratorLeftUnit.(DenominatorReciprocalLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorUnit: TargetNumeratorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXDenominatorReciprocalLeftUnit(
    right.unit.inverse.left,
).targetNumeratorLeftUnitXDenominatorReciprocalLeftUnit(
    right.unit.inverse.left,
).targetNumeratorUnitPerNumeratorDenominatorUnit(
    unit.denominator,
).byDividing(this, right, factory)
