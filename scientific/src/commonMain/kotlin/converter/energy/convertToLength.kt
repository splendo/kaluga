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
import com.splendo.kaluga.scientific.converter.length.distance
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import kotlin.jvm.JvmName

@JvmName("ergDivDyne")
infix operator fun ScientificValue<MeasurementType.Energy, Erg>.div(force: ScientificValue<MeasurementType.Force, Dyne>) = Centimeter.distance(this, force)
@JvmName("ergMultipleDivDyne")
infix operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(force: ScientificValue<MeasurementType.Force, Dyne>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Centimeter.distance(this, force)
@JvmName("ergDivDyneMultiple")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Energy, Erg>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Centimeter.distance(this, force)
@JvmName("ergMultipleDivDyneMultiple")
infix operator fun <ErgUnit, DyneUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>, DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Centimeter.distance(this, force)
@JvmName("metricEnergyDivMetricForce")
infix operator fun <EnergyUnit : MetricEnergy, ForceUnit : MetricForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Meter.distance(this, force)
@JvmName("footPoundalDivPoundal")
infix operator fun ScientificValue<MeasurementType.Energy, FootPoundal>.div(force: ScientificValue<MeasurementType.Force, Poundal>) = Foot.distance(this, force)
@JvmName("footPoundForceDivPoundForce")
infix operator fun ScientificValue<MeasurementType.Energy, FootPoundForce>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = Foot.distance(this, force)
@JvmName("inchPoundForceDivPoundForce")
infix operator fun ScientificValue<MeasurementType.Energy, InchPoundForce>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = Inch.distance(this, force)
@JvmName("inchOunceForceDivOunceForce")
infix operator fun ScientificValue<MeasurementType.Energy, InchOunceForce>.div(force: ScientificValue<MeasurementType.Force, OunceForce>) = Inch.distance(this, force)
@JvmName("imperialEnergyDivImperialForce")
infix operator fun <EnergyUnit : ImperialEnergy, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("imperialEnergyDivUKImperialForce")
infix operator fun <EnergyUnit : ImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("imperialEnergyDivUSCustomaryForce")
infix operator fun <EnergyUnit : ImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivImperialForce")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivUKImperialForce")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivUSCustomaryForce")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("energyDivForce")
infix operator fun <EnergyUnit : Energy, ForceUnit : Force> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Meter.distance(this, force)
