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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.dynamicViscosity.times
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MomentumUnitTest {

    @Test
    fun metricMomentumConversionTest() {
        assertScientificConversion(
            1.0,
            (Kilogram x (Meter per Second)),
            3600,
            Gram x (Kilometer per Hour)
        )
    }

    @Test
    fun momentumFromDynamicViscosityAndAreaTest() {
        assertEquals(4(Kilogram x (Meter per Second)), 2(Pascal x Second) * 2(SquareMeter))
        assertEquals(4(Kilogram x (Meter per Second)), 2(SquareMeter) * 2(Pascal x Second))
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)),
            2(PoundSquareFoot x Second) * 2(SquareFoot)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)),
            2(SquareFoot) * 2(PoundSquareFoot x Second)
        )
        assertEqualScientificValue(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)),
            2(PoundSquareFoot.ukImperial x Second) * 2(SquareFoot),
            8
        )
        assertEqualScientificValue(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)),
            2(SquareFoot) * 2(PoundSquareFoot.ukImperial x Second),
            8
        )
        assertEqualScientificValue(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)),
            2(PoundSquareFoot.usCustomary x Second) * 2(SquareFoot),
            8
        )
        assertEqualScientificValue(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)),
            2(SquareFoot) * 2(PoundSquareFoot.usCustomary x Second),
            8
        )
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(Pascal x Second).convert((PoundSquareFoot x Second) as DynamicViscosity) * 2(
                SquareMeter
            ),
            8
        )
        assertEqualScientificValue(
            4(Kilogram x (Meter per Second)),
            2(SquareMeter) * 2(Pascal x Second).convert((PoundSquareFoot x Second) as DynamicViscosity),
            8
        )
    }

    @Test
    fun momentumFromForceAndTimeTest() {
        assertEquals(4(Gram x (Centimeter per Second)), 2(Dyne) * 2(Second))
        assertEquals(4(Gram x (Centimeter per Second)), 2(Second) * 2(Dyne))
        assertEquals(4(Gram x (Centimeter per Second)), 20(Decidyne) * 2(Second))
        assertEquals(4(Gram x (Centimeter per Second)), 2(Second) * 20(Decidyne))
        assertEquals(4(Kilogram x (Meter per Second)), 2(Newton) * 2(Second))
        assertEquals(4(Kilogram x (Meter per Second)), 2(Second) * 2(Newton))
        assertEquals(
            (4 * MetricStandardGravityAcceleration.value)(Tonne x (Meter per Second)),
            2(TonneForce) * 2(Second)
        )
        assertEquals(
            (4 * MetricStandardGravityAcceleration.value)(Tonne x (Meter per Second)),
            2(Second) * 2(TonneForce)
        )
        assertEquals(
            (4 * MetricStandardGravityAcceleration.value)(Gram x (Meter per Second)),
            2(GramForce) * 2(Second)
        )
        assertEquals(
            (4 * MetricStandardGravityAcceleration.value)(Gram x (Meter per Second)),
            2(Second) * 2(GramForce)
        )
        assertEquals(
            (4 * MetricStandardGravityAcceleration.value)(Milligram x (Meter per Second)),
            2(MilligramForce) * 2(Second)
        )
        assertEquals(
            (4 * MetricStandardGravityAcceleration.value)(Milligram x (Meter per Second)),
            2(Second) * 2(MilligramForce)
        )

        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)),
            2(PoundForce) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)),
            2(Second) * 2(PoundForce)
        )
        assertEquals(4(Pound x (Foot per Second)), 2(Poundal) * 2(Second))
        assertEquals(4(Pound x (Foot per Second)), 2(Second) * 2(Poundal))
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Ounce x (Foot per Second)),
            2(OunceForce) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Ounce x (Foot per Second)),
            2(Second) * 2(OunceForce)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Grain x (Foot per Second)),
            2(GrainForce) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Grain x (Foot per Second)),
            2(Second) * 2(GrainForce)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)),
            2(PoundForce.ukImperial) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)),
            2(Second) * 2(PoundForce.ukImperial)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)),
            2(PoundForce.usCustomary) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)),
            2(Second) * 2(PoundForce.usCustomary)
        )
        assertEqualScientificValue(
            (4000 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)),
            2(Kip) * 2(Second),
            8
        )
        assertEqualScientificValue(
            (4000 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)),
            2(Second) * 2(Kip),
            8
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(UsTon x (Foot per Second)),
            2(UsTonForce) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(UsTon x (Foot per Second)),
            2(Second) * 2(UsTonForce)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(ImperialTon x (Foot per Second)),
            2(ImperialTonForce) * 2(Second)
        )
        assertEquals(
            (4 * ImperialStandardGravityAcceleration.value)(ImperialTon x (Foot per Second)),
            2(Second) * 2(ImperialTonForce)
        )
        assertEquals(
            4(Kilogram x (Meter per Second)),
            2(Newton).convert(PoundForce as Force) * 2(Second)
        )
        assertEquals(
            4(Kilogram x (Meter per Second)),
            2(Second) * 2(Newton).convert(PoundForce as Force)
        )
    }

    @Test
    fun momentumFromMassAndSpeedTest() {
        assertEquals(4(Kilogram x (Meter per Second)), 2(Kilogram) * 2(Meter per Second))
        assertEquals(4(Pound x (Foot per Second)), 2(Pound) * 2(Foot per Second))
        assertEquals(4(ImperialTon x (Foot per Second)), 2(ImperialTon) * 2(Foot per Second))
        assertEquals(4(UsTon x (Foot per Second)), 2(UsTon) * 2(Foot per Second))
        assertEquals(
            4(Kilogram x (Meter per Second)),
            2(Kilogram) * 2(Meter per Second).convert(Foot per Second)
        )
    }
}
