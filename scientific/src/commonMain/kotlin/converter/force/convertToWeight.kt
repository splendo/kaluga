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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Dyne
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
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("dyneDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, Dyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) =
    Gram.mass(this, acceleration)

@JvmName("dyneMultipleDivAcceleration")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> =
    Gram.mass(this, acceleration)

@JvmName("kilogramForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, KilogramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) =
    Kilogram.mass(this, acceleration)

@JvmName("gramForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, GramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) =
    Gram.mass(this, acceleration)

@JvmName("milligramForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, MilligramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) =
    Milligram.mass(this, acceleration)

@JvmName("tonneForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, TonneForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) =
    Tonne.mass(this, acceleration)

@JvmName("metricForceDivMetricAcceleration")
infix operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>
) = Kilogram.mass(this, acceleration)

@JvmName("poundalDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, Poundal>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    Pound.mass(this, acceleration)

@JvmName("poundForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    Pound.mass(this, acceleration)

@JvmName("ounceForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    Ounce.mass(this, acceleration)

@JvmName("grainForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    Grain.mass(this, acceleration)

@JvmName("kipDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, Kip>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    Pound.mass(this, acceleration)

@JvmName("usTonForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    UsTon.mass(this, acceleration)

@JvmName("imperialTonForceDivAcceleration")
infix operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    ImperialTon.mass(this, acceleration)

@JvmName("imperialForceDivImperialAcceleration")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>
) = Pound.mass(this, acceleration)

@JvmName("ukImperialForceDivImperialAcceleration")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>
) = Pound.ukImperial.mass(this, acceleration)

@JvmName("usCustomaryForceDivImperialAcceleration")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>
) = Pound.usCustomary.mass(this, acceleration)

@JvmName("forceDivAcceleration")
infix operator fun <ForceUnit : Force, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) = Kilogram.mass(this, acceleration)
