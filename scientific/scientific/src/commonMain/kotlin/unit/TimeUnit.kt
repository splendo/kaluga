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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [Time]
 */
val TimeUnits: Set<Time> get() = setOf(
    Second,
    Nanosecond,
    Microsecond,
    Millisecond,
    Centisecond,
    Decisecond,
    Minute,
    Hour,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.Time]
 * SI unit is [Second]
 */
@Serializable
sealed class Time :
    DefinedScientificUnit<PhysicalQuantity.Time>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.Time>

@Serializable
data object Second : Time(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time> {
    override val symbol = "s"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Time
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class SecondMultiple :
    Time(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second>

@Serializable
data object Decisecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Deci(Second)

@Serializable
data object Centisecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Centi(Second)

@Serializable
data object Millisecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Milli(Second)

@Serializable
data object Microsecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Micro(Second)

@Serializable
data object Nanosecond : SecondMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.Time, Second> by Nano(Second)

@Serializable
data object Minute : Time() {
    private val SECOND_PER_MINUTE = 60.toDecimal()
    override val symbol: String = "min"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Time
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_MINUTE
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_MINUTE
}

@Serializable
data object Hour : Time() {
    private val SECOND_PER_HOUR = 3600.toDecimal()
    override val symbol: String = "h"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.Time
    override fun fromSIUnit(value: Decimal): Decimal = value / SECOND_PER_HOUR
    override fun toSIUnit(value: Decimal): Decimal = value * SECOND_PER_HOUR
}

internal fun SerializersModuleBuilder.setupForTime() {
    polymorphic(Time::class) {
        registerTimeClasses()
    }
}

internal fun PolymorphicModuleBuilder<Time>.registerTimeClasses() {
    subclass(Hour::class, Hour.serializer())
    subclass(Minute::class, Minute.serializer())
    subclass(Second::class, Second.serializer())
    subclass(Centisecond::class, Centisecond.serializer())
    subclass(Decisecond::class, Decisecond.serializer())
    subclass(Microsecond::class, Microsecond.serializer())
    subclass(Millisecond::class, Millisecond.serializer())
    subclass(Nanosecond::class, Nanosecond.serializer())
}
