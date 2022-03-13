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

package com.splendo.kaluga.scientific.converter.acceleration

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("accelerationTimesGram")
infix operator fun ScientificValue<PhysicalQuantity.Acceleration, MetricAcceleration>.times(mass: ScientificValue<PhysicalQuantity.Weight, Gram>) =
    mass * this

@JvmName("metricAccelerationTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Acceleration, MetricAcceleration>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = mass * this

@JvmName("accelerationTimesPound")
infix operator fun ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<PhysicalQuantity.Weight, Pound>) =
    PoundForce.force(mass, this)

@JvmName("accelerationTimesOunce")
infix operator fun ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<PhysicalQuantity.Weight, Ounce>) =
    OunceForce.force(mass, this)

@JvmName("accelerationTimesGrain")
infix operator fun ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<PhysicalQuantity.Weight, Grain>) =
    GrainForce.force(mass, this)

@JvmName("accelerationTimesUSTon")
infix operator fun ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<PhysicalQuantity.Weight, UsTon>) =
    UsTonForce.force(mass, this)

@JvmName("accelerationTimesImperialTon")
infix operator fun ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(mass: ScientificValue<PhysicalQuantity.Weight, ImperialTon>) =
    ImperialTonForce.force(mass, this)

@JvmName("imperialAccelerationTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = mass * this

@JvmName("imperialAccelerationTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = mass * this

@JvmName("imperialAccelerationTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Acceleration, ImperialAcceleration>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = mass * this

@JvmName("accelerationTimesWeight")
infix operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = mass * this
