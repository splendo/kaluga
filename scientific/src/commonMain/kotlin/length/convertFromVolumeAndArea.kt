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

package com.splendo.kaluga.scientific.length

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.area.area
import com.splendo.kaluga.scientific.byDividing
import kotlin.jvm.JvmName

@JvmName("heightFromVolumeAndArea")
fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume
    > HeightUnit.height(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    area: ScientificValue<MeasurementType.Area, AreaUnit>
) : ScientificValue<MeasurementType.Length, HeightUnit> = byDividing(volume, area)

@JvmName("heightFromVolumeLengthAndWidth")
fun <
    HeightUnit : Length,
    LengthUnit : Length,
    WidthUnit : Length,
    VolumeUnit : Volume
    > HeightUnit.height(
    volume: ScientificValue<MeasurementType.Volume, VolumeUnit>,
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>,
) = height(volume, SquareMeter.area(length, width))
