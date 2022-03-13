/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.amountOfSubstance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.radioactivity.radioactivity
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.Becquerel
import com.splendo.kaluga.scientific.unit.Time

infix fun <AmountOfSubstanceUnit : AmountOfSubstance, TimeUnit : Time> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.decaysWithHalfLife(
    halfLife: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = Becquerel.radioactivity(this, halfLife)
