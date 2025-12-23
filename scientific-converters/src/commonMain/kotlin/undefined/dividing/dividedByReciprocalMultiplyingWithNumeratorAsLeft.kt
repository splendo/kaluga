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

// Div<B, C> / Inv<Mul<B, A>> -> Div<Mul<Mul<B, B>, A>, C>

fun <
    NumeratorNumeratorAndDenominatorReciprocalLeftQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalLeftQuantity>,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorAndDenominatorReciprocalLeftQuantity>,
    DenominatorReciprocalRightQuantity : UndefinedQuantityType,
    DenominatorReciprocalRightUnit : AbstractUndefinedScientificUnit<DenominatorReciprocalRightQuantity>,
    DenominatorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
        DenominatorReciprocalLeftUnit,
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    DenominatorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
            DenominatorReciprocalRightQuantity,
            >,
        DenominatorReciprocalUnit,
        >,
    TargetNumeratorLeftUnit : UndefinedMultipliedUnit<
        NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorUnit,
        NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorNumeratorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
            NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
            >,
        TargetNumeratorLeftUnit,
        DenominatorReciprocalRightQuantity,
        DenominatorReciprocalRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
                NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
                >,
            DenominatorReciprocalRightQuantity,
            >,
        TargetNumeratorUnit,
        NumeratorDenominatorQuantity,
        NumeratorDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
                    NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
                    >,
                DenominatorReciprocalRightQuantity,
                >,
            NumeratorDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
        NumeratorDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalMultiplyingWithNumeratorAsLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                NumeratorNumeratorAndDenominatorReciprocalLeftQuantity,
                DenominatorReciprocalRightQuantity,
                >,
            >,
        DenominatorUnit,
        >,
    numeratorNumeratorUnitXNumeratorNumeratorUnit: NumeratorNumeratorUnit.(NumeratorNumeratorUnit) -> TargetNumeratorLeftUnit,
    targetNumeratorLeftUnitXDenominatorReciprocalRightUnit: TargetNumeratorLeftUnit.(DenominatorReciprocalRightUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerNumeratorDenominatorUnit: TargetNumeratorUnit.(NumeratorDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.numeratorNumeratorUnitXNumeratorNumeratorUnit(
    unit.numerator,
).targetNumeratorLeftUnitXDenominatorReciprocalRightUnit(
    right.unit.inverse.right,
).targetNumeratorUnitPerNumeratorDenominatorUnit(
    unit.denominator,
).byDividing(this, right, factory)
