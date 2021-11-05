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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

val MetricLuminousExposureUnits: Set<MetricLuminousExposure> get() = MetricIlluminanceUnits.flatMap { illuminance ->
    TimeUnits.map { illuminance x it }
}.toSet()

val ImperialLuminousExposureUnits: Set<ImperialLuminousExposure> get() = ImperialIlluminanceUnits.flatMap { illuminance ->
    TimeUnits.map { illuminance x it }
}.toSet()

val LuminousExposureUnits: Set<LuminousExposure> get() = MetricLuminousExposureUnits + ImperialLuminousExposureUnits

@Serializable
abstract class LuminousExposure : ScientificUnit<PhysicalQuantity.LuminousExposure> {
    abstract val illuminance: Illuminance
    abstract val time: Time
    override val symbol: String get() = "${illuminance.symbol}⋅${time.symbol}"
    override val type = PhysicalQuantity.LuminousExposure
    override fun fromSIUnit(value: Decimal): Decimal = illuminance.fromSIUnit(time.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = time.toSIUnit(illuminance.toSIUnit(value))
}

@Serializable
class MetricLuminousExposure(override val illuminance: MetricIlluminance, override val time: Time) : LuminousExposure(), MetricScientificUnit<PhysicalQuantity.LuminousExposure> {
    override val system = MeasurementSystem.Metric
}

@Serializable
class ImperialLuminousExposure(override val illuminance: ImperialIlluminance, override val time: Time) : LuminousExposure(), ImperialScientificUnit<PhysicalQuantity.LuminousExposure> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricIlluminance.x(time: Time) = MetricLuminousExposure(this, time)
infix fun ImperialIlluminance.x(time: Time) = ImperialLuminousExposure(this, time)
