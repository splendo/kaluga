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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.surfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.ElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.USCustomaryElectricChargeDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialLengthTimesImperialElectricChargeDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, ImperialElectricChargeDensity>,
) = (Coulomb per SquareFoot).surfaceChargeDensity(electricChargeDensity, this)

@JvmName("imperialLengthTimesUKImperialElectricChargeDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, UKImperialElectricChargeDensity>,
) = (Coulomb per SquareFoot).surfaceChargeDensity(electricChargeDensity, this)

@JvmName("imperialLengthTimesUSCustomaryElectricChargeDensity")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, USCustomaryElectricChargeDensity>,
) = (Coulomb per SquareFoot).surfaceChargeDensity(electricChargeDensity, this)

@JvmName("lengthTimesElectricChargeDensity")
infix operator fun <LengthUnit : Length, ElectricChargeDensityUnit : ElectricChargeDensity> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    electricChargeDensity: ScientificValue<PhysicalQuantity.ElectricChargeDensity, ElectricChargeDensityUnit>,
) = (Coulomb per SquareMeter).surfaceChargeDensity(electricChargeDensity, this)
