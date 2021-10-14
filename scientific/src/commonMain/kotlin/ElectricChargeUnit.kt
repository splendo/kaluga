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
import kotlinx.serialization.Serializable

@Serializable
sealed class ElectricCharge : AbstractScientificUnit<MeasurementType.ElectricCharge>(), SIScientificUnit<MeasurementType.ElectricCharge>

@Serializable
object Coulomb : ElectricCharge(), BaseMetricUnit<MeasurementType.ElectricCharge, MeasurementSystem.SI> {
    override val symbol = "C"
    override val system = MeasurementSystem.SI
    override val type = MeasurementType.ElectricCharge
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Nano(Coulomb)
@Serializable
object MicroCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Micro(Coulomb)
@Serializable
object MilliCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Milli(Coulomb)
@Serializable
object CentiCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Centi(Coulomb)
@Serializable
object DeciCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Deci(Coulomb)
@Serializable
object DecaCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Deca(Coulomb)
@Serializable
object HectoCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Hecto(Coulomb)
@Serializable
object KiloCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Kilo(Coulomb)
@Serializable
object MegaCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Mega(Coulomb)
@Serializable
object GigaCoulomb : ElectricCharge(), SystemScientificUnit<MeasurementSystem.SI, MeasurementType.ElectricCharge> by Giga(Coulomb)

fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
    >
    ChargeUnit.charge(
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.ElectricCharge, ChargeUnit> {
    val currentInAmpere = current.convertValue(Ampere)
    val timeInSeconds = time.convertValue(Second)
    return ScientificValue(currentInAmpere * timeInSeconds, Coulomb).convert(this)
}

fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
    >
    CurrentUnit.current(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    per: ScientificValue<MeasurementType.Time, TimeUnit>
) : ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit> {
    val chargeInCoulomb = charge.convertValue(Coulomb)
    val timeInSeconds = per.convertValue(Second)
    return ScientificValue(chargeInCoulomb / timeInSeconds, Ampere).convert(this)
}

fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
    >
    TimeUnit.duration(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) : ScientificValue<MeasurementType.Time, TimeUnit> {
    val chargeInCoulomb = charge.convertValue(Coulomb)
    val currentInAmpere = current.convertValue(Ampere)
    return ScientificValue(chargeInCoulomb / currentInAmpere, Second).convert(this)
}

infix operator fun <CurrentUnit : ElectricCurrent, TimeUnit : Time> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Coulomb.charge(this, time)
infix operator fun <ChargeUnit : ElectricCharge, TimeUnit : Time> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Ampere.current(this, time)
infix operator fun <ChargeUnit : ElectricCharge, CurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Second.duration(this, current)
