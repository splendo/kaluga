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
import com.splendo.kaluga.scientific.converter.length.distance
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import kotlin.jvm.JvmName

@JvmName("ergDivDyne")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(force: ScientificValue<PhysicalQuantity.Force, Dyne>) = Centimeter.distance(this, force)

@JvmName("ergMultipleDivDyne")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(force: ScientificValue<PhysicalQuantity.Force, Dyne>) =
    Centimeter.distance(this, force)

@JvmName("ergDivDyneMultiple")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Energy, Erg>.div(force: ScientificValue<PhysicalQuantity.Force, DyneUnit>) =
    Centimeter.distance(this, force)

@JvmName("ergMultipleDivDyneMultiple")
infix operator fun <ErgUnit : ErgMultiple, DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, DyneUnit>,
) = Centimeter.distance(this, force)

@JvmName("footPoundalDivPoundal")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundal>.div(force: ScientificValue<PhysicalQuantity.Force, Poundal>) = Foot.distance(this, force)

@JvmName("footPoundForceDivPoundForce")
infix operator fun ScientificValue<PhysicalQuantity.Energy, FootPoundForce>.div(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) = Foot.distance(this, force)

@JvmName("inchPoundForceDivPoundForce")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchPoundForce>.div(force: ScientificValue<PhysicalQuantity.Force, PoundForce>) = Inch.distance(this, force)

@JvmName("inchOunceForceDivOunceForce")
infix operator fun ScientificValue<PhysicalQuantity.Energy, InchOunceForce>.div(force: ScientificValue<PhysicalQuantity.Force, OunceForce>) = Inch.distance(this, force)

@JvmName("imperialEnergyDivImperialForce")
infix operator fun <EnergyUnit : ImperialEnergy, ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = Foot.distance(this, force)

@JvmName("imperialEnergyDivUKImperialForce")
infix operator fun <EnergyUnit : ImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = Foot.distance(this, force)

@JvmName("imperialEnergyDivUSCustomaryForce")
infix operator fun <EnergyUnit : ImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = Foot.distance(this, force)

@JvmName("metricAndImperialEnergyDivImperialForce")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = Foot.distance(this, force)

@JvmName("metricAndImperialEnergyDivUKImperialForce")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = Foot.distance(this, force)

@JvmName("metricAndImperialEnergyDivUSCustomaryForce")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = Foot.distance(this, force)

@JvmName("energyDivForce")
infix operator fun <EnergyUnit : Energy, ForceUnit : Force> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    Meter.distance(this, force)
