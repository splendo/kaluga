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

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.Maxwell
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Weber
import com.splendo.kaluga.scientific.converter.magneticFlux.flux
import kotlin.jvm.JvmName

@JvmName("ergDivAbampere")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(current: ScientificValue<MeasurementType.ElectricCurrent, Abampere>) = Maxwell.flux(this, current)
@JvmName("ergMultipleDivAbampere")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, Abampere>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Maxwell.flux(this, current)
@JvmName("ergDivBiot")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(current: ScientificValue<MeasurementType.ElectricCurrent, Biot>) = Maxwell.flux(this, current)
@JvmName("ergMultipleDivBiot")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, Biot>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Maxwell.flux(this, current)
@JvmName("energyDivCurrent")
infix operator fun <EnergyUnit : Energy, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Weber.flux(this, current)
