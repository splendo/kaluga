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
import com.splendo.kaluga.scientific.PhysicalQuantity
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
          InverseUnit : MeasurementUsage.UsedInUSCustomary   {
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

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.MetricAndImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.MetricAndImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric = UndefinedReciprocalUnit.Metric(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric = UndefinedReciprocalUnit.Metric(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.Imperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.Imperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial = UndefinedReciprocalUnit.UKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial = UndefinedReciprocalUnit.UKImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.USCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.USCustomary(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial = UndefinedReciprocalUnit.MetricAndUKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial = UndefinedReciprocalUnit.MetricAndUKImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.MetricAndUSCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.MetricAndUSCustomary(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.Metric<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.Metric<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.Imperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.Imperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.UKImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.UKImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInUKImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.USCustomary<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.USCustomary<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUKImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUKImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUKImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUSCustomary<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUSCustomary<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric,
InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse.wrapped

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.MetricAndImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.Metric<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.Imperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.UKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.USCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.MetricAndUKImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUKImperial =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.MetricAndUSCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
DenominatorUnit : MeasurementUsage.UsedInUSCustomary =
    denominator x numerator.reciprocal()
