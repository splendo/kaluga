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

package com.splendo.kaluga.scientific.converter.molarMass

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molality.molality
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMolarMassMolality")
fun ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>.molality() = (unit.per per unit.weight).molality(this)

@JvmName("imperialMolarMassMolality")
fun ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>.molality() = (unit.per per unit.weight).molality(this)

@JvmName("ukImperialMolarMassMolality")
fun ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>.molality() = (unit.per per unit.weight).molality(this)

@JvmName("usCustomaryMolarMassMolality")
fun ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>.molality() = (unit.per per unit.weight).molality(this)

@JvmName("molarMassMolality")
fun <MolarMassUnit : MolarMass> ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>.molality() = (unit.per per Kilogram).molality(this)
