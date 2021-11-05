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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.amountOfSubstance.amountOfSubstance
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricAndImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricMolarEnergy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("metricAndImperialEnergyDivMetricMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("metricAndImperialEnergyDivImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("metricEnergyDivMetricMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("imperialEnergyDivImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("energyDivMolarEnergy")
infix operator fun <EnergyUnit : Energy, MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
