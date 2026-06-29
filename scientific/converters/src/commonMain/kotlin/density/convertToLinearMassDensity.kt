/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.linearMassDensity.linearMassDensity
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)

@JvmName("imperialDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)

@JvmName("ukImperialDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)

@JvmName("usCustomaryDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.weight per (1(unit.per) / area).unit).linearMassDensity(this, area)

@JvmName("densityTimesArea")
infix operator fun <DensityUnit : Density, AreaUnit : Area> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (Kilogram per Meter).linearMassDensity(this, area)
