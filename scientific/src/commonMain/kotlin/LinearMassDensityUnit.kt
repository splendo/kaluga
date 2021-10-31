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
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.volume.div
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricLinearMassDensityUnits = MetricWeightUnits.flatMap { weight ->
    MetricLengthUnits.map { weight per it }
}.toSet()

val ImperialLinearMassDensityUnits = ImperialWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

val UKImperialLinearMassDensityUnits = UKImperialWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

val USCustomaryLinearMassDensityUnits = USCustomaryWeightUnits.flatMap { weight ->
    ImperialLengthUnits.map { weight per it }
}.toSet()

val LinearMassDensityUnits: Set<LinearMassDensity> = MetricLinearMassDensityUnits +
    ImperialLinearMassDensityUnits +
    UKImperialLinearMassDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryLinearMassDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class LinearMassDensity : AbstractScientificUnit<MeasurementType.LinearMassDensity>() {
    abstract val weight: Weight
    abstract val per: Length
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.LinearMassDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricLinearMassDensity(override val weight: MetricWeight, override val per: MetricLength) : LinearMassDensity(), MetricScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialLinearMassDensity(override val weight: ImperialWeight, override val per: ImperialLength) : LinearMassDensity(), ImperialScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryLinearMassDensity(override val weight: USCustomaryWeight, override val per: ImperialLength) : LinearMassDensity(), USCustomaryScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialLinearMassDensity(override val weight: UKImperialWeight, override val per: ImperialLength) : LinearMassDensity(), UKImperialScientificUnit<MeasurementType.LinearMassDensity> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(length: MetricLength) = MetricLinearMassDensity(this, length)
infix fun ImperialWeight.per(length: ImperialLength) = ImperialLinearMassDensity(this, length)
infix fun USCustomaryWeight.per(length: ImperialLength) = USCustomaryLinearMassDensity(this, length)
infix fun UKImperialWeight.per(length: ImperialLength) = UKImperialLinearMassDensity(this, length)

@JvmName("linearMassDensityFromWeightAndArea")
fun <
    WeightUnit : Weight,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LinearMassDensityUnit.linearMassDensity(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byDividing(weight, length)

@JvmName("weightFromLinearMassDensityAndLength")
fun <
    WeightUnit : Weight,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > WeightUnit.mass(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byMultiplying(linearMassDensity, length)

@JvmName("lengthFromWeightAndLinearMassDensity")
fun <
    WeightUnit : Weight,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LengthUnit.length(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>
) = byDividing(weight, linearMassDensity)

@JvmName("linearMassDensityFromAreaDensityAndLength")
fun <
    AreaDensityUnit : AreaDensity,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LinearMassDensityUnit.linearMassDensity(
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byMultiplying(areaDensity, length)

@JvmName("areaDensityFromLinearMassDensityAndLength")
fun <
    AreaDensityUnit : AreaDensity,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > AreaDensityUnit.areaDensity(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byDividing(linearMassDensity, length)

@JvmName("lengthFromLinearMassDensityAndDensity")
fun <
    AreaDensityUnit : AreaDensity,
    LengthUnit : Length,
    LinearMassDensityUnit : LinearMassDensity
    > LengthUnit.length(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>,
) = byDividing(linearMassDensity, areaDensity)

@JvmName("metricWeightDivMetricLength")
infix operator fun <WeightUnit : MetricWeight, LengthUnit : MetricLength> ScientificValue<MeasurementType.Weight, WeightUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).linearMassDensity(this, length)
@JvmName("imperialWeightDivImperialLength")
infix operator fun <WeightUnit : ImperialWeight, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Weight, WeightUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).linearMassDensity(this, length)
@JvmName("ukImperialWeightDivImperialLength")
infix operator fun <WeightUnit : UKImperialWeight, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Weight, WeightUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).linearMassDensity(this, length)
@JvmName("usCustomaryWeightDivImperialLength")
infix operator fun <WeightUnit : USCustomaryWeight, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Weight, WeightUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit per length.unit).linearMassDensity(this, length)
@JvmName("weightDivLength")
infix operator fun <WeightUnit : Weight, LengthUnit : Length> ScientificValue<MeasurementType.Weight, WeightUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per Meter).linearMassDensity(this, length)

