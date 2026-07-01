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

package com.splendo.kaluga.scientific.converter.magneticDipoleMoment

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.MagneticDipoleMoment
import kotlin.jvm.JvmName

@JvmName("magneticDipoleMomentFromElectricCurrentAndAreaDefault")
fun <
    CurrentUnit : ElectricCurrent,
    AreaUnit : Area,
    MagneticDipoleMomentUnit : MagneticDipoleMoment,
    > MagneticDipoleMomentUnit.magneticDipoleMoment(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = magneticDipoleMoment(current, area, ::DefaultScientificValue)

@JvmName("magneticDipoleMomentFromElectricCurrentAndArea")
fun <
    CurrentUnit : ElectricCurrent,
    AreaUnit : Area,
    MagneticDipoleMomentUnit : MagneticDipoleMoment,
    Value : ScientificValue<PhysicalQuantity.MagneticDipoleMoment, MagneticDipoleMomentUnit>,
    > MagneticDipoleMomentUnit.magneticDipoleMoment(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
    factory: (Decimal, MagneticDipoleMomentUnit) -> Value,
) = byMultiplying(current, area, factory)
