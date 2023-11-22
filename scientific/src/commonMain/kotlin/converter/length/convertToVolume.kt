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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.Micrometer
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Nanometer
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareDecameter
import com.splendo.kaluga.scientific.unit.SquareDecimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareGigameter
import com.splendo.kaluga.scientific.unit.SquareHectometer
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareKilometer
import com.splendo.kaluga.scientific.unit.SquareMegameter
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SquareMicrometer
import com.splendo.kaluga.scientific.unit.SquareMile
import com.splendo.kaluga.scientific.unit.SquareMillimeter
import com.splendo.kaluga.scientific.unit.SquareNanometer
import com.splendo.kaluga.scientific.unit.SquareYard
import com.splendo.kaluga.scientific.unit.Yard
import kotlin.jvm.JvmName

@JvmName("meterTimesSquareMeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Meter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareMeter>) = area * this

@JvmName("nanometerTimesSquareNanometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Nanometer>.times(area: ScientificValue<PhysicalQuantity.Area, SquareNanometer>) = area * this

@JvmName("micrometerTimesSquareMicrometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Micrometer>.times(area: ScientificValue<PhysicalQuantity.Area, SquareMicrometer>) = area * this

@JvmName("millimeterTimesSquareMillimeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Millimeter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareMillimeter>) = area * this

@JvmName("centimeterTimesSquareCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Centimeter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) = area * this

@JvmName("decimeterTimesSquareDecimeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Decimeter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareDecimeter>) = area * this

@JvmName("decameterTimesSquareDecameter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Decameter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareDecameter>) = area * this

@JvmName("hectometerTimesSquarehectometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Hectometer>.times(area: ScientificValue<PhysicalQuantity.Area, SquareHectometer>) = area * this

@JvmName("kilometerTimesSquareKilometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Kilometer>.times(area: ScientificValue<PhysicalQuantity.Area, SquareKilometer>) = area * this

@JvmName("kilometerTimesSquareMegameter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Megameter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareMegameter>) = area * this

@JvmName("gigameterTimesSquareGigameter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Gigameter>.times(area: ScientificValue<PhysicalQuantity.Area, SquareGigameter>) = area * this

@JvmName("metricLengthTimesMetricArea")
infix operator fun <AreaUnit : MetricArea, HeightUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, HeightUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = area * this

@JvmName("inchTimesSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Length, Inch>.times(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) = area * this

@JvmName("footTimesSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Length, Foot>.times(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) = area * this

@JvmName("yardTimesSquareYard")
infix operator fun ScientificValue<PhysicalQuantity.Length, Yard>.times(area: ScientificValue<PhysicalQuantity.Area, SquareYard>) = area * this

@JvmName("mileTimesSquareMile")
infix operator fun ScientificValue<PhysicalQuantity.Length, Mile>.times(area: ScientificValue<PhysicalQuantity.Area, SquareMile>) = area * this

@JvmName("inchTimesAcre")
infix operator fun ScientificValue<PhysicalQuantity.Length, Inch>.times(area: ScientificValue<PhysicalQuantity.Area, Acre>) = area * this

@JvmName("footTimesAcre")
infix operator fun ScientificValue<PhysicalQuantity.Length, Foot>.times(area: ScientificValue<PhysicalQuantity.Area, Acre>) = area * this

@JvmName("imperialLengthTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea, HeightUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, HeightUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = area * this

@JvmName("lengthTimesArea")
infix operator fun <AreaUnit : Area, HeightUnit : Length> ScientificValue<PhysicalQuantity.Length, HeightUnit>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    area * this
