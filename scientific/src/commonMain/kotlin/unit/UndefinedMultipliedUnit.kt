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
import com.splendo.kaluga.scientific.UndefinedQuantityType

sealed class UndefinedMultipliedUnit<
    LeftQuantity : UndefinedQuantityType,
    LeftUnit : UndefinedScientificUnit<LeftQuantity>,
    RightQuantity : UndefinedQuantityType,
    RightUnit : UndefinedScientificUnit<RightQuantity>,
    > : AbstractUndefinedScientificUnit<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>>() {
    abstract val left: LeftUnit
    abstract val right: RightUnit

    override val quantityType by lazy { UndefinedQuantityType.Multiplying(left.quantityType, right.quantityType) }
    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        left.numeratorUnits + right.numeratorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        left.denominatorUnits + right.denominatorUnits
    }

    override fun fromSIUnit(value: Decimal): Decimal = left.deltaFromSIUnitDelta(right.deltaFromSIUnitDelta(value))
    override fun toSIUnit(value: Decimal): Decimal = left.deltaToSIUnitDelta(right.deltaToSIUnitDelta(value))

    data class MetricAndImperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInMetric,
          LeftUnit : MeasurementUsage.UsedInUKImperial,
          LeftUnit : MeasurementUsage.UsedInUSCustomary,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInMetric,
          RightUnit : MeasurementUsage.UsedInUKImperial,
          RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Metric(left, right) }
        override val imperial: Imperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Imperial(left, right) }
        override val ukImperial: UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { UKImperial(left, right) }
        override val usCustomary: USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { USCustomary(left, right) }
        override val metricAndUKImperial: MetricAndUKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { MetricAndUKImperial(left, right) }
        override val metricAndUSCustomary: MetricAndUSCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { MetricAndUSCustomary(left, right) }
    }

    data class Metric<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInMetric,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInUKImperial,
          LeftUnit : MeasurementUsage.UsedInUSCustomary,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInUKImperial,
          RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.Imperial

        override val ukImperial: UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { UKImperial(left, right) }
        override val usCustomary: USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { USCustomary(left, right) }
    }

    data class UKImperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInUKImperial,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInUSCustomary,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInMetric,
          LeftUnit : MeasurementUsage.UsedInUKImperial,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInMetric,
          RightUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
        override val metric: Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Metric(left, right) }
        override val ukImperial: UKImperial<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { UKImperial(left, right) }
    }

    data class MetricAndUSCustomary<
        LeftQuantity : UndefinedQuantityType,
        LeftUnit,
        RightQuantity : UndefinedQuantityType,
        RightUnit,
        > internal constructor(
        override val left: LeftUnit,
        override val right: RightUnit,
    ) : UndefinedMultipliedUnit<LeftQuantity, LeftUnit, RightQuantity, RightUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Multiplying<LeftQuantity, RightQuantity>> where
          LeftUnit : UndefinedScientificUnit<LeftQuantity>,
          LeftUnit : MeasurementUsage.UsedInMetric,
          LeftUnit : MeasurementUsage.UsedInUSCustomary,
          RightUnit : UndefinedScientificUnit<RightQuantity>,
          RightUnit : MeasurementUsage.UsedInMetric,
          RightUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
        override val metric: Metric<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { Metric(left, right) }
        override val usCustomary: USCustomary<LeftQuantity, LeftUnit, RightQuantity, RightUnit> by lazy { USCustomary(left, right) }
    }
}
