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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("specificEnergyFromKinematicViscosityAndTimeDefault")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    TimeUnit : Time,
    KinematicViscosityUnit : KinematicViscosity
    > SpecificEnergyUnit.specificEnergy(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, KinematicViscosityUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = specificEnergy(kinematicViscosity, time, ::DefaultScientificValue)

@JvmName("specificEnergyFromKinematicViscosityAndTime")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    TimeUnit : Time,
    KinematicViscosityUnit : KinematicViscosity,
    Value : ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>
    > SpecificEnergyUnit.specificEnergy(
    kinematicViscosity: ScientificValue<PhysicalQuantity.KinematicViscosity, KinematicViscosityUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, SpecificEnergyUnit) -> Value
) = byDividing(kinematicViscosity, time, factory)