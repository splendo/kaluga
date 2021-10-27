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

import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.Grain
import com.splendo.kaluga.scientific.GrainForce
import com.splendo.kaluga.scientific.Gram
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialTon
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.Kip
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.Ounce
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.Pound
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Poundal
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.UsTon
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.acceleration.acceleration
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("dyneDivGram")
operator fun ScientificValue<MeasurementType.Force, Dyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("dyneMultipleDivGram")
operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = (Centimeter per Second per Second).acceleration(this, mass)
@JvmName("metricForceDivMetricWeight")
operator fun <ForceUnit : MetricForce, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Meter per Second per Second).acceleration(this, mass)
@JvmName("poundalDivPound")
operator fun ScientificValue<MeasurementType.Force, Poundal>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("poundForceDivPound")
operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("ounceForceDivOunce")
operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(mass: ScientificValue<MeasurementType.Weight, Ounce>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("grainForceDivGrain")
operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(mass: ScientificValue<MeasurementType.Weight, Grain>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("kipDivPound")
operator fun ScientificValue<MeasurementType.Force, Kip>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("usTonForceDivUsTon")
operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(mass: ScientificValue<MeasurementType.Weight, UsTon>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("imperialTonForceDivImperialTon")
operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("imperialForceDivImperialWeight")
operator fun <ForceUnit : ImperialForce, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("imperialForceDivUKImperialWeight")
operator fun <ForceUnit : ImperialForce, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("imperialForceDivUSCustomaryWeight")
operator fun <ForceUnit : ImperialForce, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("ukImperialForceDivImperialWeight")
operator fun <ForceUnit : UKImperialForce, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("ukImperialForceDivUKImperialWeight")
operator fun <ForceUnit : UKImperialForce, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("ukImperialForceDivUSCustomaryWeight")
operator fun <ForceUnit : UKImperialForce, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("usCustomaryForceDivImperialWeight")
operator fun <ForceUnit : USCustomaryForce, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("usCustomaryForceDivUKImperialWeight")
operator fun <ForceUnit : USCustomaryForce, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("usCustomaryForceDivUSCustomaryWeight")
operator fun <ForceUnit : USCustomaryForce, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Foot per Second per Second).acceleration(this, mass)
@JvmName("forceDivWeight")
operator fun <ForceUnit : Force, WeightUnit : Weight> ScientificValue<MeasurementType.Force, ForceUnit>.div(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Meter per Second per Second).acceleration(this, mass)
