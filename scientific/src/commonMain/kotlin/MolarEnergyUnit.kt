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

val MetricAndImperialMolarEnergyUnits: Set<MetricAndImperialMolarEnergy> = MetricAndImperialEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

val MetricMolarEnergyUnits: Set<MetricMolarEnergy> = MetricEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

val ImperialMolarEnergyUnits: Set<ImperialMolarEnergy> = ImperialEnergyUnits.flatMap { energy ->
    AmountOfSubstanceUnits.map { energy per it }
}.toSet()

val MolarEnergyUnits: Set<MolarEnergy> = MetricAndImperialMolarEnergyUnits +
    MetricMolarEnergyUnits.filter { it.energy !is MetricMetricAndImperialEnergyWrapper }.toSet() +
    ImperialMolarEnergyUnits.filter { it.energy !is ImperialMetricAndImperialEnergyWrapper }.toSet()

@Serializable
sealed class MolarEnergy : AbstractScientificUnit<MeasurementType.MolarEnergy>() {
    abstract val energy: Energy
    abstract val per: AmountOfSubstance
    override val type = MeasurementType.MolarEnergy
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricAndImperialMolarEnergy(override val energy: MetricAndImperialEnergy, override val per: AmountOfSubstance) : MolarEnergy(), MetricAndImperialScientificUnit<MeasurementType.MolarEnergy> {
    override val system = MeasurementSystem.MetricAndImperial
    val metric get() = energy.metric per per
    val imperial get() = energy.imperial per per
}
@Serializable
data class MetricMolarEnergy(override val energy: MetricEnergy, override val per: AmountOfSubstance) : MolarEnergy(), MetricScientificUnit<MeasurementType.MolarEnergy> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialMolarEnergy(override val energy: ImperialEnergy, override val per: AmountOfSubstance) : MolarEnergy(), ImperialScientificUnit<MeasurementType.MolarEnergy> {
    override val system = MeasurementSystem.Imperial
}

infix fun MetricAndImperialEnergy.per(amountOfSubstance: AmountOfSubstance) = MetricAndImperialMolarEnergy(this, amountOfSubstance)
infix fun MetricEnergy.per(amountOfSubstance: AmountOfSubstance) = MetricMolarEnergy(this, amountOfSubstance)
infix fun ImperialEnergy.per(amountOfSubstance: AmountOfSubstance) = ImperialMolarEnergy(this, amountOfSubstance)

@JvmName("molarEnergyFromEnergyAndAmountOfSubstance")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy
    > MolarEnergyUnit.molarEnergy(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = byDividing(energy, amountOfSubstance)

@JvmName("energyFromMolarEnergyAndAmountOfSubstance")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy
    > EnergyUnit.energy(
    molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>,
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = byMultiplying(molarEnergy, amountOfSubstance)

@JvmName("amountOfSubstanceFromEnergyAndMolarEnergy")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy
    > AmountOfSubstanceUnit.amountOfSubstance(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>
) = byDividing(energy, molarEnergy)

@JvmName("metricAndImperialEnergyDivAmountOfSubstance")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarEnergy(this, amountOfSubstance)
@JvmName("metricEnergyDivAmountOfSubstance")
infix operator fun <EnergyUnit : MetricEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarEnergy(this, amountOfSubstance)
@JvmName("imperialEnergyDivAmountOfSubstance")
infix operator fun <EnergyUnit : ImperialEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit per amountOfSubstance.unit).molarEnergy(this, amountOfSubstance)
@JvmName("energyDivAmountOfSubstance")
infix operator fun <EnergyUnit : Energy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (Joule per Mole).molarEnergy(this, amountOfSubstance)

@JvmName("metricAndImperialMolarEnergyTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit.energy).energy(this, amountOfSubstance)
@JvmName("metricMolarEnergyTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit.energy).energy(this, amountOfSubstance)
@JvmName("imperialMolarEnergyTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit.energy).energy(this, amountOfSubstance)
@JvmName("molarEnergyTimesAmountOfSubstance")
infix operator fun <MolarEnergyUnit : MolarEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.times(amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>) = (unit.energy).energy(this, amountOfSubstance)

@JvmName("amountOfSubstanceTimesMetricAndImperialMolarEnergy")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("amountOfSubstanceTimesMetricMolarEnergy")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy * this
@JvmName("amountOfSubstanceTimesImperialMolarEnergy")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("amountOfSubstanceTimesMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = molarEnergy * this

@JvmName("metricAndImperialEnergyDivMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("metricAndImperialEnergyDivMetricMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("metricAndImperialEnergyDivImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("metricEnergyDivMetricMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("imperialEnergyDivImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
@JvmName("energyDivMolarEnergy")
infix operator fun <EnergyUnit : Energy, MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = molarEnergy.unit.per.amountOfSubstance(this, molarEnergy)
