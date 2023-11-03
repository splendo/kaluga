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
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftUnit],
 *   [RightUnit]
 *  )
 */
@JvmName("metricAndImperialUndefinedXMetricAndImperialUndefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = UndefinedMultipliedUnit.MetricAndImperial(this, right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ([RightUnit])
 *  )
 */
@JvmName("metricAndImperialUndefinedXMetricAndImperialDefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ([LeftUnit]),
 *   [RightUnit]
 *  )
 */
@JvmName("metricAndImperialDefinedXMetricAndImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().x(right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ([LeftUnit]),
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ([RightUnit])
 *  )
 */
@JvmName("metricAndImperialDefinedXMetricAndImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [RightUnit],
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("metricAndImperialReciprocalXMetricAndImperialUndefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right per inverse

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ( [RightUnit] ),
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("metricAndImperialReciprocalXMetricAndImperialDefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [LeftUnit],
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("metricAndImperialUndefinedXMetricAndImperialReciprocal")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ( [LeftUnit] ),
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("metricAndImperialDefinedXMetricAndImperialReciprocal")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedReciprocalUnit.MetricAndImperial] (
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftReciprocalUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("metricAndImperialReciprocalXMetricAndImperialReciprocal")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (inverse x right.inverse).reciprocal()

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [RightNumeratorUnit]
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftReciprocalUnit],
 *   [RightDenominatorUnit]
 *   )
 *  )
 */
@JvmName("metricAndImperialReciprocalXMetricAndImperialDivided")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right.numerator per (inverse x right.denominator)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [LeftNumeratorUnit]
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftDenominatorUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("metricAndImperialDividedXMetricAndImperialReciprocal")
infix fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit,
    LeftUnit,
    RightReciprocalQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
      LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = numerator per (denominator x right.inverse)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftNumeratorUnit],
 *   [RightUnit]
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("metricAndImperialDividedXMetricAndImperialUndefined")
infix fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
      LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right) per denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftNumeratorUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ( [RightUnit] )
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("metricAndImperialDividedXMetricAndImperialDefined")
infix fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
      LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("metricAndImperialUndefinedXMetricAndImperialDivided")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (this x right.numerator) per right.denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndImperial] ( [LeftUnit] ),
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("metricAndImperialDefinedXMetricAndImperialDivided")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined() x right

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndImperial] (
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *   [LeftNumeratorUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.MetricAndImperial] (
 *  *   [LeftDenominatorUnit],
 *  *   [RightDenominatorUnit]
 *  *   )
 *  )
 */
@JvmName("metricAndImperialDividedXMetricAndImperialDivided")
infix fun <
    LeftNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit,
    LeftDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorUnit,
    LeftUnit,
    RightNumeratorQuantity : UndefinedQuantityType,
    RightNumeratorUnit,
    RightDenominatorQuantity : UndefinedQuantityType,
    RightDenominatorUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorQuantity>,
      LeftNumeratorUnit : MeasurementUsage.UsedInMetric,
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right.numerator) per (denominator x right.denominator)
