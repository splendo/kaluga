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

package com.splendo.kaluga.scientific.converter.magneticFlux

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricResistance.resistance
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Ohm
import kotlin.jvm.JvmName

@JvmName("maxwellDivAbcoulomb")
infix operator fun ScientificValue<MeasurementType.MagneticFlux, Maxwell>.div(charge: ScientificValue<MeasurementType.ElectricCharge, Abcoulomb>) =
    Abohm.resistance(this, charge)

@JvmName("fluxDivCharge")
infix operator fun <FluxUnit : MagneticFlux, ChargeUnit : ElectricCharge> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>
) = Ohm.resistance(this, charge)
