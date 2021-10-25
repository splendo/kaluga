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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
sealed class IonizingRadiationEquivalentDose : AbstractScientificUnit<MeasurementType.IonizingRadiationEquivalentDose>(), MetricAndImperialScientificUnit<MeasurementType.IonizingRadiationEquivalentDose>

@Serializable
object Sievert : IonizingRadiationEquivalentDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose> {
    override val symbol = "Sv"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.IonizingRadiationEquivalentDose
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Nano(Sievert)
@Serializable
object MicroSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Micro(Sievert)
@Serializable
object MilliSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Milli(Sievert)
@Serializable
object CentiSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Centi(Sievert)
@Serializable
object DeciSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Deci(Sievert)
@Serializable
object DecaSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Deca(Sievert)
@Serializable
object HectoSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Hecto(Sievert)
@Serializable
object KiloSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Kilo(Sievert)
@Serializable
object MegaSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Mega(Sievert)
@Serializable
object GigaSievert : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, Sievert> by Giga(Sievert)

@Serializable
object RoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose> {
    const val SIEVERT_IN_REM = 0.01
    override val symbol = "rem"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.IonizingRadiationEquivalentDose
    override fun fromSIUnit(value: Decimal): Decimal = value / SIEVERT_IN_REM.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * SIEVERT_IN_REM.toDecimal()
}

@Serializable
object NanoRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Nano(RoentgenEquivalentMan)
@Serializable
object MicroRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Micro(RoentgenEquivalentMan)
@Serializable
object MilliRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Milli(RoentgenEquivalentMan)
@Serializable
object CentiRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Centi(RoentgenEquivalentMan)
@Serializable
object DeciRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Deci(RoentgenEquivalentMan)
@Serializable
object DecaRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Deca(RoentgenEquivalentMan)
@Serializable
object HectoRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Hecto(RoentgenEquivalentMan)
@Serializable
object KiloRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Kilo(RoentgenEquivalentMan)
@Serializable
object MegaRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Mega(RoentgenEquivalentMan)
@Serializable
object GigaRoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Giga(RoentgenEquivalentMan)

@JvmName("equivalentDoseFromEnergyAndWeight")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    EquivalentDoseUnit : IonizingRadiationEquivalentDose
    >
    EquivalentDoseUnit.equivalentDose(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) : ScientificValue<MeasurementType.IonizingRadiationEquivalentDose, EquivalentDoseUnit> = byDividing(energy, weight)

@JvmName("ergEquivalentDoseByGram")
infix fun ScientificValue<MeasurementType.Energy, Erg>.equivalentDoseBy(gram: ScientificValue<MeasurementType.Weight, Gram>) = RoentgenEquivalentMan.equivalentDose(this, gram)
@JvmName("ergMultipleEquivalentDoseByGram")
infix fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.equivalentDoseBy(gram: ScientificValue<MeasurementType.Weight, Gram>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = RoentgenEquivalentMan.equivalentDose(this, gram)
@JvmName("energyEquivalentDoseByWeight")
infix fun <EnergyUnit : Energy, WeightUnit : Weight> ScientificValue<MeasurementType.Energy, EnergyUnit>.equivalentDoseBy(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Sievert.equivalentDose(this, weight)