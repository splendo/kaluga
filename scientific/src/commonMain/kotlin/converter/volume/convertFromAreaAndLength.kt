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

package com.splendo.kaluga.scientific.converter.volume

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("volumeFromAreaAndHeightDefault")
fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume
> VolumeUnit.volume(
    area: ScientificValue<MeasurementType.Area, AreaUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = volume(area, height, ::DefaultScientificValue)

@JvmName("volumeFromAreaAndHeight")
fun <
    HeightUnit : Length,
    AreaUnit : Area,
    VolumeUnit : Volume,
    Value : ScientificValue<MeasurementType.Volume, VolumeUnit>
> VolumeUnit.volume(
    area: ScientificValue<MeasurementType.Area, AreaUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>,
    factory: (Decimal, VolumeUnit) -> Value
) = byMultiplying(area, height, factory)

@JvmName("volumeFromLengthWidthAndHeightDefault")
fun <
    LengthUnit : Length,
    WidthUnit : Length,
    HeightUnit : Length,
    VolumeUnit : Volume
    > VolumeUnit.volume(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = volume(length, width, height, ::DefaultScientificValue)

@JvmName("volumeFromLengthWidthAndHeight")
fun <
    LengthUnit : Length,
    WidthUnit : Length,
    HeightUnit : Length,
    VolumeUnit : Volume,
    Value : ScientificValue<MeasurementType.Volume, VolumeUnit>
    > VolumeUnit.volume(
    length: ScientificValue<MeasurementType.Length, LengthUnit>,
    width: ScientificValue<MeasurementType.Length, WidthUnit>,
    height: ScientificValue<MeasurementType.Length, HeightUnit>,
    factory: (Decimal, VolumeUnit) -> Value
) = volume(SquareMeter.area(length, width), height, factory)
