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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.magneticFlux.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.Maxwell
import kotlin.jvm.JvmName

@JvmName("abampereTimesMaxwell")
infix operator fun ScientificValue<MeasurementType.ElectricCurrent, Abampere>.times(flux: ScientificValue<MeasurementType.MagneticFlux, Maxwell>) = flux * this
@JvmName("biotTimesMaxwell")
infix operator fun ScientificValue<MeasurementType.ElectricCurrent, Biot>.times(flux: ScientificValue<MeasurementType.MagneticFlux, Maxwell>) = flux * this
@JvmName("currentTimesFlux")
infix operator fun <FluxUnit : MagneticFlux, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(flux: ScientificValue<MeasurementType.MagneticFlux, FluxUnit>) = flux * this
