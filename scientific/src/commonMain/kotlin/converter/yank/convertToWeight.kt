/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.scientific.ImperialJolt
import com.splendo.kaluga.scientific.ImperialYank
import com.splendo.kaluga.scientific.Jolt
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricJolt
import com.splendo.kaluga.scientific.MetricYank
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialYank
import com.splendo.kaluga.scientific.USCustomaryYank
import com.splendo.kaluga.scientific.Yank
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.converter.weight.mass
import kotlin.jvm.JvmName

@JvmName("metricYankDivMetricJolt")
infix operator fun ScientificValue<MeasurementType.Yank, MetricYank>.div(jolt: ScientificValue<MeasurementType.Jolt, MetricJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("imperialYankDivImperialJolt")
infix operator fun ScientificValue<MeasurementType.Yank, ImperialYank>.div(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("ukImperialYankDivImperialJolt")
infix operator fun ScientificValue<MeasurementType.Yank, UKImperialYank>.div(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("usCustomaryYankDivImperialJolt")
infix operator fun ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(jolt: ScientificValue<MeasurementType.Jolt, ImperialJolt>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
@JvmName("yankDivJolt")
infix operator fun <YankUnit : Yank, JoltUnit : Jolt> ScientificValue<MeasurementType.Yank, YankUnit>.div(jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>) = (1.0(unit.force) / 1.0(jolt.unit.acceleration)).unit.mass(this, jolt)
