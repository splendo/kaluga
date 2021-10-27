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

import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.FootPoundForce
import com.splendo.kaluga.scientific.FootPoundal
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialEnergy
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.InchOunceForce
import com.splendo.kaluga.scientific.InchPoundForce
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.MetricEnergy
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.length.distance
import kotlin.jvm.JvmName

@JvmName("ergDivDyne")
operator fun ScientificValue<MeasurementType.Energy, Erg>.div(force: ScientificValue<MeasurementType.Force, Dyne>) = Centimeter.distance(this, force)
@JvmName("ergMultipleDivDyne")
operator fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(force: ScientificValue<MeasurementType.Force, Dyne>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Centimeter.distance(this, force)
@JvmName("ergDivDyneMultiple")
operator fun <DyneUnit> ScientificValue<MeasurementType.Energy, Erg>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Centimeter.distance(this, force)
@JvmName("ergMultipleDivDyneMultiple")
operator fun <ErgUnit, DyneUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.div(force: ScientificValue<MeasurementType.Force, DyneUnit>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg>, DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Centimeter.distance(this, force)
@JvmName("metricEnergyDivMetricForce")
operator fun <EnergyUnit : MetricEnergy, ForceUnit : MetricForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Meter.distance(this, force)
@JvmName("footPoundalDivPoundal")
operator fun ScientificValue<MeasurementType.Energy, FootPoundal>.div(force: ScientificValue<MeasurementType.Force, Poundal>) = Foot.distance(this, force)
@JvmName("footPoundForceDivPoundForce")
operator fun ScientificValue<MeasurementType.Energy, FootPoundForce>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = Foot.distance(this, force)
@JvmName("inchPoundForceDivPoundForce")
operator fun ScientificValue<MeasurementType.Energy, InchPoundForce>.div(force: ScientificValue<MeasurementType.Force, PoundForce>) = Inch.distance(this, force)
@JvmName("inchOunceForceDivOunceForce")
operator fun ScientificValue<MeasurementType.Energy, InchOunceForce>.div(force: ScientificValue<MeasurementType.Force, OunceForce>) = Inch.distance(this, force)
@JvmName("imperialEnergyDivImperialForce")
operator fun <EnergyUnit : ImperialEnergy, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("imperialEnergyDivUKImperialForce")
operator fun <EnergyUnit : ImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("imperialEnergyDivUSCustomaryForce")
operator fun <EnergyUnit : ImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivImperialForce")
operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : ImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivUKImperialForce")
operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("metricAndImperialEnergyDivUSCustomaryForce")
operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Foot.distance(this, force)
@JvmName("energyDivForce")
operator fun <EnergyUnit : Energy, ForceUnit : Force> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(force: ScientificValue<MeasurementType.Force, ForceUnit>) = Meter.distance(this, force)
