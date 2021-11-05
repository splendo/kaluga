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

package com.splendo.kaluga.scientific.converter.molarEnergy

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.molarMass
import com.splendo.kaluga.scientific.unit.ImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialMolarEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("metricAndImperialMolarEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("metricAndImperialMolarEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("metricAndImperialMolarEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("metricMolarEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("imperialMolarEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("imperialMolarEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("imperialMolarEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>
) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)

@JvmName("molarEnergyDivSpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy, SpecificEnergyUnit : SpecificEnergy> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.div(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>
) = (Kilogram per Mole).molarMass(this, specificEnergy)
