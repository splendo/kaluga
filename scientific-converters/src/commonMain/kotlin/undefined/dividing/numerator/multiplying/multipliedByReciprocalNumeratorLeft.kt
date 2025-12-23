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

package com.splendo.kaluga.scientific.converter.undefined.dividing.numerator.multiplying

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<Mul<A, B>, C> * Inv<A> -> Div<B, C>

fun <
    LeftNumeratorLeftAndRightReciprocalQuantity : UndefinedQuantityType,
    LeftNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalQuantity>,
    LeftNumeratorRightQuantity : UndefinedQuantityType,
    LeftNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftNumeratorRightQuantity>,
    LeftNumeratorUnit : UndefinedMultipliedUnit<
        LeftNumeratorLeftAndRightReciprocalQuantity,
        LeftNumeratorLeftUnit,
        LeftNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        >,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit : AbstractUndefinedScientificUnit<LeftDenominatorQuantity>,
    LeftUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightReciprocalQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftNumeratorUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    RightReciprocalUnit : AbstractUndefinedScientificUnit<LeftNumeratorLeftAndRightReciprocalQuantity>,
    RightUnit : UndefinedReciprocalUnit<
        LeftNumeratorLeftAndRightReciprocalQuantity,
        RightReciprocalUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftNumeratorRightQuantity,
        LeftNumeratorRightUnit,
        LeftDenominatorQuantity,
        LeftDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftNumeratorRightQuantity,
            LeftDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        UndefinedQuantityType.Multiplying<
            LeftNumeratorLeftAndRightReciprocalQuantity,
            LeftNumeratorRightQuantity,
            >,
        LeftDenominatorQuantity,
        >,
    LeftUnit,
    >.multipliedByReciprocalNumeratorLeft(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            LeftNumeratorLeftAndRightReciprocalQuantity,
            >,
        RightUnit,
        >,
    leftNumeratorRightUnitPerLeftDenominatorUnit: LeftNumeratorRightUnit.(LeftDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.right.leftNumeratorRightUnitPerLeftDenominatorUnit(
    unit.denominator,
).byMultiplying(this, right, factory)
