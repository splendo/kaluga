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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.UndefinedQuantityType
import kotlin.jvm.JvmName

/**
 * [InverseUnit] -> [UndefinedReciprocalUnit.MetricAndImperial] ([InverseUnit])
 */
@JvmName("metricAndImperialUndefinedReciprocal")
fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
      InverseUnit : MeasurementUsage.UsedInUSCustomary = UndefinedReciprocalUnit.MetricAndImperial(this)

/**
 * [InverseUnit] -> [UndefinedReciprocalUnit.MetricAndImperial] ([WrappedUndefinedExtendedUnit.MetricAndImperial] ([InverseUnit]))
 */
@JvmName("metricAndImperialDefinedReciprocal")
fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
      InverseUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().reciprocal()

/**
 * [ReciprocalUnit] -> [InverseUnit]
 */
@JvmName("metricAndImperialReciprocalUndefined")
fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    ReciprocalUnit,
    > ReciprocalUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
      InverseUnit : MeasurementUsage.UsedInUSCustomary,
      ReciprocalUnit : UndefinedReciprocalUnit<InverseQuantity, InverseUnit>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary =
    inverse

/**
 * [ReciprocalUnit] -> [WrappedUnit]
 */
@JvmName("metricAndImperialReciprocalDefined")
fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit,
    InverseUnit,
    ReciprocalUnit,
    > ReciprocalUnit.reciprocal() where
      WrappedUnit : ScientificUnit<WrappedQuantity>,
      WrappedUnit : MeasurementUsage.UsedInMetric,
      WrappedUnit : MeasurementUsage.UsedInUKImperial,
      WrappedUnit : MeasurementUsage.UsedInUSCustomary,
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
      InverseUnit : MeasurementUsage.UsedInUSCustomary,
      ReciprocalUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary =
    inverse.wrapped

/**
 * [UndefinedDividedUnit] ([NumeratorUnit], [DenominatorUnit]) -> [UndefinedDividedUnit.MetricAndImperial] ([DenominatorUnit], [NumeratorUnit])
 */
@JvmName("metricAndImperialDividedUnitReciprocal")
fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    DividerUnit,
    > DividerUnit.reciprocal() where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInMetric,
      DividerUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : MeasurementUsage.UsedInUSCustomary = denominator per numerator
