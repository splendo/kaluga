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
import com.splendo.kaluga.scientific.converter.kinematicViscosity.times
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialKinematicViscosity
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricKinematicViscosity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, MetricKinematicViscosity>,
) = kinematicViscosity * this

@JvmName("imperialDensityTimesImperialKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>,
) = kinematicViscosity * this

@JvmName("ukImperialDensityTimesImperialKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>,
) = kinematicViscosity * this

@JvmName("usCustomaryDensityTimesImperialKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>,
) = kinematicViscosity * this

@JvmName("densityTimesKinematicViscosity")
infix operator fun <DensityUnit : Density, KinematicViscosityUnit : KinematicViscosity> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, KinematicViscosityUnit>,
) = kinematicViscosity * this
