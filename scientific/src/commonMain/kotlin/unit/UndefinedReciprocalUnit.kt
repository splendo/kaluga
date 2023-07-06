/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.UndefinedQuantityType

sealed class UndefinedReciprocalUnit<
    InverseQuantity : UndefinedQuantityType,
    InverseUnit : UndefinedScientificUnit<InverseQuantity>,
    > : AbstractUndefinedScientificUnit<UndefinedQuantityType.Reciprocal<InverseQuantity>>() {
    abstract val inverse: InverseUnit

    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        inverse.denominatorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        inverse.numeratorUnits
    }

    override val quantityType: UndefinedQuantityType.Reciprocal<InverseQuantity> by lazy {
        UndefinedQuantityType.Reciprocal(inverse.quantityType)
    }

    override fun fromSIUnit(value: Decimal): Decimal = inverse.deltaFromSIUnitDelta(1.0.toDecimal() / value)
    override fun toSIUnit(value: Decimal): Decimal = inverse.deltaToSIUnitDelta(1.0.toDecimal() / value)

    data class MetricAndImperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInMetric,
          InverseUnit : MeasurementUsage.UsedInUKImperial,
          InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: Metric<InverseQuantity, InverseUnit> by lazy { Metric(inverse) }
        override val imperial: Imperial<InverseQuantity, InverseUnit> by lazy { Imperial(inverse) }
        override val ukImperial: UKImperial<InverseQuantity, InverseUnit> by lazy { UKImperial(inverse) }
        override val usCustomary: USCustomary<InverseQuantity, InverseUnit> by lazy { USCustomary(inverse) }
        override val metricAndUKImperial: MetricAndUKImperial<InverseQuantity, InverseUnit> by lazy { MetricAndUKImperial(inverse) }
        override val metricAndUSCustomary: MetricAndUSCustomary<InverseQuantity, InverseUnit> by lazy { MetricAndUSCustomary(inverse) }
    }

    data class Metric<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInUKImperial,
          InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.Imperial

        override val ukImperial: UKImperial<InverseQuantity, InverseUnit> by lazy { UKImperial(inverse) }
        override val usCustomary: USCustomary<InverseQuantity, InverseUnit> by lazy { USCustomary(inverse) }
    }

    data class UKImperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInMetric,
          InverseUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial

        override val metric: Metric<InverseQuantity, InverseUnit> by lazy { Metric(inverse) }
        override val ukImperial: UKImperial<InverseQuantity, InverseUnit> by lazy { UKImperial(inverse) }
    }

    data class MetricAndUSCustomary<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInMetric,
          InverseUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary

        override val metric: Metric<InverseQuantity, InverseUnit> by lazy { Metric(inverse) }
        override val usCustomary: USCustomary<InverseQuantity, InverseUnit> by lazy { USCustomary(inverse) }
    }
}
