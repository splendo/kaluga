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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, B>> / Div<A, C> -> Div<C, Mul<Mul<A, A>, B>>

fun <
    NumeratorReciprocalLeftAndDenominatorNumeratorQuantity : UndefinedQuantityType,
    NumeratorReciprocalLeftUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorQuantity>,
    NumeratorReciprocalRightQuantity : UndefinedQuantityType,
    NumeratorReciprocalRightUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalRightQuantity>,
    NumeratorReciprocalUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    NumeratorUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        NumeratorReciprocalUnit,
        >,
    DenominatorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorReciprocalLeftAndDenominatorNumeratorQuantity>,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit : AbstractUndefinedScientificUnit<DenominatorDenominatorQuantity>,
    DenominatorUnit : UndefinedDividedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        DenominatorNumeratorUnit,
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        >,
    TargetDenominatorLeftUnit : UndefinedMultipliedUnit<
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
        NumeratorReciprocalLeftUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            >,
        TargetDenominatorLeftUnit,
        NumeratorReciprocalRightQuantity,
        NumeratorReciprocalRightUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        DenominatorDenominatorQuantity,
        DenominatorDenominatorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
                NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
                >,
            NumeratorReciprocalRightQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            DenominatorDenominatorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
                    NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
                    >,
                NumeratorReciprocalRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            NumeratorReciprocalRightQuantity,
            >,
        >,
    NumeratorUnit,
    >.dividedByDividingUnitWithLeftAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorReciprocalLeftAndDenominatorNumeratorQuantity,
            DenominatorDenominatorQuantity,
            >,
        DenominatorUnit,
        >,
    numeratorReciprocalLeftUnitXNumeratorReciprocalLeftUnit: NumeratorReciprocalLeftUnit.(NumeratorReciprocalLeftUnit) -> TargetDenominatorLeftUnit,
    targetDenominatorLeftUnitXNumeratorReciprocalRightUnit: TargetDenominatorLeftUnit.(NumeratorReciprocalRightUnit) -> TargetDenominatorUnit,
    denominatorDenominatorUnitPerTargetDenominatorUnit: DenominatorDenominatorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = right.unit.denominator.denominatorDenominatorUnitPerTargetDenominatorUnit(
    unit.inverse.left.numeratorReciprocalLeftUnitXNumeratorReciprocalLeftUnit(
        unit.inverse.left,
    ).targetDenominatorLeftUnitXNumeratorReciprocalRightUnit(
        unit.inverse.right,
    ),
).byDividing(this, right, factory)
