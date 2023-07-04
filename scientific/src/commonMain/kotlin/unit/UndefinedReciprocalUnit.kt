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
          InverseUnit : MeasurementUsage.UsedInMetricAndImperial {
        override val system = MeasurementSystem.MetricAndImperial
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
          InverseUnit : MeasurementUsage.UsedInImperial {
        override val system = MeasurementSystem.Imperial
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
          InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial {
        override val system = MeasurementSystem.MetricAndUKImperial
    }

    data class MetricAndUSCustomary<
        InverseQuantity : UndefinedQuantityType,
        InverseUnit,
        > internal constructor(override val inverse: InverseUnit) :
        UndefinedReciprocalUnit<InverseQuantity, InverseUnit>(),
        UndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Reciprocal<InverseQuantity>> where
          InverseUnit : UndefinedScientificUnit<InverseQuantity>,
          InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary {
        override val system = MeasurementSystem.MetricAndUSCustomary
    }
}

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial = UndefinedReciprocalUnit.MetricAndImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial = UndefinedReciprocalUnit.MetricAndImperial(asUndefined())

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
      InverseUnit : MeasurementUsage.UsedInImperial = UndefinedReciprocalUnit.Imperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInImperial = UndefinedReciprocalUnit.Imperial(asUndefined())

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
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial = UndefinedReciprocalUnit.MetricAndUKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial = UndefinedReciprocalUnit.MetricAndUKImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary = UndefinedReciprocalUnit.MetricAndUSCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary = UndefinedReciprocalUnit.MetricAndUSCustomary(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial =
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
      InverseUnit : MeasurementUsage.UsedInImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.Imperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInImperial =
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
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUKImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUSCustomary<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > UndefinedReciprocalUnit.MetricAndUSCustomary<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary =
    inverse.wrapped

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.MetricAndImperial<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetricAndImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetricAndImperial =
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
      NumeratorUnit : MeasurementUsage.UsedInImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInImperial =
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
      NumeratorUnit : MeasurementUsage.UsedInMetricAndUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetricAndUKImperial =
    denominator x numerator.reciprocal()

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > DividedUndefinedScientificUnit.MetricAndUSCustomary<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetricAndUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetricAndUSCustomary =
    denominator x numerator.reciprocal()
