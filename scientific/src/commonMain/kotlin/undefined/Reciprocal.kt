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

package com.splendo.kaluga.scientific.undefined

import UndefinedQuantityType
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.unit.DividedUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.InvertedUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedScientificUnit

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial = InvertedUndefinedScientificUnit.MetricAndImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial = InvertedUndefinedScientificUnit.MetricAndImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric = InvertedUndefinedScientificUnit.Metric(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric = InvertedUndefinedScientificUnit.Metric(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInImperial = InvertedUndefinedScientificUnit.Imperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInImperial = InvertedUndefinedScientificUnit.Imperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial = InvertedUndefinedScientificUnit.UKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial = InvertedUndefinedScientificUnit.UKImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary = InvertedUndefinedScientificUnit.USCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary = InvertedUndefinedScientificUnit.USCustomary(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial = InvertedUndefinedScientificUnit.MetricAndUKImperial(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial = InvertedUndefinedScientificUnit.MetricAndUKImperial(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary = InvertedUndefinedScientificUnit.MetricAndUSCustomary(this)

fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary = InvertedUndefinedScientificUnit.MetricAndUSCustomary(asUndefined())

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.MetricAndImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.MetricAndImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetricAndImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.Metric<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.Metric<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.Imperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.Imperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.UKImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUKImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.UKImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInUKImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.USCustomary<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.USCustomary<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInUSCustomary =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.MetricAndUKImperial<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.MetricAndUKImperial<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUKImperial =
    inverse.wrapped

fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InvertedUndefinedScientificUnit.MetricAndUSCustomary<InverseQuantity, InverseUnit>.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetricAndUSCustomary =
    inverse

fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit : ScientificUnit<WrappedQuantity>,
    InverseUnit,
    > InvertedUndefinedScientificUnit.MetricAndUSCustomary<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>.reciprocal() where
      InverseUnit : WrappedUndefinedScientificUnit<WrappedQuantity, WrappedUnit>,
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
