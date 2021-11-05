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
import com.splendo.kaluga.scientific.converter.molarEnergy.molarEnergy
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivAmountOfSubstance")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarEnergy(this, amountOfSubstance)
@JvmName("metricEnergyDivAmountOfSubstance")
infix operator fun <EnergyUnit : MetricEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarEnergy(this, amountOfSubstance)
@JvmName("imperialEnergyDivAmountOfSubstance")
infix operator fun <EnergyUnit : ImperialEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarEnergy(this, amountOfSubstance)
@JvmName("energyDivAmountOfSubstance")
infix operator fun <EnergyUnit : Energy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (Joule per Mole).molarEnergy(this, amountOfSubstance)
