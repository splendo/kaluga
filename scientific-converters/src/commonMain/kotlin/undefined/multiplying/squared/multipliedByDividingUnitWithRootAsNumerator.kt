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
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> * Div<A, B> -> Div<Mul<Mul<A, A>, A>, B>

fun <
    LeftLeftAndRightAndRightNumeratorQuantity : UndefinedQuantityType,
    LeftLeftUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightNumeratorQuantity>,
    LeftRightUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightNumeratorQuantity>,
    LeftUnit : UndefinedMultipliedUnit<
        LeftLeftAndRightAndRightNumeratorQuantity,
        LeftLeftUnit,
        LeftLeftAndRightAndRightNumeratorQuantity,
        LeftRightUnit,
        >,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<LeftLeftAndRightAndRightNumeratorQuantity>,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit : AbstractUndefinedScientificUnit<RightDenominatorQuantity>,
    RightUnit : UndefinedDividedUnit<
        LeftLeftAndRightAndRightNumeratorQuantity,
        RightNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetNumeratorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Multiplying<
            LeftLeftAndRightAndRightNumeratorQuantity,
            LeftLeftAndRightAndRightNumeratorQuantity,
            >,
        LeftUnit,
        LeftLeftAndRightAndRightNumeratorQuantity,
        LeftLeftUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Multiplying<
                LeftLeftAndRightAndRightNumeratorQuantity,
                LeftLeftAndRightAndRightNumeratorQuantity,
                >,
            LeftLeftAndRightAndRightNumeratorQuantity,
            >,
        TargetNumeratorUnit,
        RightDenominatorQuantity,
        RightDenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Multiplying<
                    LeftLeftAndRightAndRightNumeratorQuantity,
                    LeftLeftAndRightAndRightNumeratorQuantity,
                    >,
                LeftLeftAndRightAndRightNumeratorQuantity,
                >,
            RightDenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        LeftLeftAndRightAndRightNumeratorQuantity,
        LeftLeftAndRightAndRightNumeratorQuantity,
        >,
    LeftUnit,
    >.multipliedByDividingUnitWithRootAsNumerator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            LeftLeftAndRightAndRightNumeratorQuantity,
            RightDenominatorQuantity,
            >,
        RightUnit,
        >,
    leftUnitXLeftLeftUnit: LeftUnit.(LeftLeftUnit) -> TargetNumeratorUnit,
    targetNumeratorUnitPerRightDenominatorUnit: TargetNumeratorUnit.(RightDenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.leftUnitXLeftLeftUnit(
    unit.left,
).targetNumeratorUnitPerRightDenominatorUnit(
    right.unit.denominator,
).byMultiplying(this, right, factory)
