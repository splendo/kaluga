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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit

// A / B -> Div<A, B>

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : AbstractUndefinedScientificUnit<NumeratorQuantity>,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit : AbstractUndefinedScientificUnit<DenominatorQuantity>,
    TargetUnit : UndefinedDividedUnit<
        NumeratorQuantity,
        NumeratorUnit,
        DenominatorQuantity,
        DenominatorUnit,
        >,
    TargetValue : UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            NumeratorQuantity,
            DenominatorQuantity,
            >,
        TargetUnit,
        >,
    > UndefinedScientificValue<
    NumeratorQuantity,
    NumeratorUnit,
    >.dividedByUndefinedUnit(
    right: UndefinedScientificValue<
        DenominatorQuantity,
        DenominatorUnit,
        >,
    numeratorUnitPerDenominatorUnit: NumeratorUnit.(DenominatorUnit) -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numeratorUnitPerDenominatorUnit(
    right.unit,
).byDividing(this, right, factory)
