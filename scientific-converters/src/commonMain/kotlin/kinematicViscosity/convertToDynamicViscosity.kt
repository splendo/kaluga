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

package com.splendo.kaluga.scientific.converter.kinematicViscosity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.dynamicViscosity.dynamicViscosity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialKinematicViscosity
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricKinematicViscosity
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricKinematicViscosityTimesMetricDensity")
infix operator fun ScientificValue<PhysicalQuantity.KinematicViscosity, MetricKinematicViscosity>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    (Pascal x unit.time).dynamicViscosity(this, density)

@JvmName("imperialKinematicViscosityTimesImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    (PoundSquareInch x unit.time).dynamicViscosity(this, density)

@JvmName("imperialKinematicViscosityTimesUKImperialDensity")
infix operator fun ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>.times(density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>) =
    (PoundSquareInch.ukImperial x unit.time).dynamicViscosity(this, density)

@JvmName("imperialKinematicViscosityTimesUSCustomaryDensity")
infix operator fun ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>.times(density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>) =
    (PoundSquareInch.usCustomary x unit.time).dynamicViscosity(this, density)

@JvmName("kinematicViscosityTimesDensity")
infix operator fun <KinematicViscosityUnit : KinematicViscosity, DensityUnit : Density> ScientificValue<PhysicalQuantity.KinematicViscosity, KinematicViscosityUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = (Pascal x unit.time).dynamicViscosity(this, density)
