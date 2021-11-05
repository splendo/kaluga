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

package com.splendo.kaluga.scientific.converter.electricResistance

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.magneticFlux.flux
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Weber
import kotlin.jvm.JvmName

@JvmName("abohmTimesAbcoulomb")
infix operator fun ScientificValue<MeasurementType.ElectricResistance, Abohm>.times(charge: ScientificValue<MeasurementType.ElectricCharge, Abcoulomb>) = Maxwell.flux(this, charge)
@JvmName("resistanceTimesCharge")
infix operator fun <ResistanceUnit : ElectricResistance, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>.times(charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>) = Weber.flux(this, charge)
