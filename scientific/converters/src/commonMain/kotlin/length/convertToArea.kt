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
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
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

@JvmName("meterTimesMeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Meter>.times(width: ScientificValue<PhysicalQuantity.Length, Meter>) = SquareMeter.area(this, width)

@JvmName("nanometerTimesNanometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Nanometer>.times(width: ScientificValue<PhysicalQuantity.Length, Nanometer>) = SquareNanometer.area(this, width)

@JvmName("micrometerTimesMicrometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Micrometer>.times(width: ScientificValue<PhysicalQuantity.Length, Micrometer>) = SquareMicrometer.area(this, width)

@JvmName("millimeterTimesMillimeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Millimeter>.times(width: ScientificValue<PhysicalQuantity.Length, Millimeter>) = SquareMillimeter.area(this, width)

@JvmName("centimeterTimesCentieter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Centimeter>.times(width: ScientificValue<PhysicalQuantity.Length, Centimeter>) = SquareCentimeter.area(this, width)

@JvmName("decimeterTimesDecimeter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Decimeter>.times(width: ScientificValue<PhysicalQuantity.Length, Decimeter>) = SquareDecimeter.area(this, width)

@JvmName("decameterTimesDecameter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Decameter>.times(width: ScientificValue<PhysicalQuantity.Length, Decameter>) = SquareDecameter.area(this, width)

@JvmName("hectometerTimesHectometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Hectometer>.times(width: ScientificValue<PhysicalQuantity.Length, Hectometer>) = SquareHectometer.area(this, width)

@JvmName("kilometerTimesKilometer")
infix operator fun ScientificValue<PhysicalQuantity.Length, Kilometer>.times(width: ScientificValue<PhysicalQuantity.Length, Kilometer>) = SquareKilometer.area(this, width)

@JvmName("megameterTimesMegameter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Megameter>.times(width: ScientificValue<PhysicalQuantity.Length, Megameter>) = SquareMegameter.area(this, width)

@JvmName("gigameterTimesGigameter")
infix operator fun ScientificValue<PhysicalQuantity.Length, Gigameter>.times(width: ScientificValue<PhysicalQuantity.Length, Gigameter>) = SquareGigameter.area(this, width)

@JvmName("metricLengthTimesMetricLength")
infix operator fun <LengthUnit : MetricLength, WidthUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    width: ScientificValue<PhysicalQuantity.Length, WidthUnit>,
) = SquareMeter.area(this, width)

@JvmName("inchTimesInch")
infix operator fun ScientificValue<PhysicalQuantity.Length, Inch>.times(width: ScientificValue<PhysicalQuantity.Length, Inch>) = SquareInch.area(this, width)

@JvmName("footTimesFoot")
infix operator fun ScientificValue<PhysicalQuantity.Length, Foot>.times(width: ScientificValue<PhysicalQuantity.Length, Foot>) = SquareFoot.area(this, width)

@JvmName("yardTimesYard")
infix operator fun ScientificValue<PhysicalQuantity.Length, Yard>.times(width: ScientificValue<PhysicalQuantity.Length, Yard>) = SquareYard.area(this, width)

@JvmName("mileTimesMile")
infix operator fun ScientificValue<PhysicalQuantity.Length, Mile>.times(width: ScientificValue<PhysicalQuantity.Length, Mile>) = SquareMile.area(this, width)

@JvmName("imperialLengthTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength, WidthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    width: ScientificValue<PhysicalQuantity.Length, WidthUnit>,
) = SquareFoot.area(this, width)

@JvmName("lengthTimesLength")
infix operator fun <LengthUnit : Length, WidthUnit : Length> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    width: ScientificValue<PhysicalQuantity.Length, WidthUnit>,
) = SquareMeter.area(this, width)
