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

package com.splendo.kaluga.scientific.converter.momentOfInertia

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.ImperialMomentOfInertia
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricMomentOfInertia
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.MomentOfInertia
import com.splendo.kaluga.scientific.unit.UKImperialMomentOfInertia
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryMomentOfInertia
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.SquareMeter
import kotlin.jvm.JvmName

@JvmName("metricMomentOfInertiaDivMetricMass")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, MetricMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("imperialMomentOfInertiaDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, ImperialMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("imperialMomentOfInertiaDivUKImperialMass")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, ImperialMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("imperialMomentOfInertiaDivUSCustomaryMass")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, ImperialMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("ukImperialMomentOfInertiaDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, UKImperialMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("ukImperialMomentOfInertiaDivUKImperialMass")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, UKImperialMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("usCustomaryMomentOfInertiaDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, USCustomaryMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("usCustomaryMomentOfInertiaDivUSCustomaryMass")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.MomentOfInertia, USCustomaryMomentOfInertia>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.area.area(this, mass)

@JvmName("momentOfInertiaDivMass")
infix operator fun <MomentOfInertiaUnit : MomentOfInertia, WeightUnit : Weight> ScientificValue<PhysicalQuantity.MomentOfInertia, MomentOfInertiaUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = SquareMeter.area(this, mass)
