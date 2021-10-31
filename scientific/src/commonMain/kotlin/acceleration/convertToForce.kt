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

package com.splendo.kaluga.scientific.acceleration

import com.splendo.kaluga.scientific.Acceleration
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
import com.splendo.kaluga.scientific.weight.times
import kotlin.jvm.JvmName

@JvmName("accelerationTimesGram")
infix operator fun ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Gram>) = mass * this
@JvmName("metricAccelerationTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("accelerationTimesPound")
infix operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Pound>) = PoundForce.force(mass, this)
@JvmName("accelerationTimesOunce")
infix operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Ounce>) = OunceForce.force(mass, this)
@JvmName("accelerationTimesGrain")
infix operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, Grain>) = GrainForce.force(mass, this)
@JvmName("accelerationTimesUSTon")
infix operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, UsTon>) = UsTonForce.force(mass, this)
@JvmName("accelerationTimesImperialTon")
infix operator fun ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, ImperialTon>) = ImperialTonForce.force(mass, this)
@JvmName("imperialAccelerationTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("imperialAccelerationTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("imperialAccelerationTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
@JvmName("accelerationTimesWeight")
infix operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<MeasurementType.Acceleration, AccelerationUnit>.times(mass: ScientificValue<MeasurementType.Weight, WeightUnit>) = mass * this
