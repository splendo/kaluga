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

sealed class DividedUndefinedScientificUnit<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    > : AbstractUndefinedScientificUnit<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>>() {
    abstract val numerator: NumeratorUnit
    abstract val denominator: DenominatorUnit
    override val quantityType by lazy {
        UndefinedQuantityType.Dividing(numerator.quantityType, denominator.quantityType)
    }

    override val numeratorUnits: List<ScientificUnit<*>> by lazy {
        numerator.numeratorUnits + denominator.denominatorUnits
    }

    override val denominatorUnits: List<ScientificUnit<*>> by lazy {
        denominator.numeratorUnits + numerator.denominatorUnits
    }

    override fun fromSIUnit(value: Decimal): Decimal = denominator.deltaToSIUnitDelta(numerator.deltaFromSIUnitDelta(value))
    override fun toSIUnit(value: Decimal): Decimal = numerator.deltaToSIUnitDelta(denominator.deltaFromSIUnitDelta(value))

    data class MetricAndImperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInMetric,
          NumeratorUnit : MeasurementUsage.UsedInUKImperial,
          NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInMetric,
          DenominatorUnit : MeasurementUsage.UsedInUKImperial,
          DenominatorUnit : MeasurementUsage.UsedInUSCustomary
    {
        override val system = MeasurementSystem.MetricAndImperial

        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> by lazy { Metric(numerator, denominator) }
        override val imperial: Imperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { Imperial(numerator, denominator) }
        override val ukImperial: UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { UKImperial(numerator, denominator) }
        override val usCustomary: USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { USCustomary(numerator, denominator) }
        override val metricAndUKImperial: MetricAndUKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { MetricAndUKImperial(numerator, denominator) }
        override val metricAndUSCustomary: MetricAndUSCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { MetricAndUSCustomary(numerator, denominator) }
    }

    data class Metric<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInMetric,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInMetric {
        override val system = MeasurementSystem.Metric
    }

    data class Imperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.Imperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInUKImperial,
          NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInUKImperial,
          DenominatorUnit : MeasurementUsage.UsedInUSCustomary
    {
        override val system = MeasurementSystem.Imperial
        override val ukImperial: UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { UKImperial(numerator, denominator) }
        override val usCustomary: USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { USCustomary(numerator, denominator) }
    }

    data class UKImperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.UKImperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInUKImperial,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInUKImperial {
        override val system = MeasurementSystem.UKImperial
    }

    data class USCustomary<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.USCustomary<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.USCustomary
    }

    data class MetricAndUKImperial<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInMetric,
          NumeratorUnit : MeasurementUsage.UsedInUKImperial,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInMetric,
          DenominatorUnit : MeasurementUsage.UsedInUKImperial
    {
        override val system = MeasurementSystem.MetricAndUKImperial
        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> by lazy { Metric(numerator, denominator) }
        override val ukImperial: UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { UKImperial(numerator, denominator) }
    }

    data class MetricAndUSCustomary<
        NumeratorQuantity : UndefinedQuantityType,
        NumeratorUnit,
        DenominatorQuantity : UndefinedQuantityType,
        DenominatorUnit,
        > internal constructor(
        override val numerator: NumeratorUnit,
        override val denominator: DenominatorUnit,
    ) : DividedUndefinedScientificUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> where
          NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
          NumeratorUnit : MeasurementUsage.UsedInMetric,
          NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
          DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
          DenominatorUnit : MeasurementUsage.UsedInMetric,
          DenominatorUnit : MeasurementUsage.UsedInUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
        override val metric: UndefinedScientificUnit.Metric<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>> by lazy { Metric(numerator, denominator) }
        override val usCustomary: USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit> by lazy { USCustomary(numerator, denominator) }
    }
}

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndImperial(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndImperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndImperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndImperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric = DividedUndefinedScientificUnit.Metric(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.Imperial(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.Imperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.Imperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.Imperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.UKImperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.USCustomary(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial = DividedUndefinedScientificUnit.MetricAndUKImperial(asUndefined(), denominator.asUndefined())

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(this, denominator)

infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(this, denominator.asUndefined())

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined(), denominator)

infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary = DividedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined(), denominator.asUndefined())
