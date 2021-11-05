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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousEnergy
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import kotlin.jvm.JvmName

@JvmName("luminousEnergyDivMetricExposure")
infix operator fun ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>) = (1(unit.luminousFlux) / 1(luminousExposure.unit.illuminance)).unit.area(this, luminousExposure)
@JvmName("luminousEnergyDivImperialExposure")
infix operator fun ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>) = (1(unit.luminousFlux) / 1(luminousExposure.unit.illuminance)).unit.area(this, luminousExposure)
@JvmName("luminousEnergyDivExposure")
infix operator fun <ExposureUnit : LuminousExposure> ScientificValue<MeasurementType.LuminousEnergy, LuminousEnergy>.div(luminousExposure: ScientificValue<MeasurementType.LuminousExposure, ExposureUnit>) = (1(unit.luminousFlux) / 1(luminousExposure.unit.illuminance)).unit.area(this, luminousExposure)
