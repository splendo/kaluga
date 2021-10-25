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
import kotlin.math.ln

@Serializable
sealed class Radioactivity : AbstractScientificUnit<MeasurementType.Radioactivity>(), MetricAndImperialScientificUnit<MeasurementType.Radioactivity>

@Serializable
object Becquerel : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity> {
    override val symbol = "Bq"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Nano(Becquerel)
@Serializable
object MicroBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Micro(Becquerel)
@Serializable
object MilliBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Milli(Becquerel)
@Serializable
object CentiBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Centi(Becquerel)
@Serializable
object DeciBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Deci(Becquerel)
@Serializable
object DecaBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Deca(Becquerel)
@Serializable
object HectoBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Hecto(Becquerel)
@Serializable
object KiloBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Kilo(Becquerel)
@Serializable
object MegaBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Mega(Becquerel)
@Serializable
object GigaBecquerel : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Becquerel> by Giga(Becquerel)

@Serializable
object Rutherford : Radioactivity() {
    private const val RUTHERFORD_IN_BECQUEREL = 0.000001
    override val symbol = "Rd"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value * RUTHERFORD_IN_BECQUEREL.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value / RUTHERFORD_IN_BECQUEREL.toDecimal()
}

@Serializable
object Curie : Radioactivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity> {
    private const val BECQUEREL_IN_CURIE = 3.7e10
    override val symbol = "Ci"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Radioactivity
    override fun fromSIUnit(value: Decimal): Decimal = value / BECQUEREL_IN_CURIE.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * BECQUEREL_IN_CURIE.toDecimal()
}

@Serializable
object NanoCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Nano(Curie)
@Serializable
object MicroCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Micro(Curie)
@Serializable
object MilliCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Milli(Curie)
@Serializable
object CentiCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Centi(Curie)
@Serializable
object DeciCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Deci(Curie)
@Serializable
object DecaCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Deca(Curie)
@Serializable
object HectoCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Hecto(Curie)
@Serializable
object KiloCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Kilo(Curie)
@Serializable
object MegaCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Mega(Curie)
@Serializable
object GigaCurie : Radioactivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Radioactivity, Curie> by Giga(Curie)

fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity
    > RadioactivityUnit.radioactivity(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit> = byInverting(time)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time
    > RadioactivityUnit.radioactivity(decays: Decimal, per: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit> = (decays / per.convertValue(Second))(Becquerel).convert(this)


fun <
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity
    > TimeUnit.time(radioactivity: ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>): ScientificValue<MeasurementType.Time, TimeUnit> = byInverting(radioactivity)

fun <
    RadioactivityUnit : Radioactivity,
    TimeUnit : Time
    > TimeUnit.time(decay: Decimal, at: ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>): ScientificValue<MeasurementType.Time, TimeUnit> = (decay / at.convertValue(Becquerel))(Second).convert(this)

fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity
    > RadioactivityUnit.radioactivity(
    substance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    halfLife: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit> = byDividing(ScientificValue(substance.value * AvogadroConstant * ln(2.0).toDecimal(), substance.unit), halfLife)

@JvmName("decimalDivTime")
infix fun <TimeUnit : Time> Decimal.decaysPer(time: ScientificValue<MeasurementType.Time, Time>): ScientificValue<MeasurementType.Radioactivity, Becquerel> = Becquerel.radioactivity (this, time)

@JvmName("radioactivityTimesTime")
operator fun <RadioactivityUnit : Radioactivity, TimeUnit : Time> ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>): Decimal = convertValue(Becquerel) * time.convertValue(Second)
@JvmName("timeTimesRadioactivity")
operator fun <RadioactivityUnit : Radioactivity, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(radioactivity: ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>): Decimal = radioactivity * this

@JvmName("decimalDivRadioactivity")
operator fun <RadioactivityUnit : Radioactivity> Decimal.div(radioactivity: ScientificValue<MeasurementType.Radioactivity, RadioactivityUnit>): ScientificValue<MeasurementType.Time, Second> = Second.time(this, radioactivity)
