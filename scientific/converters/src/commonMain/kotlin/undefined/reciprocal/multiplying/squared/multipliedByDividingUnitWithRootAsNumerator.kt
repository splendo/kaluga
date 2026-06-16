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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal.multiplying.squared

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<Mul<A, A>> * Div<A, B> -> Inv<Mul<A, B>>

fun <
    LeftReciprocalLeftAndRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftReciprocalLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorQuantity>,
    LeftReciprocalRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorQuantity>,
    LeftReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
        LeftReciprocalLeftUnit,
        LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
        LeftReciprocalRightUnit,
        >,
    LeftUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
            LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
            >,
        LeftReciprocalUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftReciprocalLeftAndRightAndRightNumeratorQuantity>,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetReciprocalUnit : UndefinedMultipliedUnit<
        LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
        LeftReciprocalLeftUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedReciprocalUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
            RightDenominatorQuantity,
            >,
        TargetReciprocalUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
                RightDenominatorQuantity,
                >,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
            LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithRootAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftReciprocalLeftAndRightAndRightNumeratorQuantity,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalLeftUnitXRightDenominatorUnit: LeftReciprocalLeftUnit.(RightDenominatorUnit) -> TargetReciprocalUnit,
    reciprocalTargetUnit: TargetReciprocalUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.left.leftReciprocalLeftUnitXRightDenominatorUnit(
    right.unit.denominator,
).reciprocalTargetUnit().byMultiplying(this, right, factory)
