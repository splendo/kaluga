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

package com.splendo.kaluga.scientific.converter.pressure

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MetricEnergyDensity
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.per

/**
 * Reinterprets this [Pressure] value as a [PhysicalQuantity.EnergyDensity] of equal magnitude.
 * [Pressure][PhysicalQuantity.Pressure] shares its dimension (energy per volume) with [EnergyDensity][PhysicalQuantity.EnergyDensity]
 * but is a distinct quantity, so the two cannot be mixed in arithmetic; use this to bridge between them.
 * @return the [EnergyDensity][PhysicalQuantity.EnergyDensity] equivalent in `Joule per CubicMeter`
 */
fun <PressureUnit : Pressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.asEnergyDensity(): ScientificValue<PhysicalQuantity.EnergyDensity, MetricEnergyDensity> =
    DefaultScientificValue((Joule per CubicMeter).fromSIUnit(unit.toSIUnit(decimalValue)), Joule per CubicMeter)
