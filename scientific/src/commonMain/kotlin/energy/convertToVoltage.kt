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
import com.splendo.kaluga.scientific.ElectricCharge
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Volt
import com.splendo.kaluga.scientific.voltage.voltage
import kotlin.jvm.JvmName

@JvmName("ergDivAbcoulomb")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(charge: ScientificValue<MeasurementType.ElectricCharge, Abcoulomb>) = Abvolt.voltage(this, charge)
@JvmName("ergMultipleDivAbcoulomb")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(charge: ScientificValue<MeasurementType.ElectricCharge, Abcoulomb>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Abvolt.voltage(this, charge)
@JvmName("energyDivCharge")
infix operator fun <EnergyUnit : Energy, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = Volt.voltage(this, charge)
