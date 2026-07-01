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

package com.splendo.kaluga.scientific.converter.thermalConductance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.thermalConductivity.thermalConductivity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialThermalConductance
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricThermalConductance
import com.splendo.kaluga.scientific.unit.UKImperialThermalConductance
import com.splendo.kaluga.scientific.unit.USCustomaryThermalConductance
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.ThermalConductance
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialThermalConductanceDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.ThermalConductance, MetricAndUKImperialThermalConductance>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).thermalConductivity(this, length)

@JvmName("metricAndUKImperialThermalConductanceDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.ThermalConductance, MetricAndUKImperialThermalConductance>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).thermalConductivity(this, length)

@JvmName("metricThermalConductanceDivMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.ThermalConductance, MetricThermalConductance>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).thermalConductivity(this, length)

@JvmName("ukImperialThermalConductanceDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.ThermalConductance, UKImperialThermalConductance>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).thermalConductivity(this, length)

@JvmName("usCustomaryThermalConductanceDivImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.ThermalConductance, USCustomaryThermalConductance>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (unit per length.unit).thermalConductivity(this, length)

@JvmName("thermalConductanceDivLength")
infix operator fun <ConductanceUnit : ThermalConductance, LengthUnit : Length> ScientificValue<PhysicalQuantity.ThermalConductance, ConductanceUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = (Watt per Kelvin per Meter).thermalConductivity(this, length)
