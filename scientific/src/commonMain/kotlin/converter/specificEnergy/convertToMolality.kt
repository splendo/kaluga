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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molality.molality
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, MetricSpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("imperialSpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, ImperialSpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("ukImperialSpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, UKImperialSpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("usCustomarySpecificEnergyDivMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, USCustomarySpecificEnergy>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per unit.per).molality(this, molarEnergy)
@JvmName("specificEnergyDivMolarEnergy")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, MolarEnergyUnit : MolarEnergy> ScientificValue<MeasurementType.SpecificEnergy, SpecificEnergyUnit>.div(molarEnergy: ScientificValue<MeasurementType.MolarEnergy, MolarEnergyUnit>) = (molarEnergy.unit.per per Kilogram).molality(this, molarEnergy)
