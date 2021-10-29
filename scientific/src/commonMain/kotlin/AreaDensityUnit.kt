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
import com.splendo.kaluga.scientific.area.times
import com.splendo.kaluga.scientific.volume.div
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

val MetricAreaDensityUnits = MetricWeightUnits.map { weight ->
    MetricAreaUnits.map { weight per it }
}.flatten().toSet()

val ImperialAreaDensityUnits = ImperialWeightUnits.map { weight ->
    ImperialAreaUnits.map { weight per it }
}.flatten().toSet()

val UKImperialAreaDensityUnits = UKImperialWeightUnits.map { weight ->
    ImperialAreaUnits.map { weight per it }
}.flatten().toSet()

val USCustomaryAreaDensityUnits = USCustomaryWeightUnits.map { weight ->
    ImperialAreaUnits.map { weight per it }
}.flatten().toSet()

val AreaDensityUnits: Set<AreaDensity> = MetricAreaDensityUnits +
    ImperialAreaDensityUnits +
    UKImperialAreaDensityUnits.filter { it.weight !is UKImperialImperialWeightWrapper }.toSet() +
    USCustomaryAreaDensityUnits.filter { it.weight !is USCustomaryImperialWeightWrapper }.toSet()

@Serializable
sealed class AreaDensity : AbstractScientificUnit<MeasurementType.AreaDensity>() {
    abstract val weight: Weight
    abstract val per: Area
    override val symbol: String by lazy { "${weight.symbol} / ${per.symbol}" }
    override val type = MeasurementType.AreaDensity
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(weight.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = weight.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricAreaDensity(override val weight: MetricWeight, override val per: MetricArea) : AreaDensity(), MetricScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialAreaDensity(override val weight: ImperialWeight, override val per: ImperialArea) : AreaDensity(), ImperialScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = weight.ukImperial per per
    val usCustomary get() = weight.usCustomary per per
}
@Serializable
data class USCustomaryAreaDensity(override val weight: USCustomaryWeight, override val per: ImperialArea) : AreaDensity(), USCustomaryScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.USCustomary
}
@Serializable
data class UKImperialAreaDensity(override val weight: UKImperialWeight, override val per: ImperialArea) : AreaDensity(), UKImperialScientificUnit<MeasurementType.AreaDensity> {
    override val system = MeasurementSystem.UKImperial
}

infix fun MetricWeight.per(area: MetricArea) = MetricAreaDensity(this, area)
infix fun ImperialWeight.per(area: ImperialArea) = ImperialAreaDensity(this, area)
infix fun USCustomaryWeight.per(area: ImperialArea) = USCustomaryAreaDensity(this, area)
infix fun UKImperialWeight.per(area: ImperialArea) = UKImperialAreaDensity(this, area)

@JvmName("areaDensityFromWeightAndArea")
fun <
    WeightUnit : Weight,
    AreaUnit : Area,
    AreaDensityUnit : AreaDensity
    > AreaDensityUnit.areaDensity(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byDividing(weight, area)

@JvmName("weightFromAreaDensityAndArea")
fun <
    WeightUnit : Weight,
    AreaUnit : Area,
    AreaDensityUnit : AreaDensity
    > WeightUnit.mass(
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byMultiplying(areaDensity, area)

@JvmName("areaFromWeightAndAreaDensity")
fun <
    WeightUnit : Weight,
    AreaUnit : Area,
    AreaDensityUnit : AreaDensity
    > AreaUnit.area(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>
) = byDividing(weight, areaDensity)

@JvmName("areaDensityFromDensityAndLength")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
    > AreaDensityUnit.areaDensity(
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byMultiplying(density, length)

@JvmName("densityFromAreaDensityAndLength")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
    > DensityUnit.density(
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>
) = byDividing(areaDensity, length)

@JvmName("lengthFromAreaDensityAndDensity")
fun <
    DensityUnit : Density,
    LengthUnit : Length,
    AreaDensityUnit : AreaDensity
    > LengthUnit.length(
    areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>,
    density: ScientificValue<MeasurementType.Density, DensityUnit>,
) = byDividing(areaDensity, density)

@JvmName("metricWeightDivMetricArea")
infix operator fun <WeightUnit : MetricWeight, AreaUnit : MetricArea> ScientificValue<MeasurementType.Weight, WeightUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).areaDensity(this, area)
@JvmName("imperialWeightDivImperialArea")
infix operator fun <WeightUnit : ImperialWeight, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Weight, WeightUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).areaDensity(this, area)
@JvmName("ukImperialWeightDivImperialArea")
infix operator fun <WeightUnit : UKImperialWeight, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Weight, WeightUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).areaDensity(this, area)
@JvmName("usCustomaryWeightDivImperialArea")
infix operator fun <WeightUnit : USCustomaryWeight, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Weight, WeightUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).areaDensity(this, area)
@JvmName("weightDivArea")
infix operator fun <WeightUnit : Weight, AreaUnit : Area> ScientificValue<MeasurementType.Weight, WeightUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (Kilogram per SquareMeter).areaDensity(this, area)

