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
    > : UndefinedScientificUnit<UndefinedQuantityType.Extended<ExtendedQuantity>>() {
    abstract val extendedQuantity: ExtendedQuantity
    override val quantityType by lazy { UndefinedQuantityType.Extended(extendedQuantity) }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy { listOf(this) }
    override val denominatorUnits: List<ScientificUnit<*>> = emptyList()

    abstract class MetricAndImperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.MetricAndImperial
    }

    abstract class Metric<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        MetricScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.Metric
    }

    abstract class Imperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        ImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.Imperial
    }

    abstract class UKImperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        UKImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.UKImperial
    }

    abstract class USCustomary<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        USCustomaryScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.USCustomary
    }

    abstract class MetricAndUKImperial<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    abstract class MetricAndUSCustomary<ExtendedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension> :
        UndefinedExtendedUnit<ExtendedQuantity>(),
        MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<ExtendedQuantity>>> {
        override val system = MeasurementSystem.MetricAndUSCustomary
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

    class MetricAndImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        MetricAndImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetricAndImperial {
        override val system = MeasurementSystem.MetricAndImperial
    }

    class Metric<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        MetricScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    class Imperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        ImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInImperial {
        override val system = MeasurementSystem.Imperial
    }

    class UKImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        UKImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    class USCustomary<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        USCustomaryScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    class MetricAndUKImperial<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        MetricAndUKImperialScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetricAndUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    class MetricAndUSCustomary<
        WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
        WrappedUnit,
        > internal constructor(override val wrapped: WrappedUnit) :
        WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>(),
        MetricAndUSCustomaryScientificUnit<PhysicalQuantity.Undefined<UndefinedQuantityType.Extended<WrappedQuantity>>> where
          WrappedUnit : ScientificUnit<WrappedQuantity>,
          WrappedUnit : MeasurementUsage.UsedInMetricAndUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetricAndImperial = WrappedUndefinedExtendedUnit.MetricAndImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetric = WrappedUndefinedExtendedUnit.Metric(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInImperial = WrappedUndefinedExtendedUnit.Imperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInUKImperial = WrappedUndefinedExtendedUnit.UKImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInUSCustomary = WrappedUndefinedExtendedUnit.USCustomary(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetricAndUKImperial = WrappedUndefinedExtendedUnit.MetricAndUKImperial(this)
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    > Unit.asUndefined() where Unit : ScientificUnit<Quantity>, Unit : MeasurementUsage.UsedInMetricAndUSCustomary = WrappedUndefinedExtendedUnit.MetricAndUSCustomary(this)
