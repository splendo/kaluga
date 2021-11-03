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

val CatalysticActivityUnits: Set<CatalysticActivity> get() = setOf(
    Katal,
    Nanokatal,
    Microkatal,
    Millikatal,
    Centikatal,
    Decikatal,
    Decakatal,
    Hectokatal,
    Kilokatal,
    Megakatal,
    Gigakatal
)

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
object Nanokatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Nano(Katal)
@Serializable
object Microkatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Micro(Katal)
@Serializable
object Millikatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Milli(Katal)
@Serializable
object Centikatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Centi(Katal)
@Serializable
object Decikatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Deci(Katal)
@Serializable
object Decakatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Deca(Katal)
@Serializable
object Hectokatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Hecto(Katal)
@Serializable
object Kilokatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Kilo(Katal)
@Serializable
object Megakatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Mega(Katal)
@Serializable
object Gigakatal : CatalysticActivity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.CatalysticActivity, Katal> by Giga(Katal)
