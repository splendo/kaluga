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

package com.splendo.kaluga.scientific.converter.undefined.reciprocal

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Inv<A> * Div<Mul<A, A>, B> -> Div<A, B>

fun <
    LeftReciprocalAndRightNumeratorLeftAndRightQuantity : UndefinedQuantityType,
    LeftReciprocalUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightNumeratorLeftAndRightQuantity>,
    LeftUnit : UndefinedReciprocalUnit<
        LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalUnit,
        >,
    RightNumeratorLeftUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorRightUnit : AbstractUndefinedScientificUnit<LeftReciprocalAndRightNumeratorLeftAndRightQuantity>,
    RightNumeratorUnit : UndefinedMultipliedUnit<
        LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorLeftUnit,
        LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
        RightNumeratorRightUnit,
        >,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
            LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
            >,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
        LeftReciprocalUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Reciprocal<
        LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithSquaredNumeratorWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
                LeftReciprocalAndRightNumeratorLeftAndRightQuantity,
                >,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftReciprocalUnitPerRightDenominatorUnit: LeftReciprocalUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.inverse.leftReciprocalUnitPerRightDenominatorUnit(
    right.unit.denominator,
).byMultiplying(this, right, factory)
