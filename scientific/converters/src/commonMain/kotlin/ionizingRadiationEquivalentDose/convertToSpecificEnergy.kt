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

package com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentManMultiple
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("specificEnergyFromRoentgewnEquivalentMan")
fun ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>.asSpecificEnergy() = (Erg per Gram).specificEnergy(this)

@JvmName("specificEnergyFromRoentgenEquivalentManMultiple")
fun <RoentgenEquivalentManUnit : RoentgenEquivalentManMultiple> ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentManUnit>.asSpecificEnergy() =
    (Erg per Gram).specificEnergy(this)

@JvmName("specificEnergyFromEquivalentDose")
fun <EquivalentDoseUnit : IonizingRadiationEquivalentDose> ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>.asSpecificEnergy() =
    (Joule per Kilogram).specificEnergy(this)
