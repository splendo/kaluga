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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricMassFlowRateUnits = MetricWeightUnits.map { weight ->
    TimeUnits.map { weight per it }
}.flatten().toSet()

val ImperialMassFlowRateUnits = ImperialWeightUnits.map { weight ->
    TimeUnits.map { weight per it }
}.flatten().toSet()

val UKImperialMassFlowRateUnits = UKImperialWeightUnits.map { weight ->
    TimeUnits.map { weight per it }
}.flatten().toSet()

val USCustomaryMassFlowRateUnits = USCustomaryWeightUnits.map { weight ->
    TimeUnits.map { weight per it }
}.flatten().toSet()

val MassFlowRateUnits: Set<MassFlowRate> = MetricMassFlowRateUnits +
    ImperialMassFlowRateUnits +
    UKImperialMassFlowRateUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryMassFlowRateUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class MassFlowRate : AbstractScientificUnit<MeasurementType.MassFlowRate>() {
    abstract val weight: Weight
    abstract val per: Time
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.MassFlowRate
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricMassFlowRate(override val weight: MetricWeight, override val per: Time) : MassFlowRate(), MetricScientificUnit<MeasurementType.MassFlowRate> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMassFlowRate(override val weight: ImperialWeight, override val per: Time) : MassFlowRate(), ImperialScientificUnit<MeasurementType.MassFlowRate> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryMassFlowRate(override val weight: USCustomaryWeight, override val per: Time) : MassFlowRate(), USCustomaryScientificUnit<MeasurementType.MassFlowRate> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialMassFlowRate(override val weight: UKImperialWeight, override val per: Time) : MassFlowRate(), UKImperialScientificUnit<MeasurementType.MassFlowRate> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(time: Time) = MetricMassFlowRate(this, time)
infix fun ImperialWeight.per(time: Time) = ImperialMassFlowRate(this, time)
infix fun USCustomaryWeight.per(time: Time) = USCustomaryMassFlowRate(this, time)
infix fun UKImperialWeight.per(time: Time) = UKImperialMassFlowRate(this, time)

@JvmName("massFlowRateFromWeightAndArea")
fun <
    WeightUnit : Weight,
    TimeUnit : Time,
    MassFlowRateUnit : MassFlowRate
    > MassFlowRateUnit.massFlowRate(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(weight, time)

@JvmName("weightFromMassFlowRateAndTime")
fun <
    WeightUnit : Weight,
    TimeUnit : Time,
    MassFlowRateUnit : MassFlowRate
    > WeightUnit.mass(
    massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(massFlowRate, time)

@JvmName("timeFromWeightAndMassFlowRate")
fun <
    WeightUnit : Weight,
    TimeUnit : Time,
    MassFlowRateUnit : MassFlowRate
    > TimeUnit.time(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>
) = byDividing(weight, massFlowRate)

@JvmName("metricWeightDivTime")
infix operator fun <WeightUnit : MetricWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("imperialWeightDivTime")
infix operator fun <WeightUnit : ImperialWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("ukImperialWeightDivTime")
infix operator fun <WeightUnit : UKImperialWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("usCustomaryWeightDivTime")
infix operator fun <WeightUnit : USCustomaryWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("weightDivTime")
infix operator fun <WeightUnit : Weight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Kilogram per time.unit).massFlowRate(this, time)

@JvmName("metricMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.MassFlowRate, MetricMassFlowRate>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.weight.mass(this, time)
@JvmName("imperialMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.weight.mass(this, time)
@JvmName("ukImperialMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.MassFlowRate, UKImperialMassFlowRate>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.weight.mass(this, time)
@JvmName("usCustomaryMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.MassFlowRate, USCustomaryMassFlowRate>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = unit.weight.mass(this, time)
@JvmName("massFlowRateTimesTime")
infix operator fun <MassFlowRateUnit : MassFlowRate, TimeUnit : Time> ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Kilogram.mass(this, time)

@JvmName("timeTimesMetricMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MetricMassFlowRate>) = massFlowRate * this
@JvmName("timeTimesImperialMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate * this
@JvmName("timeUKImperialMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, UKImperialMassFlowRate>) = massFlowRate * this
@JvmName("timeTimesUSCustomaryMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, USCustomaryMassFlowRate>) = massFlowRate * this
@JvmName("timeTimesMassFlowRate")
infix operator fun <MassFlowRateUnit : MassFlowRate, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>) = massFlowRate * this

@JvmName("metricWeightDivMetricMassFlowRate")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MetricMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("imperialWeightDivImperialMassFlowRate")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("ukImperialWeightDivImperialMassFlowRate")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("ukImperialWeightDivUKImperialMassFlowRate")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, UKImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("usCustomaryWeightDivImperialMassFlowRate")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("usCustomaryWeightDivUSCustomaryMassFlowRate")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, USCustomaryMassFlowRate>) = massFlowRate.unit.per.time(this, massFlowRate)
@JvmName("weightDivMassFlowRate")
infix operator fun <WeightUnit : Weight, MassFlowRateUnit : MassFlowRate> ScientificValue<MeasurementType.Weight, WeightUnit>.div(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>) = massFlowRate.unit.per.time(this, massFlowRate)
