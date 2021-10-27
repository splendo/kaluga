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

package com.splendo.kaluga.scientific.weight

import com.splendo.kaluga.scientific.Acceleration
import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.Grain
import com.splendo.kaluga.scientific.GrainForce
import com.splendo.kaluga.scientific.Gram
import com.splendo.kaluga.scientific.ImperialAcceleration
import com.splendo.kaluga.scientific.ImperialTon
import com.splendo.kaluga.scientific.ImperialTonForce
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAcceleration
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.Newton
import com.splendo.kaluga.scientific.Ounce
import com.splendo.kaluga.scientific.OunceForce
import com.splendo.kaluga.scientific.Pound
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.UsTon
import com.splendo.kaluga.scientific.UsTonForce
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.force.force
import com.splendo.kaluga.scientific.ukImperial
import com.splendo.kaluga.scientific.usCustomary
import kotlin.jvm.JvmName

@JvmName("gramTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Gram>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Dyne.force(this, acceleration)
@JvmName("metricWeightTimesMetricAcceleration")
operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, MetricAcceleration>) = Newton.force(this, acceleration)
@JvmName("poundTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Pound>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.force(this, acceleration)
@JvmName("ounceTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Ounce>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = OunceForce.force(this, acceleration)
@JvmName("grainTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, Grain>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = GrainForce.force(this, acceleration)
@JvmName("usTonTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, UsTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = UsTonForce.force(this, acceleration)
@JvmName("imperialTonTimesAcceleration")
operator fun ScientificValue<MeasurementType.Weight, ImperialTon>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = ImperialTonForce.force(this, acceleration)
@JvmName("imperialWeightTimesImperialAcceleration")
operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.force(this, acceleration)
@JvmName("ukImperialWeightTimesImperialAcceleration")
operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.ukImperial.force(this, acceleration)
@JvmName("usCustomaryWeightTimesImperialAcceleration")
operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>) = PoundForce.usCustomary.force(this, acceleration)
@JvmName("weightTimesAcceleration")
operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Weight, WeightUnit>.times(acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>) = Newton.force(this, acceleration)
