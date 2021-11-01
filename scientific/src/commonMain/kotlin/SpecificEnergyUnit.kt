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

val MetricSpecificEnergyUnits: Set<MetricSpecificEnergy> = MetricEnergyUnits.flatMap { energy ->
    MetricWeightUnits.map { energy per it }
}.toSet()

val ImperialSpecificEnergyUnits: Set<ImperialSpecificEnergy> = ImperialEnergyUnits.flatMap { energy ->
    ImperialWeightUnits.map { energy per it }
}.toSet()

val UKImperialSpecificEnergyUnits: Set<UKImperialSpecificEnergy> = ImperialEnergyUnits.flatMap { energy ->
    UKImperialWeightUnits.map { energy per it }
}.toSet()

val USCustomarySpecificEnergyUnits: Set<USCustomarySpecificEnergy> = ImperialEnergyUnits.flatMap { energy ->
    USCustomaryWeightUnits.map { energy per it }
}.toSet()

val SpecificEnergyUnits: Set<SpecificEnergy> = MetricSpecificEnergyUnits +
    ImperialSpecificEnergyUnits +
    UKImperialSpecificEnergyUnits.filter { it.per !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomarySpecificEnergyUnits.filter { it.per !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class SpecificEnergy : AbstractScientificUnit<MeasurementType.SpecificEnergy>() {
    abstract val energy: Energy
    abstract val per: Weight
    override val type = MeasurementType.SpecificEnergy
    override val symbol: String by lazy { "${energy.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(energy.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = energy.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricSpecificEnergy(override val energy: MetricEnergy, override val per: MetricWeight) : SpecificEnergy(), MetricScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialSpecificEnergy(override val energy: ImperialEnergy, override val per: ImperialWeight) : SpecificEnergy(), ImperialScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = energy per per.ukImperial
    val usCustomary get() = energy per per.usCustomary
}
@Serializable
data class UKImperialSpecificEnergy(override val energy: ImperialEnergy, override val per: UKImperialWeight) : SpecificEnergy(), UKImperialScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomarySpecificEnergy(override val energy: ImperialEnergy, override val per: USCustomaryWeight) : SpecificEnergy(), USCustomaryScientificUnit<MeasurementType.SpecificEnergy> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricAndImperialEnergy.per(weight: MetricWeight) = MetricSpecificEnergy(this.metric, weight)
infix fun MetricAndImperialEnergy.per(weight: ImperialWeight) = ImperialSpecificEnergy(this.imperial, weight)
infix fun MetricAndImperialEnergy.per(weight: UKImperialWeight) = UKImperialSpecificEnergy(this.imperial, weight)
infix fun MetricAndImperialEnergy.per(weight: USCustomaryWeight) = USCustomarySpecificEnergy(this.imperial, weight)
infix fun MetricEnergy.per(weight: MetricWeight) = MetricSpecificEnergy(this, weight)
infix fun ImperialEnergy.per(weight: ImperialWeight) = ImperialSpecificEnergy(this, weight)
infix fun ImperialEnergy.per(weight: UKImperialWeight) = UKImperialSpecificEnergy(this, weight)
infix fun ImperialEnergy.per(weight: USCustomaryWeight) = USCustomarySpecificEnergy(this, weight)

@JvmName("specificEnergyFromEnergyAndWeight")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    SpecificEnergyUnit : SpecificEnergy
    > SpecificEnergyUnit.specificEnergy(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byDividing(energy, weight)

@JvmName("energyFromSpecificEnergyAndWeight")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    SpecificEnergyUnit : SpecificEnergy
    > EnergyUnit.energy(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = byMultiplying(specificEnergy, weight)

@JvmName("weightFromEnergyAndSpecificEnergy")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    SpecificEnergyUnit : SpecificEnergy
    > WeightUnit.weight(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>
) = byDividing(energy, specificEnergy)

@JvmName("metricAndImperialEnergyDivMetricWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("metricAndImperialEnergyDivImperialWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("metricAndImperialEnergyDivUKImperialWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("metricAndImperialEnergyDivUSCustomaryWeight")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("metricEnergyDivMetricWeight")
infix operator fun <EnergyUnit : MetricEnergy, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("imperialEnergyDivImperialWeight")
infix operator fun <EnergyUnit : ImperialEnergy, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("imperialEnergyDivUKImperialWeight")
infix operator fun <EnergyUnit : ImperialEnergy, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("imperialEnergyDivUSCustomaryWeight")
infix operator fun <EnergyUnit : ImperialEnergy, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificEnergy(this, weight)
@JvmName("energyDivWeight")
infix operator fun <EnergyUnit : Energy, WeightUnit : Weight> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Joule per Kilogram).specificEnergy(this, weight)

@JvmName("metricSpecificEnergyTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("imperialSpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("imperialSpecificEnergyTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("imperialSpecificEnergyTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("ukImperialSpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("ukImperialSpecificEnergyTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("usCustomarySpecificEnergyTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("usCustomarySpecificEnergyTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)
@JvmName("specificEnergyTimesWeight")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, WeightUnit : Weight> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy).energy(this, weight)

@JvmName("metricWeightTimesMetricSpecificEnergy")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy * this
@JvmName("imperialWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("ukImperialWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("usCustomaryWeightTimesImperialSpecificEnergy")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("imperialWeightTimesUKImperialSpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy * this
@JvmName("ukImperialWeightTimesUKImperialSpecificEnergy")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy * this
@JvmName("imperialWeightTimesUSCustomarySpecificEnergy")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy * this
@JvmName("usCustomaryWeightTimesUSCustomarySpecificEnergy")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy * this
@JvmName("weightTimesSpecificEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = specificEnergy * this

@JvmName("metricAndImperialEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricAndImperialEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricAndImperialEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricAndImperialEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricAndImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("metricEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, MetricEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("imperialEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("imperialEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("imperialEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.Energy, ImperialEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy.unit.per.weight(this, specificEnergy)
@JvmName("energyDivSpecificEnergy")
infix operator fun <EnergyUnit : Energy, SpecificEnergyUnit : SpecificEnergy> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = specificEnergy.unit.per.weight(this, specificEnergy)

@JvmName("molarMassFromMolarEnergyAndSpecificEnergy")
fun <
    MolarEnergyUnit : MolarEnergy,
    SpecificEnergyUnit : SpecificEnergy,
    MolarMassUnit : MolarMass
    > MolarMassUnit.molarMass(
    molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>,
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>
) = byDividing(molarEnergy, specificEnergy)

@JvmName("molarEnergyFromMolarMassAndSpecificEnergy")
fun <
    MolarEnergyUnit : MolarEnergy,
    MolarMassUnit : MolarMass,
    SpecificEnergyUnit : SpecificEnergy
    > MolarEnergyUnit.molarEnergy(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>,
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>
) = byMultiplying(specificEnergy, molarMass)

@JvmName("specificEnergyFromMolarEnergyAndMolarMass")
fun <
    MolarEnergyUnit : MolarEnergy,
    MolarMassUnit : MolarMass,
    SpecificEnergyUnit : SpecificEnergy
    > SpecificEnergyUnit.specificEnergy(
    molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>,
    molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>
) = byDividing(molarEnergy, molarMass)

@JvmName("metricAndImperialMolarEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("metricAndImperialMolarEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("metricAndImperialMolarEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("metricAndImperialMolarEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("metricMolarEnergyDivMetricSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("imperialMolarEnergyDivImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("imperialMolarEnergyDivUKImperialSpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("imperialMolarEnergyDivUSCustomarySpecificEnergy")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = (specificEnergy.unit.per per unit.per).molarMass(this, specificEnergy)
@JvmName("molarEnergyDivSpecificEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy, SpecificEnergyUnit : SpecificEnergy> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.div(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = (Kilogram per Mole).molarMass(this, specificEnergy)

@JvmName("metricSpecificEnergyTimesMMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("imperialSpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("ukImperialSpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("usCustomarySpecificEnergyTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (unit.energy per molarMass.unit.per).molarEnergy(this, molarMass)
@JvmName("specificEnergyTimesMolarMass")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.times(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (Joule per molarMass.unit.per).molarEnergy(this, molarMass)

@JvmName("molarMassTimesMetricSpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesImperialSpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesUKImperialSpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesUSCustomarySpecificEnergy")
infix operator fun <MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>) = specificEnergy * this
@JvmName("molarMassTimesSpecificEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>) = specificEnergy * this

@JvmName("metricAndImperialMolarEnergyDivMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricAndImperialMolarEnergyDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricAndImperialMolarEnergyDivUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricAndImperialMolarEnergyDivUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("metricMolarEnergyDivMetricMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MetricMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("imperialMolarEnergyDivImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("imperialMolarEnergyDivUKImperialMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("imperialMolarEnergyDivUSCustomaryMolarMass")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.div(molarMass: ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>) = (unit.energy per molarMass.unit.weight).specificEnergy(this, molarMass)
@JvmName("molarEnergyDivMolarMass")
infix operator fun <MolarEnergyUnit : MolarEnergy, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = (Joule per Kilogram).specificEnergy(this, molarMass)

@JvmName("molarEnergyFromSpecificEnergyAndMolality")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    MolalityUnit : Molality,
    MolarEnergyUnit : MolarEnergy
    > MolarEnergyUnit.molarEnergy(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>,
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>
) = byDividing(specificEnergy, molality)

@JvmName("specificEnergyFromMolarEnergyAndMolality")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    MolarEnergyUnit : MolarEnergy,
    MolalityUnit : Molality
    > SpecificEnergyUnit.specificEnergy(
    molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>,
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>
) = byMultiplying(molarEnergy, molality)

@JvmName("molalityFromSpecificEnergyAndMolarEnergy")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    MolarEnergyUnit : MolarEnergy,
    MolalityUnit : Molality
    > MolalityUnit.molality(
    specificEnergy: ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>,
    molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>
) = byDividing(specificEnergy, molarEnergy)

@JvmName("metricSpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("imperialSpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("ukImperialSpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("usCustomarySpecificEnergyDivMolality")
infix operator fun <MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (unit.energy per molality.unit.amountOfSubstance).molarEnergy(this, molality)
@JvmName("specificEnergyDivMolality")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolalityUnit : Molality> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.div(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (Joule per molality.unit.amountOfSubstance).molarEnergy(this, molality)

@JvmName("metricAndImperialMolarEnergyTimesMetricMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricAndImperialMolarEnergyTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricAndImperialMolarEnergyTimesUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricAndImperialMolarEnergyTimesUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("metricMolarEnergyTimesMetricMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, MetricMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("imperialMolarEnergyTimesImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, ImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("imperialMolarEnergyTimesUKImperialMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, UKImperialMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("imperialMolarEnergyTimesUSCustomaryMolality")
infix operator fun ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>.times(molality: ScientificValue<MeasurementType.Molality, USCustomaryMolality>) = (unit.energy per molality.unit.per).specificEnergy(this, molality)
@JvmName("molarEnergyTimesMolality")
infix operator fun <MolarEnergyUnit : MolarEnergy, MolalityUnit : Molality> ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = (Joule per Kilogram).specificEnergy(this, molality)

@JvmName("metricMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, MetricMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("imperialMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, ImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("ukImperialMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, UKImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("usCustomaryMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, USCustomaryMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("metricMolalityTimesMetricMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, MetricMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy * this
@JvmName("imperialMolalityTimesImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, ImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("ukImperialMolalityTimesImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, UKImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("usCustomaryMolalityTimesImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, USCustomaryMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("molalityTimesMolarEnergy")
infix operator fun <MolalityUnit : Molality, MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = molarEnergy * this

@JvmName("metricSpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("imperialSpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("ukImperialSpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("usCustomarySpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("specificEnergyDivMolarEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per Kilogram).molality(this, molarEnergy)
