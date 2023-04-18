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
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.unit.*
import kotlin.jvm.JvmName

@JvmName("metricDynamicViscosityDivMetricKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, MetricDynamicViscosity>.div(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, MetricKinematicViscosity>,
) = (Kilogram per CubicMeter).density(this, kinematicViscosity)

@JvmName("imperialDynamicViscosityDivImperialKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, ImperialDynamicViscosity>.div(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>,
) = (Pound per CubicFoot).density(this, kinematicViscosity)

@JvmName("ukImperialDynamicViscosityDivImperialKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, UKImperialDynamicViscosity>.div(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>,
) = (Pound.ukImperial per CubicFoot).density(this, kinematicViscosity)

@JvmName("usCustomaryDynamicViscosityDivImperialKinematicViscosity")
infix operator fun ScientificValue<PhysicalQuantity.DynamicViscosity, USCustomaryDynamicViscosity>.div(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>,
) = (Pound.usCustomary per CubicFoot).density(this, kinematicViscosity)

@JvmName("dynamicViscosityDivKinematicViscosity")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, DensityUnit : KinematicViscosity> ScientificValue<PhysicalQuantity.DynamicViscosity, DynamicViscosityUnit>.div(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, DensityUnit>,
) = (Kilogram per CubicMeter).density(this, kinematicViscosity)
