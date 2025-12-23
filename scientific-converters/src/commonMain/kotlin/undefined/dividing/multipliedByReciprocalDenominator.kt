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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, B> * Inv<B> -> Div<A, Mul<B, B>>

fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorQuantity>,
    LeftDenominatorAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalQuantity>,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        LeftDenominatorAndRightReciprocalQuantity,
        LeftDenominatorUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftDenominatorAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftDenominatorAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    TargetDenominatorUnit : UndefinedMultipliedUnit<
        LeftDenominatorAndRightReciprocalQuantity,
        LeftDenominatorUnit,
        LeftDenominatorAndRightReciprocalQuantity,
        LeftDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            LeftDenominatorAndRightReciprocalQuantity,
            LeftDenominatorAndRightReciprocalQuantity,
            >,
        TargetDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                LeftDenominatorAndRightReciprocalQuantity,
                LeftDenominatorAndRightReciprocalQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorQuantity,
        LeftDenominatorAndRightReciprocalQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftDenominatorAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    leftDenominatorUnitXLeftDenominatorUnit: LeftDenominatorUnit.(LeftDenominatorUnit) -> TargetDenominatorUnit,
    leftNumeratorUnitPerTargetDenominatorUnit: LeftNumeratorUnit.(TargetDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.leftNumeratorUnitPerTargetDenominatorUnit(
    unit.denominator.leftDenominatorUnitXLeftDenominatorUnit(
        unit.denominator,
    ),
).byMultiplying(this, right, factory)
