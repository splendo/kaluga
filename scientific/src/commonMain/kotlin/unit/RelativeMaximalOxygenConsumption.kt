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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity

sealed class RelativeMaximalOxygenConsumption : AbstractScientificUnit<PhysicalQuantity.AerobicCapacity>() {
    abstract val flow: VolumetricFlow
    abstract val per: Weight
    override val quantity = PhysicalQuantity.AerobicCapacity
    override val symbol: String by lazy { "${flow.symbol} / ${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(flow.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = flow.toSIUnit(per.fromSIUnit(value))
}

class MetricRelativeMaximalOxygenConsumption(
    override val flow: MetricVolumetricFlow,
    override val per: MetricWeight
) : RelativeMaximalOxygenConsumption(), MetricScientificUnit<PhysicalQuantity.AerobicCapacity> {
    override val system = MeasurementSystem.Metric
}

class ImperialRelativeMaximalOxygenConsumption(
    override val flow: ImperialVolumetricFlow,
    override val per: ImperialWeight
) : RelativeMaximalOxygenConsumption(), ImperialScientificUnit<PhysicalQuantity.AerobicCapacity> {
    override val system = MeasurementSystem.Imperial
}

class UKImperialRelativeMaximalOxygenConsumption(
    override val flow: UKImperialVolumetricFlow,
    override val per: UKImperialWeight
) : RelativeMaximalOxygenConsumption(), UKImperialScientificUnit<PhysicalQuantity.AerobicCapacity> {
    override val system = MeasurementSystem.UKImperial
}

class USCustomaryRelativeMaximalOxygenConsumption(
    override val flow: USCustomaryVolumetricFlow,
    override val per: USCustomaryWeight
) : RelativeMaximalOxygenConsumption(), USCustomaryScientificUnit<PhysicalQuantity.AerobicCapacity> {
    override val system = MeasurementSystem.USCustomary
}

infix fun MetricVolumetricFlow.per(per:  MetricWeight) = MetricRelativeMaximalOxygenConsumption(flow = this, per = per)
infix fun ImperialVolumetricFlow.per(per:  ImperialWeight) = ImperialRelativeMaximalOxygenConsumption(flow = this, per = per)
infix fun UKImperialVolumetricFlow.per(per:  UKImperialWeight) = UKImperialRelativeMaximalOxygenConsumption(flow = this, per = per)
infix fun USCustomaryVolumetricFlow.per(per:  USCustomaryWeight) = USCustomaryRelativeMaximalOxygenConsumption(flow = this, per = per)
