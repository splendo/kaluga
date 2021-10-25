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

@Serializable
sealed class CatalysticActivity : AbstractScientificUnit<MeasurementType.CatalysticActivity>(), MetricAndImperialScientificUnit<MeasurementType.CatalysticActivity>

@Serializable
object Katal : CatalysticActivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity> {
    override val symbol = "kat"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.CatalysticActivity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Nano(Katal)
@Serializable
object MicroKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Micro(Katal)
@Serializable
object MilliKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Milli(Katal)
@Serializable
object CentiKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Centi(Katal)
@Serializable
object DeciKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Deci(Katal)
@Serializable
object DecaKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Deca(Katal)
@Serializable
object HectoKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Hecto(Katal)
@Serializable
object KiloKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Kilo(Katal)
@Serializable
object MegaKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Mega(Katal)
@Serializable
object GigaKatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Giga(Katal)

fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    TimeUnit : Time,
    CatalysisUnit : CatalysticActivity
    >
    CatalysisUnit.catalysticActivity(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byDividing(amountOfSubstance, time)

fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    TimeUnit : Time,
    CatalysisUnit : CatalysticActivity
    >
    TimeUnit.time(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>,
    catalysis: ScientificValue<MeasurementType.CatalysticActivity, CatalysisUnit>
) = byDividing(amountOfSubstance, catalysis)

@JvmName("amountOfSubstanceDivTime")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, TimeUnit : Time> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Katal.catalysticActivity(this, time)
@JvmName("amountOfSubstanceDivCatalysticActivity")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, CatalysisUnit : CatalysticActivity> ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>.div(catalysis: ScientificValue<MeasurementType.CatalysticActivity, CatalysisUnit>) = Second.time(this, catalysis)
