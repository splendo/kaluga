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
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftUnit],
 *   [RightUnit]
 *  )
 */
@JvmName("metricAndUKImperialUndefinedXMetricAndUKImperialUndefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = UndefinedMultipliedUnit.MetricAndUKImperial(this, right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([RightUnit])
 *  )
 */
@JvmName("metricAndUKImperialUndefinedXMetricAndUKImperialDefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([LeftUnit]),
 *   [RightUnit]
 *  )
 */
@JvmName("metricAndUKImperialDefinedXMetricAndUKImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = asUndefined().x(right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([LeftUnit]),
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([RightUnit])
 *  )
 */
@JvmName("metricAndUKImperialDefinedXMetricAndUKImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [RightUnit],
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("metricAndUKImperialReciprocalXMetricAndUKImperialUndefined")
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
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = right per inverse

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ( [RightUnit] ),
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("metricAndUKImperialReciprocalXMetricAndUKImperialDefined")
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
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [LeftUnit],
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("metricAndUKImperialUndefinedXMetricAndUKImperialReciprocal")
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
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ( [LeftUnit] ),
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("metricAndUKImperialDefinedXMetricAndUKImperialReciprocal")
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
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedReciprocalUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftReciprocalUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("metricAndUKImperialReciprocalXMetricAndUKImperialReciprocal")
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
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = (inverse x right.inverse).reciprocal()

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [RightNumeratorUnit]
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftReciprocalUnit],
 *   [RightDenominatorUnit]
 *   )
 *  )
 */
@JvmName("metricAndUKImperialReciprocalXMetricAndUKImperialDivided")
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
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = right.numerator per (inverse x right.denominator)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [LeftNumeratorUnit]
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftDenominatorUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("metricAndUKImperialDividedXMetricAndUKImperialReciprocal")
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
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = numerator per (denominator x right.inverse)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftNumeratorUnit],
 *   [RightUnit]
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("metricAndUKImperialDividedXMetricAndUKImperialUndefined")
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
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = (numerator x right) per denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftNumeratorUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ( [RightUnit] )
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("metricAndUKImperialDividedXMetricAndUKImperialDefined")
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
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("metricAndUKImperialUndefinedXMetricAndUKImperialDivided")
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
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = (this x right.numerator) per right.denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ( [LeftUnit] ),
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("metricAndUKImperialDefinedXMetricAndUKImperialDivided")
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
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = asUndefined() x right

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [LeftNumeratorUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *  *   [LeftDenominatorUnit],
 *  *   [RightDenominatorUnit]
 *  *   )
 *  )
 */
@JvmName("metricAndUKImperialDividedXMetricAndUKImperialDivided")
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
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUKImperial = (numerator x right.numerator) per (denominator x right.denominator)
