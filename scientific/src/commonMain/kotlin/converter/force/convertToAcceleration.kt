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
import com.splendo.kaluga.scientific.converter.acceleration.acceleration
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("dyneDivGram")
infix operator fun ScientificValue<MeasurementType.Force, Dyne>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) =
    (Centimeter per Second per Second).acceleration(this, mass)

@JvmName("dyneMultipleDivGram")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.div(mass: ScientificValue<MeasurementType.Weight, Gram>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> =
    (Centimeter per Second per Second).acceleration(this, mass)

@JvmName("metricForceDivMetricWeight")
infix operator fun <ForceUnit : MetricForce, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Meter per Second per Second).acceleration(this, mass)

@JvmName("poundalDivPound")
infix operator fun ScientificValue<MeasurementType.Force, Poundal>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("poundForceDivPound")
infix operator fun ScientificValue<MeasurementType.Force, PoundForce>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("ounceForceDivOunce")
infix operator fun ScientificValue<MeasurementType.Force, OunceForce>.div(mass: ScientificValue<MeasurementType.Weight, Ounce>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("grainForceDivGrain")
infix operator fun ScientificValue<MeasurementType.Force, GrainForce>.div(mass: ScientificValue<MeasurementType.Weight, Grain>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("kipDivPound")
infix operator fun ScientificValue<MeasurementType.Force, Kip>.div(mass: ScientificValue<MeasurementType.Weight, Pound>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("usTonForceDivUsTon")
infix operator fun ScientificValue<MeasurementType.Force, UsTonForce>.div(mass: ScientificValue<MeasurementType.Weight, UsTon>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("imperialTonForceDivImperialTon")
infix operator fun ScientificValue<MeasurementType.Force, ImperialTonForce>.div(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) =
    (Foot per Second per Second).acceleration(this, mass)

@JvmName("imperialForceDivImperialWeight")
infix operator fun <ForceUnit : ImperialForce, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("imperialForceDivUKImperialWeight")
infix operator fun <ForceUnit : ImperialForce, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("imperialForceDivUSCustomaryWeight")
infix operator fun <ForceUnit : ImperialForce, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("ukImperialForceDivImperialWeight")
infix operator fun <ForceUnit : UKImperialForce, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("ukImperialForceDivUKImperialWeight")
infix operator fun <ForceUnit : UKImperialForce, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("ukImperialForceDivUSCustomaryWeight")
infix operator fun <ForceUnit : UKImperialForce, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("usCustomaryForceDivImperialWeight")
infix operator fun <ForceUnit : USCustomaryForce, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("usCustomaryForceDivUKImperialWeight")
infix operator fun <ForceUnit : USCustomaryForce, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("usCustomaryForceDivUSCustomaryWeight")
infix operator fun <ForceUnit : USCustomaryForce, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Foot per Second per Second).acceleration(this, mass)

@JvmName("forceDivWeight")
infix operator fun <ForceUnit : Force, WeightUnit : Weight> ScientificValue<MeasurementType.Force, ForceUnit>.div(
    mass: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Meter per Second per Second).acceleration(this, mass)
