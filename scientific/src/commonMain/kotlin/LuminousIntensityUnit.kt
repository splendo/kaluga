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
sealed class LuminousIntensity : AbstractScientificUnit<MeasurementType.LuminousIntensity>(), MetricAndImperialScientificUnit<MeasurementType.LuminousIntensity>

@Serializable
object Candela : LuminousIntensity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity> {
    override val symbol = "cd"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.LuminousIntensity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Nano(Candela)
@Serializable
object MicroCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Micro(Candela)
@Serializable
object MilliCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Milli(Candela)
@Serializable
object CentiCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Centi(Candela)
@Serializable
object DeciCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Deci(Candela)
@Serializable
object DecaCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Deca(Candela)
@Serializable
object HectoCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Hecto(Candela)
@Serializable
object KiloCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Kilo(Candela)
@Serializable
object MegaCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Mega(Candela)
@Serializable
object GigaCandela : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.LuminousIntensity, Candela> by Giga(Candela)

@JvmName("luminousFluxFromIntensityAndSolidAngle")
fun <
    IntensityUnit : LuminousIntensity,
    SolidAngleUnit : SolidAngle,
    FluxUnit : LuminousFlux
    >
    IntensityUnit.intensity(
    flux: ScientificValue<MeasurementType.LuminousFlux, FluxUnit>,
    solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>
) : ScientificValue<MeasurementType.LuminousIntensity, IntensityUnit> = byDividing(flux, solidAngle)

@JvmName("luminousFluxDivSolidAngle")
infix operator fun <FluxUnit : LuminousFlux, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Candela.intensity(this, solidAngle)
