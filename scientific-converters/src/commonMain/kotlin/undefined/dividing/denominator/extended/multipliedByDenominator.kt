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

package com.splendo.kaluga.scientific.converter.undefined.dividing.denominator.extended

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

// Div<A, Ex<B>> * B! -> A

fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : AbstractUndefinedScientificUnit<LeftNumeratorQuantity>,
    ExtendedLeftDenominatorUnit,
    LeftUnit : UndefinedDividedUnit<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightQuantity,
            >,
        ExtendedLeftDenominatorUnit,
        >,
    LeftDenominatorAndRightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit : DefinedScientificUnit<LeftDenominatorAndRightQuantity>,
    LeftNumeratorValue : UndefinedScientificValue<
        LeftNumeratorQuantity,
        LeftNumeratorUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        LeftNumeratorQuantity,
        UndefinedQuantityType.Extended<
            LeftDenominatorAndRightQuantity,
            >,
        >,
    LeftUnit,
    >.multipliedByDenominator(
    right: ScientificValue<LeftDenominatorAndRightQuantity, RightUnit>,
    factory: (Decimal, LeftNumeratorUnit) -> LeftNumeratorValue,
) where
        ExtendedLeftDenominatorUnit : UndefinedExtendedUnit<
            LeftDenominatorAndRightQuantity,
            >,
        ExtendedLeftDenominatorUnit : AbstractUndefinedScientificUnit<
            UndefinedQuantityType.Extended<
                LeftDenominatorAndRightQuantity,
                >,
            > =
    unit.numerator.byMultiplying(this, right, factory)
