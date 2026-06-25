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

import com.splendo.kaluga.scientific.converter.irradiance.div
import com.splendo.kaluga.scientific.converter.radiance.times
import com.splendo.kaluga.scientific.converter.radiantIntensity.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.Steradian
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class RadianceUnitTest {

    @Test
    fun radianceFromRadiantIntensityAndAreaTest() {
        assertEqualScientificValue(2((Horsepower per Steradian) per SquareFoot), 4(Horsepower per Steradian) / 2(SquareFoot), round = 28)
        assertEqualScientificValue(4(Horsepower per Steradian), 2((Horsepower per Steradian) per SquareFoot) * 2(SquareFoot), round = 28)
    }

    @Test
    fun radianceFromIrradianceAndSolidAngleTest() {
        assertEqualScientificValue(2((Horsepower per Steradian) per SquareFoot), 4(Horsepower per SquareFoot) / 2(Steradian), round = 28)
        assertEqualScientificValue(4(Horsepower per SquareFoot), 2((Horsepower per Steradian) per SquareFoot) * 2(Steradian), round = 28)
    }
}
