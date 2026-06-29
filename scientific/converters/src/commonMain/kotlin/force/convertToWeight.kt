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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.KilogramForce
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricAndImperialAcceleration
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("dyneDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, Dyne>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Gram.mass(this, acceleration)

@JvmName("dyneDivMetricAcceleration")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, Dyne>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Gram.mass(this, acceleration)

@JvmName("dyneMultipleDivMetricAndImperialAcceleration")
infix operator fun <DyneUnit : DyneMultiple, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, DyneUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Gram.mass(this, acceleration)

@JvmName("dyneMultipleDivMetricAcceleration")
infix operator fun <DyneUnit : DyneMultiple, MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, DyneUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Gram.mass(this, acceleration)

@JvmName("kilogramForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, KilogramForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Kilogram.mass(this, acceleration)

@JvmName("kilogramForceDivMetricAcceleration")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, KilogramForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Kilogram.mass(this, acceleration)

@JvmName("gramForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, GramForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Gram.mass(this, acceleration)

@JvmName("gramForceDivMetricAcceleration")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, GramForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Gram.mass(this, acceleration)

@JvmName("milligramForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, MilligramForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Milligram.mass(this, acceleration)

@JvmName("milligramForceDivMetricAcceleration")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, MilligramForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Milligram.mass(this, acceleration)

@JvmName("tonneForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, TonneForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Tonne.mass(this, acceleration)

@JvmName("tonneForceDivMetricAcceleration")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, TonneForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Tonne.mass(this, acceleration)

@JvmName("metricForceDivMetricAndImperialAcceleration")
infix operator fun <ForceUnit : MetricForce, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Kilogram.mass(this, acceleration)

@JvmName("metricForceDivMetricAcceleration")
infix operator fun <ForceUnit : MetricForce, MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Kilogram.mass(this, acceleration)

@JvmName("ounceForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, OunceForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Ounce.mass(this, acceleration)

@JvmName("ounceForceDivImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, OunceForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = Ounce.mass(this, acceleration)

@JvmName("grainForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, GrainForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Grain.mass(this, acceleration)

@JvmName("grainForceDivImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, GrainForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = Grain.mass(this, acceleration)

@JvmName("usTonForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, UsTonForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = UsTon.mass(this, acceleration)

@JvmName("usTonForceDivImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, UsTonForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = UsTon.mass(this, acceleration)

@JvmName("imperialTonForceDivMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = ImperialTon.mass(this, acceleration)

@JvmName("imperialTonForceDivImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ImperialTonForce>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = ImperialTon.mass(this, acceleration)

@JvmName("imperialForceDivMetricAndImperialAcceleration")
infix operator fun <ForceUnit : ImperialForce, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Pound.mass(this, acceleration)

@JvmName("imperialForceDivImperialAcceleration")
infix operator fun <ForceUnit : ImperialForce, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = Pound.mass(this, acceleration)

@JvmName("ukImperialForceDivMetricAndImperialAcceleration")
infix operator fun <ForceUnit : UKImperialForce, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Pound.ukImperial.mass(this, acceleration)

@JvmName("ukImperialForceDivImperialAcceleration")
infix operator fun <ForceUnit : UKImperialForce, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = Pound.ukImperial.mass(this, acceleration)

@JvmName("usCustomaryForceDivMetricAndImperialAcceleration")
infix operator fun <ForceUnit : USCustomaryForce, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = Pound.usCustomary.mass(this, acceleration)

@JvmName("usCustomaryForceDivImperialAcceleration")
infix operator fun <ForceUnit : USCustomaryForce, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = Pound.usCustomary.mass(this, acceleration)

@JvmName("forceDivAcceleration")
infix operator fun <ForceUnit : Force, AccelerationUnit : Acceleration> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
) = Kilogram.mass(this, acceleration)
