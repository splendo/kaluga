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
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricMassFlowRate]
 */
val MetricMassFlowRateUnits: Set<MetricMassFlowRate> get() = MetricWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [ImperialMassFlowRate]
 */
val ImperialMassFlowRateUnits: Set<ImperialMassFlowRate> get() = ImperialWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [UKImperialMassFlowRate]
 */
val UKImperialMassFlowRateUnits: Set<UKImperialMassFlowRate> get() = UKImperialWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [USCustomaryMassFlowRate]
 */
val USCustomaryMassFlowRateUnits: Set<USCustomaryMassFlowRate> get() = USCustomaryWeightUnits.flatMap { weight ->
    TimeUnits.map { weight per it }
}.toSet()

/**
 * Set of all [MassFlowRate]
 */
val MassFlowRateUnits: Set<MassFlowRate> get() = MetricMassFlowRateUnits +
    ImperialMassFlowRateUnits +
    UKImperialMassFlowRateUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMassFlowRateUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.MassFlowRate]
 * SI unit is `Kilogram per Second`
 */
@Serializable
sealed class MassFlowRate : AbstractScientificUnit<PhysicalQuantity.MassFlowRate>() {

    /**
     * The [Weight] component
     */
    abstract val weight: Weight

    /**
     * The [Time] component
     */
    abstract val per: Time
    override val symbol: String by lazy { "${weight.symbol}/${per.symbol}" }
    override val quantity = PhysicalQuantity.MassFlowRate
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [MassFlowRate] for [MeasurementSystem.Metric]
 * @param weight the [MetricWeight] component
 * @param per the [Time] component
 */
@Serializable
data class MetricMassFlowRate(override val weight: MetricWeight, override val per: Time) : MassFlowRate(), MetricScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [MassFlowRate] for [MeasurementSystem.Imperial]
 * @param weight the [ImperialWeight] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialMassFlowRate(override val weight: ImperialWeight, override val per: Time) : MassFlowRate(), ImperialScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.Imperial

    /**
     * The [UKImperialMassFlowRate] equivalent to this [ImperialMassFlowRate]
     */
    val ukImperial get() = weight.ukImperial per per

    /**
     * The [USCustomaryMassFlowRate] equivalent to this [ImperialMassFlowRate]
     */
    val usCustomary get() = weight.usCustomary per per
}

/**
 * A [MassFlowRate] for [MeasurementSystem.USCustomary]
 * @param weight the [USCustomaryWeight] component
 * @param per the [Time] component
 */
@Serializable
data class USCustomaryMassFlowRate(override val weight: USCustomaryWeight, override val per: Time) : MassFlowRate(), USCustomaryScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.USCustomary
}

/**
 * A [MassFlowRate] for [MeasurementSystem.UKImperial]
 * @param weight the [UKImperialWeight] component
 * @param per the [Time] component
 */
@Serializable
data class UKImperialMassFlowRate(override val weight: UKImperialWeight, override val per: Time) : MassFlowRate(), UKImperialScientificUnit<PhysicalQuantity.MassFlowRate> {
    override val system = MeasurementSystem.UKImperial
}

/**
 * Gets a [MetricMassFlowRate] from a [MetricWeight] and a [Time]
 * @param time the [Time] component
 * @return the [MetricMassFlowRate] represented by the units
 */
infix fun MetricWeight.per(time: Time) = MetricMassFlowRate(this, time)

/**
 * Gets an [ImperialMassFlowRate] from an [ImperialWeight] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialMassFlowRate] represented by the units
 */
infix fun ImperialWeight.per(time: Time) = ImperialMassFlowRate(this, time)

/**
 * Gets a [USCustomaryMassFlowRate] from a [USCustomaryWeight] and a [Time]
 * @param time the [Time] component
 * @return the [USCustomaryMassFlowRate] represented by the units
 */
infix fun USCustomaryWeight.per(time: Time) = USCustomaryMassFlowRate(this, time)

/**
 * Gets a [UKImperialMassFlowRate] from a [UKImperialWeight] and a [Time]
 * @param time the [Time] component
 * @return the [UKImperialMassFlowRate] represented by the units
 */
infix fun UKImperialWeight.per(time: Time) = UKImperialMassFlowRate(this, time)
