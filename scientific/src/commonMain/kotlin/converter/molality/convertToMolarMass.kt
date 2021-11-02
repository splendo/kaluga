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

package com.splendo.kaluga.scientific.converter.molality

import com.splendo.kaluga.scientific.ImperialMolality
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMolality
import com.splendo.kaluga.scientific.Molality
import com.splendo.kaluga.scientific.Mole
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMolality
import com.splendo.kaluga.scientific.USCustomaryMolality
import com.splendo.kaluga.scientific.converter.molarMass.molarMass
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, MetricMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("imperialMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, ImperialMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("ukImperialMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, UKImperialMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("usCustomaryMolalityMolarMass")
fun ScientificValue<MeasurementType.Molality, USCustomaryMolality>.molarMass() = (unit.per per unit.amountOfSubstance).molarMass(this)
@JvmName("molalityMolarMass")
fun <MolalityUnit : Molality> ScientificValue<MeasurementType.Molality, MolalityUnit>.molarMass() = (Kilogram per Mole).molarMass(this)
