/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.luminousFlux

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialIlluminance
import com.splendo.kaluga.scientific.unit.LuminousFlux
import com.splendo.kaluga.scientific.unit.Phot
import com.splendo.kaluga.scientific.unit.PhotMultiple
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import kotlin.jvm.JvmName

@JvmName("fluxDivPhot")
infix operator fun <FluxUnit : LuminousFlux> ScientificValue<PhysicalQuantity.LuminousFlux, FluxUnit>.div(phot: ScientificValue<PhysicalQuantity.Illuminance, Phot>) =
    SquareCentimeter.area(this, phot)

@JvmName("fluxDivPhotMultiple")
infix operator fun <FluxUnit : LuminousFlux, PhotUnit : PhotMultiple> ScientificValue<PhysicalQuantity.LuminousFlux, FluxUnit>.div(
    phot: ScientificValue<PhysicalQuantity.Illuminance, PhotUnit>,
) = SquareCentimeter.area(this, phot)

@JvmName("fluxDivImperialIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : ImperialIlluminance> ScientificValue<PhysicalQuantity.LuminousFlux, FluxUnit>.div(
    illuminance: ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>,
) = SquareFoot.area(this, illuminance)

@JvmName("fluxDivIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : Illuminance> ScientificValue<PhysicalQuantity.LuminousFlux, FluxUnit>.div(
    illuminance: ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>,
) = SquareMeter.area(this, illuminance)
