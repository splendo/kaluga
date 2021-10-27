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

package com.splendo.kaluga.scientific.force

import com.splendo.kaluga.scientific.Acceleration
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.Grain
import com.splendo.kaluga.scientific.GrainForce
import com.splendo.kaluga.scientific.Gram
import com.splendo.kaluga.scientific.GramForce
import com.splendo.kaluga.scientific.ImperialAcceleration
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialTon
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.KilogramForce
import com.splendo.kaluga.scientific.Kip
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAcceleration
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Milligram
import com.splendo.kaluga.scientific.MilligramForce
import com.splendo.kaluga.scientific.Ounce
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.Pound
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Tonne
import com.splendo.kaluga.scientific.TonneForce
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.UsTon
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.ukImperial
import com.splendo.kaluga.scientific.usCustomary
import com.splendo.kaluga.scientific.weight.mass
import kotlin.jvm.JvmName

@JvmName("dyneDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("dyneMultipleDivAcceleration")
operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) where DyneUnit : MetricForce, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = Gram.mass(this, acceleration)
@JvmName("kilogramForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, KilogramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("gramForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, GramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Gram.mass(this, acceleration)
@JvmName("milligramForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, MilligramForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Milligram.mass(this, acceleration)
@JvmName("tonneForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, TonneForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Tonne.mass(this, acceleration)
@JvmName("metricForceDivMetricAcceleration")
operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Kilogram.mass(this, acceleration)
@JvmName("poundalDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("poundForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("ounceForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Ounce.mass(this, acceleration)
@JvmName("grainForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Grain.mass(this, acceleration)
@JvmName("kipDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("usTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTon.mass(this, acceleration)
@JvmName("imperialTonForceDivAcceleration")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTon.mass(this, acceleration)
@JvmName("imperialForceDivImperialAcceleration")
operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.mass(this, acceleration)
@JvmName("ukImperialForceDivImperialAcceleration")
operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.ukImperial.mass(this, acceleration)
@JvmName("usCustomaryForceDivImperialAcceleration")
operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = Pound.usCustomary.mass(this, acceleration)
@JvmName("forceDivAcceleration")
operator fun <ForceUnit : Force, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Force, ForceUnit>.div(acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>) = Kilogram.mass(this, acceleration)
