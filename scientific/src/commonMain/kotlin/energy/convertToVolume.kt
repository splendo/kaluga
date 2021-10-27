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

package com.splendo.kaluga.scientific.energy

import com.splendo.kaluga.scientific.Barye
import com.splendo.kaluga.scientific.CubicCentimeter
import com.splendo.kaluga.scientific.CubicFoot
import com.splendo.kaluga.scientific.CubicInch
import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.FootPoundal
import com.splendo.kaluga.scientific.ImperialEnergy
import com.splendo.kaluga.scientific.ImperialPressure
import com.splendo.kaluga.scientific.InchOunceForce
import com.splendo.kaluga.scientific.InchPoundForce
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.MetricEnergy
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.MetricPressure
import com.splendo.kaluga.scientific.PoundSquareFoot
import com.splendo.kaluga.scientific.PoundSquareInch
import com.splendo.kaluga.scientific.Pressure
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialPressure
import com.splendo.kaluga.scientific.USCustomaryPressure
import com.splendo.kaluga.scientific.ukImperial
import com.splendo.kaluga.scientific.usCustomary
import com.splendo.kaluga.scientific.volume.volume
import kotlin.jvm.JvmName

@JvmName("ergDivBarye")
operator fun ScientificValue<MeasurementType.Energy, Erg>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) = CubicCentimeter.volume(this, pressure)
@JvmName("ergMultipleDivBarye")
operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, Barye>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = CubicCentimeter.volume(this, pressure)
@JvmName("ergDivBaryeMultiple")
operator fun <BaryeUnit> ScientificValue<MeasurementType.Energy, Erg>.div(pressure: ScientificValue<MeasurementType.Pressure, BaryeUnit>) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> = CubicCentimeter.volume(this, pressure)
@JvmName("ergMultipleDivBaryeMultiple")
operator fun <ErgUnit, BaryeUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, BaryeUnit>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>, BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> = CubicCentimeter.volume(this, pressure)
@JvmName("metricEnergyDivMetricPressure")
operator fun <EnergyUnit : MetricEnergy, PressureUnit : MetricPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicMeter.volume(this, pressure)
@JvmName("footPoundalDivPoundSquareFoot")
operator fun ScientificValue<MeasurementType.Energy, FootPoundal>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFoot>) = CubicFoot.volume(this, pressure)
@JvmName("inchPoundForceDivPoundSquareInch")
operator fun ScientificValue<MeasurementType.Energy, InchPoundForce>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) = CubicInch.volume(this, pressure)
@JvmName("inchOunceForceDivPoundSquareInch")
operator fun ScientificValue<MeasurementType.Energy, InchOunceForce>.div(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) = CubicInch.volume(this, pressure)
@JvmName("imperialEnergyDivImperialPressure")
operator fun <EnergyUnit : ImperialEnergy, PressureUnit : ImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicFoot.volume(this, pressure)
@JvmName("metricAndImperialEnergyDivImperialPressure")
operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : ImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicFoot.volume(this, pressure)
@JvmName("imperialEnergyDivUKImperialPressure")
operator fun <EnergyUnit : ImperialEnergy, PressureUnit : UKImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicFoot.ukImperial.volume(this, pressure)
@JvmName("metricAndImperialEnergyDivUKImperialPressure")
operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : UKImperialPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicFoot.ukImperial.volume(this, pressure)
@JvmName("imperialEnergyDivUSCustomaryPressure")
operator fun <EnergyUnit : ImperialEnergy, PressureUnit : USCustomaryPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicFoot.usCustomary.volume(this, pressure)
@JvmName("metricAndImperialEnergyDivUSCustomaryPressure")
operator fun <EnergyUnit : MetricAndImperialEnergy, PressureUnit : USCustomaryPressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicFoot.usCustomary.volume(this, pressure)
@JvmName("energyDivPressure")
operator fun <EnergyUnit : Energy, PressureUnit : Pressure> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>) = CubicMeter.volume(this, pressure)
