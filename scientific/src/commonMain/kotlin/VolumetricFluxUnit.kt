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

val MetricVolumetricFluxUnits: Set<MetricVolumetricFlux> = MetricVolumetricFlowUnits.map { volumetricFlow ->
    MetricAreaUnits.map { volumetricFlow per it }
}.flatten().toSet()

val ImperialVolumetricFluxUnits: Set<ImperialVolumetricFlux> = ImperialVolumetricFlowUnits.map { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.flatten().toSet()

val UKImperialVolumetricFluxUnits: Set<UKImperialVolumetricFlux> = UKImperialVolumetricFlowUnits.map { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.flatten().toSet()

val USCustomaryVolumetricFluxUnits: Set<USCustomaryVolumetricFlux> = USCustomaryVolumetricFlowUnits.map { volumetricFlow ->
    ImperialAreaUnits.map { volumetricFlow per it }
}.flatten().toSet()

val VolumetricFluxUnits: Set<VolumetricFlux> = MetricVolumetricFluxUnits +
    ImperialVolumetricFluxUnits +
    UKImperialVolumetricFluxUnits.filter { it.volumetricFlow.volume !is UKImperialImperialVolumeWrapper }.toSet() +
    USCustomaryVolumetricFluxUnits.filter { it.volumetricFlow.volume !is USCustomaryImperialVolumeWrapper }.toSet()

@Serializable
sealed class VolumetricFlux : AbstractScientificUnit<MeasurementType.VolumetricFlux>() {
    abstract val volumetricFlow: VolumetricFlow
    abstract val per: Area
    override val type = MeasurementType.VolumetricFlux
    override val symbol: String by lazy { "${volumetricFlow.symbol}⋅${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(volumetricFlow.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = volumetricFlow.toSIUnit(per.fromSIUnit(value))
}

@Serializable
data class MetricVolumetricFlux(override val volumetricFlow: MetricVolumetricFlow, override val per: MetricArea) : VolumetricFlux(), MetricScientificUnit<MeasurementType.VolumetricFlux> {
    override val system = MeasurementSystem.Metric
}
@Serializable
data class ImperialVolumetricFlux(override val volumetricFlow: ImperialVolumetricFlow, override val per: ImperialArea) : VolumetricFlux(), ImperialScientificUnit<MeasurementType.VolumetricFlux> {
    override val system = MeasurementSystem.Imperial
    val ukImperial get() = volumetricFlow.ukImperial per per
    val usCustomary get() = volumetricFlow.usCustomary per per
}
@Serializable
data class UKImperialVolumetricFlux(override val volumetricFlow: UKImperialVolumetricFlow, override val per: ImperialArea) : VolumetricFlux(), UKImperialScientificUnit<MeasurementType.VolumetricFlux> {
    override val system = MeasurementSystem.UKImperial
}
@Serializable
data class USCustomaryVolumetricFlux(override val volumetricFlow: USCustomaryVolumetricFlow, override val per: ImperialArea) : VolumetricFlux(), USCustomaryScientificUnit<MeasurementType.VolumetricFlux> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricVolumetricFlow.per(area: MetricArea) = MetricVolumetricFlux(this, area)
infix fun ImperialVolumetricFlow.per(area: ImperialArea) = ImperialVolumetricFlux(this, area)
infix fun UKImperialVolumetricFlow.per(area: ImperialArea) = UKImperialVolumetricFlux(this, area)
infix fun USCustomaryVolumetricFlow.per(area: ImperialArea) = USCustomaryVolumetricFlux(this, area)

@JvmName("volumetricFluxFromVolumetricFlowAndArea")
fun <
    VolumetricFlowUnit : VolumetricFlow,
    AreaUnit : Area,
    VolumetricFluxUnit : VolumetricFlux
    > VolumetricFluxUnit.volumetricFlux(
    volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byDividing(volumetricFlow, area)

@JvmName("volumetricFlowFromVolumetricFluxAndArea")
fun <
    VolumetricFlowUnit : VolumetricFlow,
    AreaUnit : Area,
    VolumetricFluxUnit : VolumetricFlux
    > VolumetricFlowUnit.volumetricFlow(
    volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) = byMultiplying(volumetricFlux, area)

@JvmName("areaFromVolumetricFlowAndVolumetricFlux")
fun <
    VolumetricFlowUnit : VolumetricFlow,
    AreaUnit : Area,
    VolumetricFluxUnit : VolumetricFlux
    > AreaUnit.area(
    volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>,
    volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>
) = byDividing(volumetricFlow, volumetricFlux)

@JvmName("metricVolumetricFlowDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("imperialVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("ukImperialVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("usCustomaryVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("volumetricFlowDivArea")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, AreaUnit : Area> ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (CubicMeter per Second per SquareMeter).volumetricFlux(this, area)

@JvmName("metricVolumetricFluxTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.VolumetricFlux, MetricVolumetricFlux>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.volumetricFlow).volumetricFlow(this, area)
@JvmName("imperialVolumetricFluxTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.volumetricFlow).volumetricFlow(this, area)
@JvmName("ukImperialVolumetricFluxTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.volumetricFlow).volumetricFlow(this, area)
@JvmName("usCustomaryVolumetricFluxTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.volumetricFlow).volumetricFlow(this, area)
@JvmName("volumetricFluxTimesArea")
infix operator fun <VolumetricFluxUnit : VolumetricFlux, AreaUnit : Area> ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit.volumetricFlow).volumetricFlow(this, area)

@JvmName("metricAreaTimesMetricVolumetricFlux")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, MetricVolumetricFlux>) = volumetricFlux * this
@JvmName("imperialAreaTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux * this
@JvmName("imperialAreaTimesUKImperialAreaT")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux * this
@JvmName("imperialAreaTimesUSCustomaryArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux * this
@JvmName("areaTimesVolumetricFlux")
infix operator fun <VolumetricFluxUnit : VolumetricFlux, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>) = volumetricFlux * this

@JvmName("metricVolumetricFlowDivMetricVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, MetricVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("imperialVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("imperialVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("imperialVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("ukImperialVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("ukImperialVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("ukImperialVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("usCustomaryVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("usCustomaryVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("usCustomaryVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("volumetricFlowDivVolumetricFlux")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, VolumetricFluxUnit : VolumetricFlux> ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>) = volumetricFlux.unit.per.area(this, volumetricFlux)
