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

package com.splendo.kaluga.scientific.converter.molarMass

import com.splendo.kaluga.scientific.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricSpecificEnergy
import com.splendo.kaluga.scientific.MolarMass
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificEnergy
import com.splendo.kaluga.scientific.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import kotlin.jvm.JvmName

@JvmName("molarMassTimesMetricSpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesImperialSpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesUKImperialSpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesUSCustomarySpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesSpecificEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = specificEnergy * this
