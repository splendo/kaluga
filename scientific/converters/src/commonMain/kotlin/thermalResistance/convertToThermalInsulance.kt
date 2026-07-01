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

package com.splendo.kaluga.scientific.converter.thermalResistance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.thermalInsulance.thermalInsulance
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.MetricThermalResistance
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ThermalResistance
import com.splendo.kaluga.scientific.unit.UKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.USCustomaryThermalResistance
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialThermalResistanceTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.ThermalResistance, MetricAndUKImperialThermalResistance>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).thermalInsulance(this, area)

@JvmName("metricAndUKImperialThermalResistanceTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.ThermalResistance, MetricAndUKImperialThermalResistance>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).thermalInsulance(this, area)

@JvmName("metricThermalResistanceTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.ThermalResistance, MetricThermalResistance>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).thermalInsulance(this, area)

@JvmName("ukImperialThermalResistanceTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.ThermalResistance, UKImperialThermalResistance>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).thermalInsulance(this, area)

@JvmName("usCustomaryThermalResistanceTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.ThermalResistance, USCustomaryThermalResistance>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).thermalInsulance(this, area)

@JvmName("thermalResistanceTimesArea")
infix operator fun <ResistanceUnit : ThermalResistance, AreaUnit : Area> ScientificValue<PhysicalQuantity.ThermalResistance, ResistanceUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = ((Kelvin per Watt) x SquareMeter).thermalInsulance(this, area)
