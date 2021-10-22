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
import kotlin.math.PI

@Serializable
sealed class Angle : AbstractScientificUnit<MeasurementType.Angle>(), MetricAndImperialScientificUnit<MeasurementType.Angle>

@Serializable
object Radian : Angle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle> {
    override val symbol = "rad"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Angle
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoRadian : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Radian> by Nano(Radian)
@Serializable
object MicroRadian : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Radian> by Micro(Radian)
@Serializable
object MilliRadian : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Radian> by Milli(Radian)
@Serializable
object CentiRadian : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Radian> by Centi(Radian)
@Serializable
object DeciRadian : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Radian> by Deci(Radian)

@Serializable
object Turn : Angle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle> {
    private const val RADIAN_IN_TURN = 2.0 * PI
    override val symbol = "tr"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Angle
    override fun fromSIUnit(value: Decimal): Decimal = value / RADIAN_IN_TURN.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * RADIAN_IN_TURN.toDecimal()
}

@Serializable
object MilliTurn : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Turn> by Milli(Turn)
@Serializable
object CentiTurn : Angle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.Angle, Turn> by Centi(Turn)

@Serializable
object Degree : Angle() {
    private const val DEGREE_IN_TURN = 360.0
    override val symbol = "°"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * DEGREE_IN_TURN.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / DEGREE_IN_TURN.toDecimal())
}

@Serializable
object Gradian : Angle() {
    private const val GRADIAN_IN_TURN = 400.0
    override val symbol = "gon"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * GRADIAN_IN_TURN.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / GRADIAN_IN_TURN.toDecimal())
}

@Serializable
object ArcMinute : Angle() {
    private const val ARCMINUTE_IN_TURN = 21600.0
    override val symbol = "′"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * ARCMINUTE_IN_TURN.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / ARCMINUTE_IN_TURN.toDecimal())
}

@Serializable
object ArcSecond : Angle() {
    private const val ARCSECOND_IN_TURN = 1296000.0
    override val symbol = "″"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.Angle
    override fun fromSIUnit(value: Decimal): Decimal = Turn.fromSIUnit(value) * ARCSECOND_IN_TURN.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = Turn.toSIUnit(value / ARCSECOND_IN_TURN.toDecimal())
}
