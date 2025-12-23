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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit

// Div<A, B> / Inv<B> -> A

fun <
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit : AbstractUndefinedScientificUnit<NumeratorNumeratorQuantity>,
    NumeratorDenominatorAndDenominatorReciprocalQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
    NumeratorUnit : UndefinedDividedUnit<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        NumeratorDenominatorAndDenominatorReciprocalQuantity,
        NumeratorDenominatorUnit,
        >,
    DenominatorReciprocalUnit : AbstractUndefinedScientificUnit<NumeratorDenominatorAndDenominatorReciprocalQuantity>,
    DenominatorUnit : UndefinedReciprocalUnit<
        NumeratorDenominatorAndDenominatorReciprocalQuantity,
        DenominatorReciprocalUnit,
        >,
    NumeratorNumeratorValue : UndefinedScientificValue<
        NumeratorNumeratorQuantity,
        NumeratorNumeratorUnit,
        >,
    > UndefinedScientificValue<
    UndefinedQuantityType.Dividing<
        NumeratorNumeratorQuantity,
        NumeratorDenominatorAndDenominatorReciprocalQuantity,
        >,
    NumeratorUnit,
    >.dividedByReciprocalDenominator(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Reciprocal<
            NumeratorDenominatorAndDenominatorReciprocalQuantity,
            >,
        DenominatorUnit,
        >,
    factory: (Decimal, NumeratorNumeratorUnit) -> NumeratorNumeratorValue,
) = unit.numerator.byDividing(this, right, factory)
