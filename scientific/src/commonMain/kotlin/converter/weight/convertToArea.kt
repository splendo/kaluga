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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricAreaDensity")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("imperialWeightDivImperialAreaDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("imperialWeightDivUKImperialAreaDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("imperialWeightDivUSCustomaryAreaDensity")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("ukImperialWeightDivImperialAreaDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("ukImperialWeightDivUKImperialAreaDensity")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("usCustomaryWeightDivImperialAreaDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("usCustomaryWeightDivUSCustomaryAreaDensity")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>
) = areaDensity.unit.per.area(this, areaDensity)

@JvmName("weightDivAreaDensity")
infix operator fun <WeightUnit : Weight, AreaDensityUnit : AreaDensity> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>
) = SquareMeter.area(this, areaDensity)
