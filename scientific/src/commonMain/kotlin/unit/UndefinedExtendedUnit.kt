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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.UndefinedQuantityType

sealed class UndefinedExtendedUnit<
    ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    > : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<ExtendedQuantity>>() {
    abstract val extendedQuantity: ExtendedQuantity
    override val quantityType by lazy { UndefinedQuantityType.Extended(extendedQuantity) }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy { listOf(this) }
    override val denominatorUnits: List<ScientificUnit<*>> = emptyList()

    abstract class MetricAndImperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: Metric<ExtendedQuantity> by lazy {
            object : Metric<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val imperial: Imperial<ExtendedQuantity> by lazy {
            object : Imperial<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val ukImperial: UKImperial<ExtendedQuantity> by lazy {
            object : UKImperial<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val usCustomary: USCustomary<ExtendedQuantity> by lazy {
            object : USCustomary<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val metricAndUKImperial: MetricAndUKImperial<ExtendedQuantity> by lazy {
            object : MetricAndUKImperial<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
        override val metricAndUSCustomary: MetricAndUSCustomary<ExtendedQuantity> by lazy {
            object : MetricAndUSCustomary<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndImperial.fromSIUnit(value)
            }
        }
    }

    abstract class Metric<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.Metric
    }

    abstract class Imperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.Imperial

        override val ukImperial: UKImperial<ExtendedQuantity> by lazy {
            object : UKImperial<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@Imperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@Imperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@Imperial.fromSIUnit(value)
            }
        }
        override val usCustomary: USCustomary<ExtendedQuantity> by lazy {
            object : USCustomary<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@Imperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@Imperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@Imperial.fromSIUnit(value)
            }
        }
    }

    abstract class UKImperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.UKImperial
    }

    abstract class USCustomary<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.USCustomary
    }

    abstract class MetricAndUKImperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.MetricAndUKImperial

        override val metric: Metric<ExtendedQuantity> by lazy {
            object : Metric<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndUKImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.fromSIUnit(value)
            }
        }
        override val ukImperial: UKImperial<ExtendedQuantity> by lazy {
            object : UKImperial<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndUKImperial.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUKImperial.fromSIUnit(value)
            }
        }
    }

    abstract class MetricAndUSCustomary<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Extended<ExtendedQuantity>> {
        override val system = MeasurementSystem.MetricAndUSCustomary

        override val metric: Metric<ExtendedQuantity> by lazy {
            object : Metric<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndUSCustomary.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.fromSIUnit(value)
            }
        }
        override val usCustomary: USCustomary<ExtendedQuantity> by lazy {
            object : USCustomary<ExtendedQuantity>() {
                override val extendedQuantity: ExtendedQuantity = this@MetricAndUSCustomary.extendedQuantity
                override fun toSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.toSIUnit(value)
                override fun fromSIUnit(value: Decimal): Decimal = this@MetricAndUSCustomary.fromSIUnit(value)
            }
        }
    }
}

sealed class WrappedUndefinedExtendedUnit<
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    > : UndefinedExtendedUnit<WrappedQuantity>() {
    abstract val wrapped: WrappedUnit
    override val extendedQuantity: WrappedQuantity by lazy { wrapped.quantity }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy { listOf(wrapped) }

    override fun fromSIUnit(value: Decimal): Decimal = wrapped.toSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = wrapped.fromSIUnit(value)
    override fun deltaFromSIUnitDelta(delta: Decimal): Decimal = wrapped.deltaFromSIUnitDelta(delta)
    override fun deltaToSIUnitDelta(delta: Decimal): Decimal = wrapped.deltaToSIUnitDelta(delta)

    data class MetricAndImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetric,
          WrappedUnit : MeasurementUsage.UsedInUKImperial,
          WrappedUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndImperial
        override val metric: Metric<WrappedQuantity, WrappedUnit> by lazy { Metric(wrapped) }
        override val imperial: Imperial<WrappedQuantity, WrappedUnit> by lazy { Imperial(wrapped) }
        override val ukImperial: UKImperial<WrappedQuantity, WrappedUnit> by lazy { UKImperial(wrapped) }
        override val usCustomary: USCustomary<WrappedQuantity, WrappedUnit> by lazy { USCustomary(wrapped) }
        override val metricAndUKImperial: MetricAndUKImperial<WrappedQuantity, WrappedUnit> by lazy { MetricAndUKImperial(wrapped) }
        override val metricAndUSCustomary: MetricAndUSCustomary<WrappedQuantity, WrappedUnit> by lazy { MetricAndUSCustomary(wrapped) }
    }

    data class Metric<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInUKImperial,
          WrappedUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.Imperial
        override val ukImperial: UKImperial<WrappedQuantity, WrappedUnit> by lazy { UKImperial(wrapped) }
        override val usCustomary: USCustomary<WrappedQuantity, WrappedUnit> by lazy { USCustomary(wrapped) }
    }

    data class UKImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetric,
          WrappedUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
        override val metric: Metric<WrappedQuantity, WrappedUnit> by lazy { Metric(wrapped) }
        override val ukImperial: UKImperial<WrappedQuantity, WrappedUnit> by lazy { UKImperial(wrapped) }
    }

    data class MetricAndUSCustomary<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Extended<WrappedQuantity>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetric,
          WrappedUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
        override val metric: Metric<WrappedQuantity, WrappedUnit> by lazy { Metric(wrapped) }
        override val usCustomary: USCustomary<WrappedQuantity, WrappedUnit> by lazy { USCustomary(wrapped) }
    }
}

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary = WrappedUndefinedExtendedUnit.MetricAndImperial(
    this,
)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric = WrappedUndefinedExtendedUnit.Metric(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary = WrappedUndefinedExtendedUnit.Imperial(
    this,
)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial = WrappedUndefinedExtendedUnit.UKImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUSCustomary = WrappedUndefinedExtendedUnit.USCustomary(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial = WrappedUndefinedExtendedUnit.MetricAndUKImperial(
    this,
)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary = WrappedUndefinedExtendedUnit.MetricAndUSCustomary(
    this,
)
