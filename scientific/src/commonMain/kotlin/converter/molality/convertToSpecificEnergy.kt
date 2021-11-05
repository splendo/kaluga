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

package com.splendo.kaluga.scientific.converter.molality

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.unit.ImperialMolality
import com.splendo.kaluga.scientific.unit.ImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricAndImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricMolality
import com.splendo.kaluga.scientific.unit.MetricMolarEnergy
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.UKImperialMolality
import com.splendo.kaluga.scientific.unit.USCustomaryMolality
import kotlin.jvm.JvmName

@JvmName("metricMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, MetricMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("imperialMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, ImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("ukImperialMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, UKImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("usCustomaryMolalityTimesMetricAndImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, USCustomaryMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricAndImperialMolarEnergy>) = molarEnergy * this
@JvmName("metricMolalityTimesMetricMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, MetricMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MetricMolarEnergy>) = molarEnergy * this
@JvmName("imperialMolalityTimesImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, ImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("ukImperialMolalityTimesImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, UKImperialMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("usCustomaryMolalityTimesImperialMolarEnergy")
infix operator fun ScientificValue<MeasurementType.Molality, USCustomaryMolality>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, ImperialMolarEnergy>) = molarEnergy * this
@JvmName("molalityTimesMolarEnergy")
infix operator fun <MolalityUnit : Molality, MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.Molality, MolalityUnit>.times(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = molarEnergy * this
