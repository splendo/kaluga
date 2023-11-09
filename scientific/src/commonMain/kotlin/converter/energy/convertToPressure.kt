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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.pressure
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("ergDivCubicCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(volume: ScientificValue<PhysicalQuantity.Volume, CubicCentimeter>) = Barye.pressure(this, volume)

@JvmName("ergMultipleDivCubicCentimeter")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(volume: ScientificValue<PhysicalQuantity.Volume, CubicCentimeter>) =
    Barye.pressure(this, volume)

@JvmName("footPoundalDivCubicFoot")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundal>.div(volume: ScientificValue<PhysicalQuantity.Volume, CubicFoot>) = PoundSquareFoot.pressure(this, volume)

@JvmName("footPoundForceDivCubicFoot")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundForce>.div(volume: ScientificValue<PhysicalQuantity.Volume, CubicFoot>) =
    PoundSquareFoot.pressure(this, volume)

@JvmName("imperialEnergyDivImperialVolume")
infix operator fun <EnergyUnit : ImperialEnergy, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = PoundSquareInch.pressure(this, volume)

@JvmName("metricAndImperialEnergyDivImperialVolume")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = PoundSquareInch.pressure(this, volume)

@JvmName("imperialEnergyDivUKImperialVolume")
infix operator fun <EnergyUnit : ImperialEnergy, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = PoundSquareInch.ukImperial.pressure(this, volume)

@JvmName("imperialEnergyDivUSCustomaryVolume")
infix operator fun <EnergyUnit : ImperialEnergy, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = PoundSquareInch.usCustomary.pressure(this, volume)

@JvmName("metricAndImperialEnergyDivUKImperialVolume")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = PoundSquareInch.ukImperial.pressure(this, volume)

@JvmName("metricAndImperialEnergyDivUSCustomaryVolume")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = PoundSquareInch.usCustomary.pressure(this, volume)

@JvmName("energyDivVolume")
infix operator fun <EnergyUnit : Energy, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = Pascal.pressure(this, volume)