@JvmName("metricLinearMassDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = unit.weight.mass(this, length)
@JvmName("imperialLinearMassDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = unit.weight.mass(this, length)
@JvmName("ukImperialLinearMassDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = unit.weight.mass(this, length)
@JvmName("usCustomaryLinearMassDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = unit.weight.mass(this, length)
@JvmName("linearMassDensityTimesLength")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Kilogram.mass(this, length)

@JvmName("metricLengthTimesMetricLinearMassDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = linearMassDensity * this
@JvmName("imperialLengthTimesImperialLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity * this
@JvmName("imperialLengthUKImperialLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = linearMassDensity * this
@JvmName("imperialLengthTimesUSCustomaryLinearMassDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = linearMassDensity * this
@JvmName("lengthTimesLinearMassDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = linearMassDensity * this


@JvmName("metricWeightDivMetricLinearMassDensity")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("imperialWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("ukImperialWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("ukImperialWeightDivUKImperialLinearMassDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("usCustomaryWeightDivImperialLinearMassDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("usCustomaryWeightDivUSCustomaryLinearMassDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>) = linearMassDensity.unit.per.length(this, linearMassDensity)
@JvmName("weightDivLinearMassDensity")
infix operator fun <WeightUnit : Weight, LinearMassDensityUnit : LinearMassDensity> ScientificValue<MeasurementType.Weight, WeightUnit>.div(linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>) = Meter.length(this, linearMassDensity)

@JvmName("metricAreaDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("imperialAreaDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("ukImperialAreaDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("usCustomaryAreaDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).linearMassDensity(this, length)
@JvmName("areaDensityTimesLength")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per Meter).linearMassDensity(this, length)

@JvmName("metricLengthTimesMetricAreaDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = areaDensity * this
@JvmName("imperialLengthTimesImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = areaDensity * this
@JvmName("imperialLengthTimesUKImperialAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = areaDensity * this
@JvmName("imperialLengthTimesUSCustomaryAreaDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = areaDensity * this
@JvmName("lengthTimesAreaDensity")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = areaDensity * this

@JvmName("metricLinearMassDensityDivMetricAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("imperialLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("imperialLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("imperialLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("ukImperialLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomaryLinearMassDensityDivImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomaryLinearMassDensityDivUKImperialAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("usCustomaryLinearMassDensityDivUSCustomaryAreaDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)
@JvmName("linearMassDensityDivAreaDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, AreaDensityUnit : AreaDensity> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = (1(areaDensity.unit.per) / 1(unit.per)).unit.length(this, areaDensity)

@JvmName("metricLinearMassDensityDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)
@JvmName("imperialLinearMassDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)
@JvmName("ukImperialLinearMassDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)
@JvmName("usCustomaryLinearMassDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength>  ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).areaDensity(this, length)
@JvmName("linearMassDensityDivLength")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, LengthUnit : Length> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per SquareMeter).areaDensity(this, length)


@JvmName("linearMassDensityFromDensityAndLength")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity
    > LinearMassDensityUnit.linearMassDensity(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byMultiplying(density, area)

@JvmName("densityFromLinearMassDensityAndArea")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity
    > DensityUnit.density(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byDividing(linearMassDensity, area)

@JvmName("areaFromLinearMassDensityAndDensity")
fun <
    DensityUnit : Density,
    AreaUnit : Area,
    LinearMassDensityUnit : LinearMassDensity
    > AreaUnit.area(
    linearMassDensity: ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>,
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
) = byDividing(linearMassDensity, density)

@JvmName("metricDensityTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Density, MetricDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)
@JvmName("imperialDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Density, ImperialDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)
@JvmName("ukImperialDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Density, UKImperialDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)
@JvmName("usCustomaryDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)
@JvmName("densityTimesArea")
infix operator fun <DensityUnit : Density, AreaUnit : Area> ScientificValue<MeasurementType.Density, DensityUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (Kilogram per Meter).linearMassDensity(this, area)

@JvmName("metricAreaTimesMetricDensity")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = density * this
@JvmName("imperialAreaTimesImperialDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = density * this
@JvmName("imperialAreaTimesUKImperialDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = density * this
@JvmName("imperialAreaTimesUSCustomaryDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = density * this
@JvmName("areaTimesDensity")
infix operator fun <DensityUnit : Density, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(density: ScientificValue<MeasurementType.Density, DensityUnit>) = density * this

@JvmName("metricLinearMassDensityDivMetricDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("imperialLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("imperialLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("imperialLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("ukImperialLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("ukImperialLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("ukImperialLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("usCustomaryLinearMassDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("usCustomaryLinearMassDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("usCustomaryLinearMassDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)
@JvmName("linearMassDensityDivDensity")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, DensityUnit : Density> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (1(density.unit.per) / 1(unit.per)).unit.area(this, density)

@JvmName("metricLinearMassDensityDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.LinearMassDensity, MetricLinearMassDensity>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)
@JvmName("imperialLinearMassDensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.LinearMassDensity, ImperialLinearMassDensity>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)
@JvmName("ukImperialLinearMassDensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.LinearMassDensity, UKImperialLinearMassDensity>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)
@JvmName("usCustomaryLinearMassDensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea>  ScientificValue<MeasurementType.LinearMassDensity, USCustomaryLinearMassDensity>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)
@JvmName("linearMassDensityDivArea")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, AreaUnit : Area> ScientificValue<MeasurementType.LinearMassDensity, LinearMassDensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (Kilogram per CubicMeter).density(this, area)
