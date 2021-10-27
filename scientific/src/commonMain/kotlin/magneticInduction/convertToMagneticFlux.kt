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

package com.splendo.kaluga.scientific.magneticInduction

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Gauss
import com.splendo.kaluga.scientific.MagneticInduction
import com.splendo.kaluga.scientific.Maxwell
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.Weber
import com.splendo.kaluga.scientific.magneticFlux.flux
import kotlin.jvm.JvmName

@JvmName("gaussTimesSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.MagneticInduction, Gauss>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Maxwell.flux(this, area)
@JvmName("inductionTimesArea")
infix operator fun <InductionUnit : MagneticInduction, AreaUnit : Area> ScientificValue<MeasurementType.MagneticInduction, InductionUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Weber.flux(this, area)
