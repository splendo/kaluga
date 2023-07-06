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
 * [InverseUnit] -> [UndefinedReciprocalUnit.MetricAndUKImperial] ([InverseUnit])
 */
@JvmName("metricAndUKImperialUndefinedReciprocal")
fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial = UndefinedReciprocalUnit.MetricAndUKImperial(this)

/**
 * [InverseUnit] -> [UndefinedReciprocalUnit.MetricAndUKImperial] ([WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([InverseUnit]))
 */
@JvmName("metricAndUKImperialDefinedReciprocal")
fun <
    InverseQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    InverseUnit,
    > InverseUnit.reciprocal() where
      InverseUnit : ScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial = asUndefined().reciprocal()

/**
 * [ReciprocalUnit] -> [InverseUnit]
 */
@JvmName("metricAndUKImperialReciprocalUndefined")
fun <
    InverseQuantity : UndefinedQuantityType,
    InverseUnit,
    ReciprocalUnit,
    > ReciprocalUnit.reciprocal() where
      InverseUnit : UndefinedScientificUnit<InverseQuantity>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedReciprocalUnit<InverseQuantity, InverseUnit>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial =
    inverse

/**
 * [ReciprocalUnit] -> [WrappedUnit]
 */
@JvmName("metricAndUKImperialReciprocalDefined")
fun <
    WrappedQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedUnit,
    InverseUnit,
    ReciprocalUnit,
    > ReciprocalUnit.reciprocal() where
      WrappedUnit : ScientificUnit<WrappedQuantity>,
      WrappedUnit : MeasurementUsage.UsedInMetric,
      WrappedUnit : MeasurementUsage.UsedInUKImperial,
      InverseUnit : WrappedUndefinedExtendedUnit<WrappedQuantity, WrappedUnit>,
      InverseUnit : MeasurementUsage.UsedInMetric,
      InverseUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedReciprocalUnit<UndefinedQuantityType.Extended<WrappedQuantity>, InverseUnit>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial =
    inverse.wrapped

/**
 * [UndefinedDividedUnit] ([NumeratorUnit], [DenominatorUnit]) -> [UndefinedDividedUnit.MetricAndUKImperial] ([DenominatorUnit], [NumeratorUnit])
 */
@JvmName("metricAndUKImperialDividedUnitReciprocal")
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
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>,
      DividerUnit : MeasurementUsage.UsedInMetric,
      DividerUnit : MeasurementUsage.UsedInUKImperial = denominator per numerator
