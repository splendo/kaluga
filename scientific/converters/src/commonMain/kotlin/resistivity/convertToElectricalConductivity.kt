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

package com.splendo.kaluga.scientific.converter.resistivity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricalConductivity.electricalConductivity
import com.splendo.kaluga.scientific.unit.Resistivity
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Siemens
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("resistivityElectricalConductivity")
fun <ResistivityUnit : Resistivity> ScientificValue<PhysicalQuantity.Resistivity, ResistivityUnit>.electricalConductivity() = (Siemens per Meter).electricalConductivity(this)
