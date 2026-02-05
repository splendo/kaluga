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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedMultipliedUnit

// Mul<A, A> / A -> A

fun <
    NumeratorLeftAndRightAndDenominatorQuantity : UndefinedQuantityType,
    NumeratorLeftUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorQuantity>,
    NumeratorRightUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorQuantity>,
    NumeratorUnit : UndefinedMultipliedUnit<
        NumeratorLeftAndRightAndDenominatorQuantity,
        NumeratorLeftUnit,
        NumeratorLeftAndRightAndDenominatorQuantity,
        NumeratorRightUnit,
        >,
    DenominatorUnit : AbstractUndefinedScientificUnit<NumeratorLeftAndRightAndDenominatorQuantity>,
    NumeratorLeftValue : UndefinedScientificValue<
        NumeratorLeftAndRightAndDenominatorQuantity,
        NumeratorLeftUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Multiplying<
        NumeratorLeftAndRightAndDenominatorQuantity,
        NumeratorLeftAndRightAndDenominatorQuantity,
        >,
    NumeratorUnit,
    >.dividedByRoot(
    right: UndefinedScientificValue<
        NumeratorLeftAndRightAndDenominatorQuantity,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorLeftUnit) -> NumeratorLeftValue,
) = unit.left.byDividing(this, right, factory)
