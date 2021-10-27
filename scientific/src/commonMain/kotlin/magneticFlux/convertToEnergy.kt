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

package com.splendo.kaluga.scientific.magneticFlux

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.MagneticFlux
import com.splendo.kaluga.scientific.Maxwell
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.energy.energy
import kotlin.jvm.JvmName

@JvmName("maxwellTimesAbampere")
infix operator fun ScientificValue<MeasurementType.MagneticFlux, Maxwell>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Abampere>) = Erg.energy(this, current)
@JvmName("maxwellTimesBiot")
infix operator fun ScientificValue<MeasurementType.MagneticFlux, Maxwell>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Biot>) = Erg.energy(this, current)
@JvmName("fluxTimesCurrent")
infix operator fun <FluxUnit : MagneticFlux, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Joule.energy(this, current)
