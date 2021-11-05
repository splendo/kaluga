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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("ergDivBarye")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) =
    CubicCentimeter.volume(this, pressure)

@JvmName("ergMultipleDivBarye")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> =
    CubicCentimeter.volume(this, pressure)

@JvmName("ergDivBaryeMultiple")
infix operator fun <BaryeUnit> ScientificValue<MeasurementType.Energy, Erg>.div(pressure: ScientificValue<MeasurementType.Pressure, BaryeUnit>) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> =
    CubicCentimeter.volume(this, pressure)

@JvmName("ergMultipleDivBaryeMultiple")
infix operator fun <ErgUnit, BaryeUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, BaryeUnit>
) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>, BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> =
    CubicCentimeter.volume(this, pressure)

@JvmName("metricEnergyDivMetricPressure")
infix operator fun <EnergyUnit : MetricEnergy, PressureUnit : MetricPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicMeter.volume(this, pressure)

@JvmName("footPoundalDivPoundSquareFoot")
infix operator fun ScientificValue<MeasurementType.Energy, FootPoundal>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFoot>) =
    CubicFoot.volume(this, pressure)

@JvmName("inchPoundForceDivPoundSquareInch")
infix operator fun ScientificValue<MeasurementType.Energy, InchPoundForce>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) =
    CubicInch.volume(this, pressure)

@JvmName("inchOunceForceDivPoundSquareInch")
infix operator fun ScientificValue<MeasurementType.Energy, InchOunceForce>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) =
    CubicInch.volume(this, pressure)

@JvmName("imperialEnergyDivImperialPressure")
infix operator fun <EnergyUnit : ImperialEnergy, PressureUnit : ImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicFoot.volume(this, pressure)

@JvmName("metricAndImperialEnergyDivImperialPressure")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : ImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicFoot.volume(this, pressure)

@JvmName("imperialEnergyDivUKImperialPressure")
infix operator fun <EnergyUnit : ImperialEnergy, PressureUnit : UKImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicFoot.ukImperial.volume(this, pressure)

@JvmName("metricAndImperialEnergyDivUKImperialPressure")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : UKImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicFoot.ukImperial.volume(this, pressure)

@JvmName("imperialEnergyDivUSCustomaryPressure")
infix operator fun <EnergyUnit : ImperialEnergy, PressureUnit : USCustomaryPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicFoot.usCustomary.volume(this, pressure)

@JvmName("metricAndImperialEnergyDivUSCustomaryPressure")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : USCustomaryPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicFoot.usCustomary.volume(this, pressure)

@JvmName("energyDivPressure")
infix operator fun <EnergyUnit : Energy, PressureUnit : Pressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = CubicMeter.volume(this, pressure)
