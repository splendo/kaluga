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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.KilogramForce
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricAndImperialAcceleration
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("kilogramTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Kilogram>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = KilogramForce.force(this, acceleration)

@JvmName("tonneTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Tonne>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = TonneForce.force(this, acceleration)

@JvmName("gramTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Gram>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = GramForce.force(this, acceleration)

@JvmName("milligramTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Milligram>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = MilligramForce.force(this, acceleration)

@JvmName("gramTimesMetricAcceleration")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Weight, Gram>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Dyne.force(this, acceleration)

@JvmName("metricWeightTimesMetricAcceleration")
infix operator fun <WeightUnit : MetricWeight, MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>,
) = Newton.force(this, acceleration)

@JvmName("poundTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Pound>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = PoundForce.force(this, acceleration)

@JvmName("poundTimesImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Pound>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = PoundForce.force(this, acceleration)

@JvmName("ounceTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Ounce>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = OunceForce.force(this, acceleration)

@JvmName("ounceTimesImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Ounce>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = OunceForce.force(this, acceleration)

@JvmName("grainTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Grain>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = GrainForce.force(this, acceleration)

@JvmName("grainTimesImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, Grain>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = GrainForce.force(this, acceleration)

@JvmName("usTonTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, UsTon>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = UsTonForce.force(this, acceleration)

@JvmName("usTonTimesImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, UsTon>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = UsTonForce.force(this, acceleration)

@JvmName("imperialTonTimesMetricAndImperialAcceleration")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, ImperialTon>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = ImperialTonForce.force(this, acceleration)

@JvmName("imperialTonTimesImperialAcceleration")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, ImperialTon>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = ImperialTonForce.force(this, acceleration)

@JvmName("imperialWeightTimesMetricAndImperialAcceleration")
infix operator fun <WeightUnit : ImperialWeight, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = PoundForce.force(this, acceleration)

@JvmName("imperialWeightTimesImperialAcceleration")
infix operator fun <WeightUnit : ImperialWeight, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = PoundForce.force(this, acceleration)

@JvmName("ukImperialWeightTimesMetricAndImperialAcceleration")
infix operator fun <WeightUnit : UKImperialWeight, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = PoundForce.ukImperial.force(this, acceleration)

@JvmName("ukImperialWeightTimesImperialAcceleration")
infix operator fun <WeightUnit : UKImperialWeight, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = PoundForce.ukImperial.force(this, acceleration)

@JvmName("usCustomaryWeightTimesMetricAndImperialAcceleration")
infix operator fun <WeightUnit : USCustomaryWeight, MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>,
) = PoundForce.usCustomary.force(this, acceleration)

@JvmName("usCustomaryWeightTimesImperialAcceleration")
infix operator fun <WeightUnit : USCustomaryWeight, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>,
) = PoundForce.usCustomary.force(this, acceleration)

@JvmName("weightTimesAcceleration")
infix operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
) = Newton.force(this, acceleration)
