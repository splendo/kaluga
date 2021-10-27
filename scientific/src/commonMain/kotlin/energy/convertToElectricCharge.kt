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

package com.splendo.kaluga.scientific.energy

import com.splendo.kaluga.scientific.Abcoulomb
import com.splendo.kaluga.scientific.Abvolt
import com.splendo.kaluga.scientific.Coulomb
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Voltage
import com.splendo.kaluga.scientific.electricCharge.charge
import kotlin.jvm.JvmName

@JvmName("ergDivAbvolt")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) = Abcoulomb.charge(this, voltage)
@JvmName("ergMultipleDivAbvolt")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Abcoulomb.charge(this, voltage)
@JvmName("energyDivVoltage")
infix operator fun <EnergyUnit : Energy, VoltageUnit : Voltage> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Coulomb.charge(this, voltage)
