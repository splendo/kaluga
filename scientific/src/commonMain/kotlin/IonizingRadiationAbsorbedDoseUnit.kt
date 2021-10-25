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
sealed class IonizingRadiationAbsorbedDose : AbstractScientificUnit<MeasurementType.IonizingRadiationAbsorbedDose>(), MetricAndImperialScientificUnit<MeasurementType.IonizingRadiationAbsorbedDose>

@Serializable
object Gray : IonizingRadiationAbsorbedDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose> {
    override val symbol = "Gy"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.IonizingRadiationAbsorbedDose
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Nano(Gray)
@Serializable
object MicroGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Micro(Gray)
@Serializable
object MilliGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Milli(Gray)
@Serializable
object CentiGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Centi(Gray)
@Serializable
object DeciGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Deci(Gray)
@Serializable
object DecaGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Deca(Gray)
@Serializable
object HectoGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Hecto(Gray)
@Serializable
object KiloGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Kilo(Gray)
@Serializable
object MegaGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Mega(Gray)
@Serializable
object GigaGray : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Gray> by Giga(Gray)

@Serializable
object Rad : IonizingRadiationAbsorbedDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose> {
    const val GRAY_IN_RAD = 0.01
    override val symbol = "rad"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.IonizingRadiationAbsorbedDose
    override fun fromSIUnit(value: Decimal): Decimal = value / GRAY_IN_RAD.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * GRAY_IN_RAD.toDecimal()
}

@Serializable
object NanoRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Nano(Rad)
@Serializable
object MicroRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Micro(Rad)
@Serializable
object MilliRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Milli(Rad)
@Serializable
object CentiRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Centi(Rad)
@Serializable
object DeciRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Deci(Rad)
@Serializable
object DecaRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Deca(Rad)
@Serializable
object HectoRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Hecto(Rad)
@Serializable
object KiloRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Kilo(Rad)
@Serializable
object MegaRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Mega(Rad)
@Serializable
object GigaRad : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.IonizingRadiationAbsorbedDose, Rad> by Giga(Rad)

@JvmName("absorbedDoseFromEnergyAndWeight")
fun <
    EnergyUnit : Energy,
    WeightUnit : Weight,
    AbsorbedDoseUnit : IonizingRadiationAbsorbedDose
    >
    AbsorbedDoseUnit.absorbedDose(
    energy: ScientificValue<MeasurementType.Energy, EnergyUnit>,
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) : ScientificValue<MeasurementType.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit> = byDividing(energy, weight)

@JvmName("ergAbsorbedByGram")
infix fun ScientificValue<MeasurementType.Energy, Erg>.absorbedBy(gram: ScientificValue<MeasurementType.Weight, Gram>) = Rad.absorbedDose(this, gram)
@JvmName("ergMultipleAbsorbedByGram")
infix fun <ErgUnit> ScientificValue<MeasurementType.Energy, ErgUnit>.absorbedBy(gram: ScientificValue<MeasurementType.Weight, Gram>) where ErgUnit : Energy, ErgUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Energy, Erg> = Rad.absorbedDose(this, gram)
@JvmName("energyAbsorbedByWeight")
infix fun <EnergyUnit : Energy, WeightUnit : Weight> ScientificValue<MeasurementType.Energy, EnergyUnit>.absorbedBy(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = Gray.absorbedDose(this, weight)
