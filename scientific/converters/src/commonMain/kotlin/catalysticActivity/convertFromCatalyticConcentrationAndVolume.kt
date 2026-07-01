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

package com.splendo.kaluga.scientific.converter.catalysticActivity

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.CatalysticActivity
import com.splendo.kaluga.scientific.unit.CatalyticConcentration
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("catalysticActivityFromCatalyticConcentrationAndVolumeDefault")
fun <
    CatalysticActivityUnit : CatalysticActivity,
    VolumeUnit : Volume,
    CatalyticConcentrationUnit : CatalyticConcentration,
    > CatalysticActivityUnit.catalysticActivity(
    catalyticConcentration: ScientificValue<PhysicalQuantity.CatalyticConcentration, CatalyticConcentrationUnit>,
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = catalysticActivity(catalyticConcentration, volume, ::DefaultScientificValue)

@JvmName("catalysticActivityFromCatalyticConcentrationAndVolume")
fun <
    CatalysticActivityUnit : CatalysticActivity,
    VolumeUnit : Volume,
    CatalyticConcentrationUnit : CatalyticConcentration,
    Value : ScientificValue<PhysicalQuantity.CatalysticActivity, CatalysticActivityUnit>,
    > CatalysticActivityUnit.catalysticActivity(
    catalyticConcentration: ScientificValue<PhysicalQuantity.CatalyticConcentration, CatalyticConcentrationUnit>,
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
    factory: (Decimal, CatalysticActivityUnit) -> Value,
) = byMultiplying(catalyticConcentration, volume, factory)
