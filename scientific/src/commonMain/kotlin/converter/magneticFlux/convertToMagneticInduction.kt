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

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Gauss
import com.splendo.kaluga.scientific.MagneticFlux
import com.splendo.kaluga.scientific.Maxwell
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.Tesla
import com.splendo.kaluga.scientific.converter.magneticInduction.induction
import kotlin.jvm.JvmName

@JvmName("maxwellDivSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.MagneticFlux, Maxwell>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Gauss.induction(this, area)
@JvmName("fluxDivArea")
infix operator fun <FluxUnit : MagneticFlux, AreaUnit : Area> ScientificValue<MeasurementType.MagneticFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Tesla.induction(this, area)
