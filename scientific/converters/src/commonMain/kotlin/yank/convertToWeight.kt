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

package com.splendo.kaluga.scientific.converter.yank

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.ImperialJolt
import com.splendo.kaluga.scientific.unit.ImperialYank
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.MetricJolt
import com.splendo.kaluga.scientific.unit.MetricYank
import com.splendo.kaluga.scientific.unit.UKImperialYank
import com.splendo.kaluga.scientific.unit.USCustomaryYank
import com.splendo.kaluga.scientific.unit.Yank
import kotlin.jvm.JvmName

@JvmName("metricYankDivMetricJolt")
infix operator fun ScientificValue<PhysicalQuantity.Yank, MetricYank>.div(jolt: ScientificValue<PhysicalQuantity.Jolt, MetricJolt>) =
    (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)

@JvmName("imperialYankDivImperialJolt")
infix operator fun ScientificValue<PhysicalQuantity.Yank, ImperialYank>.div(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) =
    (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)

@JvmName("ukImperialYankDivImperialJolt")
infix operator fun ScientificValue<PhysicalQuantity.Yank, UKImperialYank>.div(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) =
    (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)

@JvmName("usCustomaryYankDivImperialJolt")
infix operator fun ScientificValue<PhysicalQuantity.Yank, USCustomaryYank>.div(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) =
    (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)

@JvmName("yankDivJolt")
infix operator fun <YankUnit : Yank, JoltUnit : Jolt> ScientificValue<PhysicalQuantity.Yank, YankUnit>.div(jolt: ScientificValue<PhysicalQuantity.Jolt, JoltUnit>) =
    (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
