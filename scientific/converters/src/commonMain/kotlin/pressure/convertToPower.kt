/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("baryeTimesMetricVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, Barye>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>) = (
    Erg per
        volumetricFlow.unit.per
    ).power(this, volumetricFlow)

@JvmName("baryeMultipleTimesMetricVolumetricFlow")
infix operator fun <BaryeUnit : BaryeMultiple> ScientificValue<PhysicalQuantity.Pressure, BaryeUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>,
) = (Erg per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("poundSquareInchTimesImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("poundSquareInchTimesUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>) =
    (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("poundSquareInchTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, PoundSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>) =
    (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("ounceSquareInchTimesImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    (InchOunceForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("ounceSquareInchTimesUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>) =
    (InchOunceForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("ounceSquareInchTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, OunceSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>) =
    (InchOunceForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("kilopoundSquareInchTimesImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>) =
    (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("kilopoundSquareInchTimesUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("kilopoundSquareInchTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, KiloPoundSquareInch>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("kipSquareInchTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, KipSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>) =
    (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("usTonSquareInchTimesUSCustomaryVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, USTonSquareInch>.times(volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>) =
    (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("imperialTonSquareInchTimesUKImperialVolumetricFlow")
infix operator fun ScientificValue<PhysicalQuantity.Pressure, ImperialTonSquareInch>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = (InchPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("imperialPressureTimesImperialVolumetricFlow")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("imperialPressureTimesUKImperialVolumetricFlow")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("imperialPressureTimesUSCustomaryVolumetricFlow")
infix operator fun <PressureUnit : ImperialPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("ukImperialPressureTimesImperialVolumetricFlow")
infix operator fun <PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("ukImperialPressureTimesUKImperialVolumetricFlow")
infix operator fun <PressureUnit : UKImperialPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("usCustomaryPressureTimesImperialVolumetricFlow")
infix operator fun <PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("usCustomaryPressureTimesUSCustomaryVolumetricFlow")
infix operator fun <PressureUnit : USCustomaryPressure> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = (FootPoundForce per volumetricFlow.unit.per).power(this, volumetricFlow)

@JvmName("pressureTimesVolume")
infix operator fun <PressureUnit : Pressure, VolumetricFlowUnit : VolumetricFlow> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
) = Watt.power(this, volumetricFlow)
