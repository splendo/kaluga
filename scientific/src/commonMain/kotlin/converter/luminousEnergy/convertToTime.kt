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

package com.splendo.kaluga.scientific.converter.luminousEnergy

import com.splendo.kaluga.scientific.LuminousEnergy
import com.splendo.kaluga.scientific.LuminousFlux
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import kotlin.jvm.JvmName

@JvmName("luminousEnergyDivLuminousFlux")
infix operator fun <LuminousFluxUnit : LuminousFlux> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousFlux: ScientificValue<MeasurementType.LuminousFlux, LuminousFluxUnit>) = unit.time.time(this, luminousFlux)
