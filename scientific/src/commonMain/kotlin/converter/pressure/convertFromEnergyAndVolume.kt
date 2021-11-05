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

package com.splendo.kaluga.scientific.converter.pressure

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("pressureFromEnergyAndVolumeDefault")
fun <
    EnergyUnit : Energy,
    VolumeUnit : Volume,
    PressureUnit : Pressure
> PressureUnit.pressure(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>
) = pressure(energy, volume, ::DefaultScientificValue)

@JvmName("pressureFromEnergyAndVolume")
fun <
    EnergyUnit : Energy,
    VolumeUnit : Volume,
    PressureUnit : Pressure,
    Value : ScientificValue<MeasurementType.Pressure, PressureUnit>
> PressureUnit.pressure(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    factory: (Decimal, PressureUnit) -> Value
) = byDividing(energy, volume, factory)
