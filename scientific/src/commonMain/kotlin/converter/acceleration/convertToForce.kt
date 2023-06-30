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
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricAndImperialAcceleration
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricAndImperialAccelerationTimesKilogram")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Kilogram>,
) = mass * this

@JvmName("metricAndImperialAccelerationTimesTonne")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Tonne>,
) = mass * this

@JvmName("metricAndImperialAccelerationTimesGram")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Gram>,
) = mass * this

@JvmName("metricAndImperialAccelerationTimesMilligram")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Milligram>,
) = mass * this

@JvmName("metricAccelerationTimesGram")
infix operator fun <MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Gram>,
) = mass * this

@JvmName("metricAccelerationTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight, MetricAccelerationUnit : MetricAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("imperialAccelerationTimesPound")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Pound>,
) = PoundForce.force(mass, this)

@JvmName("imperialAccelerationTimesOunce")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Ounce>,
) = OunceForce.force(mass, this)

@JvmName("imperialAccelerationTimesGrain")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Grain>,
) = GrainForce.force(mass, this)

@JvmName("imperialAccelerationTimesUSTon")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, UsTon>,
) = UsTonForce.force(mass, this)

@JvmName("imperialAccelerationTimesImperialTon")
infix operator fun <ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, ImperialTon>,
) = ImperialTonForce.force(mass, this)

@JvmName("imperialAccelerationTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("imperialAccelerationTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("imperialAccelerationTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight, ImperialAccelerationUnit : ImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, ImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("metricAndImperialAccelerationTimesPound")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Pound>,
) = PoundForce.force(mass, this)

@JvmName("metricAndImperialAccelerationTimesOunce")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Ounce>,
) = OunceForce.force(mass, this)

@JvmName("metricAndImperialAccelerationTimesGrain")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, Grain>,
) = GrainForce.force(mass, this)

@JvmName("metricAndImperialAccelerationTimesUSTon")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, UsTon>,
) = UsTonForce.force(mass, this)

@JvmName("metricAndImperialAccelerationTimesImperialTon")
infix operator fun <MetricAndImperialAccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, MetricAndImperialAccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, ImperialTon>,
) = ImperialTonForce.force(mass, this)

@JvmName("metricAndImperialAccelerationTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight, AccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("metricAndImperialAccelerationTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight, AccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("metricAndImperialAccelerationTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight, AccelerationUnit : MetricAndImperialAcceleration> ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this

@JvmName("accelerationTimesWeight")
infix operator fun <WeightUnit : Weight, AccelerationUnit : Acceleration> ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>.times(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = mass * this
