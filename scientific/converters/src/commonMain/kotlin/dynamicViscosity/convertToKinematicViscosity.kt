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

package com.splendo.kaluga.scientific.converter.dynamicViscosity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.kinematicViscosity.kinematicViscosity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricDynamicViscosity
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDynamicViscosity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricDynamicViscosityDivMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, MetricDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (SquareMeter per unit.time).kinematicViscosity(this, density)

@JvmName("imperialDynamicViscosityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, ImperialDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("imperialDynamicViscosityDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, ImperialDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("imperialDynamicViscosityDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, ImperialDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("ukImperialDynamicViscosityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, UKImperialDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("ukImperialDynamicViscosityDivUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, UKImperialDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("usCustomaryDynamicViscosityDivImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, USCustomaryDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("usCustomaryDynamicViscosityDivUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, USCustomaryDynamicViscosity>.div(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (SquareFoot per unit.time).kinematicViscosity(this, density)

@JvmName("dynamicViscosityDivDensity")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, DensityUnit : Density> ScientificValue<PhysicalQuantity.DynamicViscosity, DynamicViscosityUnit>.div(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (SquareMeter per unit.time).kinematicViscosity(this, density)
