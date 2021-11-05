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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("gramTimesAcceleration")
infix operator fun ScientificValue<MeasurementType.Weight, Gram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) =
    Dyne.force(this, acceleration)

@JvmName("metricWeightTimesMetricAcceleration")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(
    acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>
) = Newton.force(this, acceleration)

@JvmName("poundTimesAcceleration")
infix operator fun ScientificValue<MeasurementType.Weight, Pound>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    PoundForce.force(this, acceleration)

@JvmName("ounceTimesAcceleration")
infix operator fun ScientificValue<MeasurementType.Weight, Ounce>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    OunceForce.force(this, acceleration)

@JvmName("grainTimesAcceleration")
infix operator fun ScientificValue<MeasurementType.Weight, Grain>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    GrainForce.force(this, acceleration)

@JvmName("usTonTimesAcceleration")
infix operator fun ScientificValue<MeasurementType.Weight, UsTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    UsTonForce.force(this, acceleration)

@JvmName("imperialTonTimesAcceleration")
infix operator fun ScientificValue<MeasurementType.Weight, ImperialTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) =
    ImperialTonForce.force(this, acceleration)

@JvmName("imperialWeightTimesImperialAcceleration")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(
    acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>
) = PoundForce.force(this, acceleration)

@JvmName("ukImperialWeightTimesImperialAcceleration")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(
    acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>
) = PoundForce.ukImperial.force(this, acceleration)

@JvmName("usCustomaryWeightTimesImperialAcceleration")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(
    acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>
) = PoundForce.usCustomary.force(this, acceleration)

@JvmName("weightTimesAcceleration")
infix operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Weight, WeightUnit>.times(
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
) = Newton.force(this, acceleration)
