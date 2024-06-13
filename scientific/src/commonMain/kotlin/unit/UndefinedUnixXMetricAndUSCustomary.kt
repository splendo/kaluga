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
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftUnit],
 *   [RightUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryUndefinedXMetricAndUSCustomaryUndefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = UndefinedMultipliedUnit.MetricAndUSCustomary(this, right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([RightUnit])
 *  )
 */
@JvmName("metricAndUSCustomaryUndefinedXMetricAndUSCustomaryDefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([LeftUnit]),
 *   [RightUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDefinedXMetricAndUSCustomaryUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().x(right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([LeftUnit]),
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([RightUnit])
 *  )
 */
@JvmName("metricAndUSCustomaryDefinedXMetricAndUSCustomaryUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [RightUnit],
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryReciprocalXMetricAndUSCustomaryUndefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right per inverse

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ( [RightUnit] ),
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryReciprocalXMetricAndUSCustomaryDefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInMetric,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [LeftUnit],
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryUndefinedXMetricAndUSCustomaryReciprocal")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ( [LeftUnit] ),
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDefinedXMetricAndUSCustomaryReciprocal")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedReciprocalUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftReciprocalUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("metricAndUSCustomaryReciprocalXMetricAndUSCustomaryReciprocal")
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
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (inverse x right.inverse).reciprocal()

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [RightNumeratorUnit]
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftReciprocalUnit],
 *   [RightDenominatorUnit]
 *   )
 *  )
 */
@JvmName("metricAndUSCustomaryReciprocalXMetricAndUSCustomaryDivided")
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
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right.numerator per (inverse x right.denominator)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [LeftNumeratorUnit]
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftDenominatorUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("metricAndUSCustomaryDividedXMetricAndUSCustomaryReciprocal")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInMetric,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = numerator per (denominator x right.inverse)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftNumeratorUnit],
 *   [RightUnit]
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDividedXMetricAndUSCustomaryUndefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right) per denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftNumeratorUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ( [RightUnit] )
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDividedXMetricAndUSCustomaryDefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryUndefinedXMetricAndUSCustomaryDivided")
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
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (this x right.numerator) per right.denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ( [LeftUnit] ),
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDefinedXMetricAndUSCustomaryDivided")
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
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined() x right

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [LeftNumeratorUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *  *   [LeftDenominatorUnit],
 *  *   [RightDenominatorUnit]
 *  *   )
 *  )
 */
@JvmName("metricAndUSCustomaryDividedXMetricAndUSCustomaryDivided")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInMetric,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInMetric,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInMetric,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInMetric,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInMetric,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right.numerator) per (denominator x right.denominator)
