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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricTorque
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.x

/**
 * Reinterprets this [Energy] value as a [PhysicalQuantity.Torque] of equal magnitude.
 * [Torque][PhysicalQuantity.Torque] shares its dimension (force times length) with [Energy][PhysicalQuantity.Energy]
 * but is a distinct quantity, so the two cannot be mixed in arithmetic; use this to bridge between them.
 * @return the [Torque][PhysicalQuantity.Torque] equivalent in `Newton x Meter`
 */
fun <EnergyUnit : Energy> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.asTorque(): ScientificValue<PhysicalQuantity.Torque, MetricTorque> =
    DefaultScientificValue((Newton x Meter).fromSIUnit(unit.toSIUnit(decimalValue)), Newton x Meter)
