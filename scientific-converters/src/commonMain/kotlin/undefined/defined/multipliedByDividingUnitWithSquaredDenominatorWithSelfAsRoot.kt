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

package com.splendo.kaluga.scientific.converter.undefined.defined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// A! * Div<B, Mul<Ex<A>, Ex<A>>> -> Div<B, Ex<A>>

fun <
    LeftAndRightDenominatorLeftAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : DefinedScientificUnit<LeftAndRightDenominatorLeftAndRightQuantity>,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit : AbstractUndefinedScientificUnit<RightNumeratorQuantity>,
    ExtendedRightDenominatorLeftUnit,
    ExtendedRightDenominatorRightUnit,
    RightDenominatorUnit : UndefinedMultipliedUnit<
        UndefinedQuantityType.Extended<
            LeftAndRightDenominatorLeftAndRightQuantity,
            >,
        ExtendedRightDenominatorLeftUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightDenominatorLeftAndRightQuantity,
            >,
        ExtendedRightDenominatorRightUnit,
        >,
    RightUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Multiplying<
            UndefinedQuantityType.Extended<
                LeftAndRightDenominatorLeftAndRightQuantity,
                >,
            UndefinedQuantityType.Extended<
                LeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        RightDenominatorUnit,
        >,
    TargetUnit : UndefinedDividedUnit<
        RightNumeratorQuantity,
        RightNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftAndRightDenominatorLeftAndRightQuantity,
            >,
        ExtendedRightDenominatorLeftUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Extended<
                LeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        TargetUnit,
        >,
    > ScientificValue<LeftAndRightDenominatorLeftAndRightQuantity, LeftUnit>.multipliedByDividingUnitWithSquaredDenominatorWithSelfAsRoot(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            RightNumeratorQuantity,
            UndefinedQuantityType.Multiplying<
                UndefinedQuantityType.Extended<
                    LeftAndRightDenominatorLeftAndRightQuantity,
                    >,
                UndefinedQuantityType.Extended<
                    LeftAndRightDenominatorLeftAndRightQuantity,
                    >,
                >,
            >,
        RightUnit,
        >,
    rightNumeratorUnitPerExtendedRightDenominatorLeftUnit: RightNumeratorUnit.(ExtendedRightDenominatorLeftUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) where
        ExtendedRightDenominatorLeftUnit : UndefinedExtendedUnit<
            LeftAndRightDenominatorLeftAndRightQuantity,
            >,
        ExtendedRightDenominatorLeftUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightDenominatorLeftAndRightQuantity,
                >,
            >,
        ExtendedRightDenominatorRightUnit : UndefinedExtendedUnit<
            LeftAndRightDenominatorLeftAndRightQuantity,
            >,
        ExtendedRightDenominatorRightUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftAndRightDenominatorLeftAndRightQuantity,
                >,
            > =
    right.unit.numerator.rightNumeratorUnitPerExtendedRightDenominatorLeftUnit(
        right.unit.denominator.left,
    ).byMultiplying(this, right, factory)
