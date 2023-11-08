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

package com.splendo.kaluga.scientific.converter.magneticInduction

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.magneticFlux.flux
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Gauss
import com.splendo.kaluga.scientific.unit.MagneticInduction
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.Weber
import kotlin.jvm.JvmName

@JvmName("gaussTimesSquareCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.MagneticInduction, Gauss>.times(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) = Maxwell.flux(this, area)

@JvmName("inductionTimesArea")
infix operator fun <InductionUnit : MagneticInduction, AreaUnit : Area> ScientificValue<PhysicalQuantity.MagneticInduction, InductionUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Weber.flux(this, area)
