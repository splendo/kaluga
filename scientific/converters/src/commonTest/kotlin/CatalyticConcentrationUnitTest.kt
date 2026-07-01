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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.catalysticActivity.div
import com.splendo.kaluga.scientific.converter.catalyticConcentration.times
import com.splendo.kaluga.scientific.converter.volume.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Katal
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class CatalyticConcentrationUnitTest {

    @Test
    fun catalyticConcentrationFromCatalysticActivityAndVolumeTest() {
        assertEqualScientificValue(1(Katal per CubicMeter), 2(Katal) / 2(CubicMeter))
        assertEqualScientificValue(1(Katal per CubicFoot), 2(Katal) / 2(CubicFoot), round = 27)
        assertEqualScientificValue(1(Katal per CubicFoot.ukImperial), 2(Katal) / 2(CubicFoot.ukImperial), round = 27)
        assertEqualScientificValue(1(Katal per CubicFoot.usCustomary), 2(Katal) / 2(CubicFoot.usCustomary), round = 27)
        assertEqualScientificValue(1(Katal per CubicMeter), 2(Katal) / 2(CubicMeter).convert(CubicFoot as Volume), round = 27)
    }

    @Test
    fun catalysticActivityFromCatalyticConcentrationAndVolumeTest() {
        assertEqualScientificValue(4(Katal), 2(Katal per CubicMeter) * 2(CubicMeter))
        assertEqualScientificValue(4(Katal), 2(CubicMeter) * 2(Katal per CubicMeter))
        assertEqualScientificValue(4(Katal), 2(Katal per CubicFoot) * 2(CubicFoot), round = 27)
        assertEqualScientificValue(4(Katal), 2(CubicFoot) * 2(Katal per CubicFoot), round = 27)
        assertEqualScientificValue(4(Katal), 2(Katal per CubicFoot.ukImperial) * 2(CubicFoot.ukImperial), round = 27)
        assertEqualScientificValue(4(Katal), 2(CubicFoot.ukImperial) * 2(Katal per CubicFoot.ukImperial), round = 27)
        assertEqualScientificValue(4(Katal), 2(Katal per CubicFoot.usCustomary) * 2(CubicFoot.usCustomary), round = 27)
        assertEqualScientificValue(4(Katal), 2(CubicFoot.usCustomary) * 2(Katal per CubicFoot.usCustomary), round = 27)
    }
}
