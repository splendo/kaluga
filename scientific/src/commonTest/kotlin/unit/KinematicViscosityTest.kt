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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test

class KinematicViscosityTest {

    @Test
    fun metricKinematicViscosityConversionTest() {
        assertScientificConversion(1, (SquareMeter per Second), 1e+18, (SquareNanometer per Second))
        assertScientificConversion(1, (SquareMeter per Second), 1e+12, (SquareMicrometer per Second))
        assertScientificConversion(1, (SquareMeter per Second), 1000000.0, (SquareMillimeter per Second))
        assertScientificConversion(1, (SquareMeter per Second), 10000.0, (SquareCentimeter per Second))
        assertScientificConversion(1, (SquareMeter per Second), 100.0, (SquareDecimeter per Second))
        assertScientificConversion(1, (SquareMeter per Second), 0.01, (SquareDecameter per Second))
        assertScientificConversion(1, (SquareMeter per Second), 0.0001, (SquareHectometer per Second))
        assertScientificConversion(1, (SquareMeter per Second), 1e-6, (SquareKilometer per Second))
        assertScientificConversion(1, (SquareMeter per Second), 0.0001, (Hectare per Second))

        assertScientificConversion(1, (SquareMeter per Second), 1550.0, (SquareInch per Second), 0)
        assertScientificConversion(1, (SquareMeter per Second), 10.7639, (SquareFoot per Second), 4)
        assertScientificConversion(1, (SquareMeter per Second), 1.19599, (SquareYard per Second), 5)
        assertScientificConversion(1, (SquareMeter per Second), 3.86102e-7, (SquareMile per Second), 12)
        assertScientificConversion(1, (SquareMeter per Second), 0.000247105, (Acre per Second), 9)

        assertScientificConversion(1, (SquareMeter per Second), 1e-9, (SquareMeter per Nanosecond))
        assertScientificConversion(1, (SquareMeter per Second), 1e-6, (SquareMeter per Microsecond))
        assertScientificConversion(1, (SquareMeter per Second), 0.001, (SquareMeter per Millisecond))
        assertScientificConversion(1, (SquareMeter per Second), 0.01, (SquareMeter per Centisecond))
        assertScientificConversion(1, (SquareMeter per Second), 0.1, (SquareMeter per Decisecond))
        assertScientificConversion(1, (SquareMeter per Second), 60, (SquareMeter per Minute), 3)
        assertScientificConversion(1, (SquareMeter per Second), 3600.0, (SquareMeter per Hour), 5)
    }

    @Test
    fun kinematicViscosityFromAreaByTimeTest() {
        assertEqualScientificValue(1(SquareMeter per Second), (2(SquareMeter)) / 2(Second))
        assertEqualScientificValue(1(SquareMeter per Second), (2(Meter) * 1(Meter)) / 2(Second))

        assertEqualScientificValue(1(SquareFoot per Second), (2(SquareFoot)) / 2(Second))
        assertEqualScientificValue(1(SquareFoot per Second), (2(Foot) * 1(Foot)) / 2(Second))

        assertEqualScientificValue(1(SquareMeter per Second), (2(SquareMeter).convert(SquareFoot)) / 2(Second))
        assertEqualScientificValue(1(SquareMeter per Second), (2(Meter) * 1(Meter).convert(Foot)) / 2(Second))
    }

    @Test
    fun kinematicViscosityFromDynamicViscosityAndDensity() {
        assertEqualScientificValue(1(SquareMeter per Second), (2(Pascal x Second)) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot x Second)) / 2(Pound per CubicFoot))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot x Second)) / 2(Pound.ukImperial per CubicFoot))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot x Second)) / 2(Pound.usCustomary per CubicFoot))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot.ukImperial x Second)) / 2(Pound per CubicFoot))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot.ukImperial x Second)) / 2(Pound.ukImperial per CubicFoot))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot.usCustomary x Second)) / 2(Pound per CubicFoot))
        assertEqualScientificValue(ImperialStandardGravityAcceleration.value(SquareFoot per Second), (2(PoundSquareFoot.usCustomary x Second)) / 2(Pound.usCustomary per CubicFoot))
        assertEqualScientificValue(1(SquareMeter per Second), (2(Pascal x Second)) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot))
    }

    @Test
    fun kinematicViscosityFromSpecificEnergyAndTime() {
        assertEqualScientificValue(4(SquareMeter per Second), (2(Joule per Kilogram)) * 2(Second))
        assertEqualScientificValue(4(SquareMeter per Second), (2(Second) * 2(Joule per Kilogram)))
        assertEqualScientificValue((4 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second), (2(FootPoundForce per Pound)) * 2(Second))
        assertEqualScientificValue((4 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second), (2(Second) * 2(FootPoundForce per Pound)))
        assertEqualScientificValue((4 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second), (2(FootPoundForce per Pound.ukImperial)) * 2(Second))
        assertEqualScientificValue((4 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second), (2(Second) * 2(FootPoundForce per Pound.ukImperial)))
        assertEqualScientificValue((4 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second), (2(FootPoundForce per Pound.usCustomary)) * 2(Second))
        assertEqualScientificValue((4 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second), (2(Second) * 2(FootPoundForce per Pound.usCustomary)))
        assertEqualScientificValue(4(SquareMeter per Second), (2(Joule per Kilogram)).convert(FootPoundForce per Pound) * 2(Second))
        assertEqualScientificValue(4(SquareMeter per Second), (2(Second) * 2(Joule per Kilogram).convert(FootPoundForce per Pound)))
    }
}