@JvmName("metricAreaDensityTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = unit.weight.mass(this, area)
@JvmName("imperialAreaDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = unit.weight.mass(this, area)
@JvmName("ukImperialAreaDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = unit.weight.mass(this, area)
@JvmName("usCustomaryAreaDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = unit.weight.mass(this, area)
@JvmName("areaDensityTimesArea")
infix operator fun <AreaDensityUnit : AreaDensity, AreaUnit : Area> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Kilogram.mass(this, area)

@JvmName("metricWeightDivMetricAreaDensity")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>) = areaDensity.unit.per.area(this, areaDensity)
@JvmName("imperialWeightDivImperialAreaDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = areaDensity.unit.per.area(this, areaDensity)
@JvmName("ukImperialWeightDivImperialAreaDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = areaDensity.unit.per.area(this, areaDensity)
@JvmName("ukImperialWeightDivUKImperialAreaDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>) = areaDensity.unit.per.area(this, areaDensity)
@JvmName("usCustomaryWeightDivImperialAreaDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>) = areaDensity.unit.per.area(this, areaDensity)
@JvmName("usCustomaryWeightDivUSCustomaryAreaDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>) = areaDensity.unit.per.area(this, areaDensity)
@JvmName("weightDivAreaDensity")
infix operator fun <WeightUnit : Weight, AreaDensityUnit : AreaDensity> ScientificValue<MeasurementType.Weight, WeightUnit>.div(areaDensity: ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>) = SquareMeter.area(this, areaDensity)

@JvmName("metricDensityTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Density, MetricDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("imperialDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Density, ImperialDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("ukImperialDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Density, UKImperialDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("usCustomaryDensityTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) / length).unit).areaDensity(this, length)
@JvmName("densityTimesLength")
infix operator fun <DensityUnit : Density, LengthUnit : Length> ScientificValue<MeasurementType.Density, DensityUnit>.times(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per SquareMeter).areaDensity(this, length)

@JvmName("metricLengthTimesMetricDensity")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = density * this
@JvmName("imperialLengthTimesImperialDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = density * this
@JvmName("imperialLengthTimesUKImperialDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = density * this
@JvmName("imperialLengthTimesUSCustomaryDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = density * this
@JvmName("lengthTimesDensity")
infix operator fun <DensityUnit : Density, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(density: ScientificValue<MeasurementType.Density, DensityUnit>) = density * this

@JvmName("metricAreaDensityDivMetricDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.div(density: ScientificValue<MeasurementType.Density, MetricDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("imperialAreaDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("imperialAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("imperialAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("ukImperialAreaDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("ukImperialAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("ukImperialAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("usCustomaryAreaDensityDivImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("usCustomaryAreaDensityDivUKImperialDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("usCustomaryAreaDensityDivUSCustomaryDensity")
infix operator fun ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)
@JvmName("areaDensityDivDensity")
infix operator fun <AreaDensityUnit : AreaDensity, DensityUnit : Density> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.div(density: ScientificValue<MeasurementType.Density, DensityUnit>) = (1(density.unit.per) / 1(unit.per)).unit.length(this, density)

@JvmName("metricAreaDensityDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.AreaDensity, MetricAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("imperialAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, ImperialAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("ukImperialAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.AreaDensity, UKImperialAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("usCustomaryAreaDensityDivImperialLength")
infix operator fun <LengthUnit : ImperialLength>  ScientificValue<MeasurementType.AreaDensity, USCustomaryAreaDensity>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (unit.weight per (1(unit.per) * 1(length.unit)).unit).density(this, length)
@JvmName("areaDensityDivDensity")
infix operator fun <AreaDensityUnit : AreaDensity, LengthUnit : Length> ScientificValue<MeasurementType.AreaDensity, AreaDensityUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = (Kilogram per CubicMeter).density(this, length)
