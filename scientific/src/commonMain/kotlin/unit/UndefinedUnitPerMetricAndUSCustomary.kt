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
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUSCustomary] (
 *  [NumeratorUnit],
 *  [DenominatorUnit]
 * )
 */
@JvmName("metricAndUSCustomaryUndefinedPerMetricAndUSCustomaryUndefined")
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

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = UndefinedDividedUnit.MetricAndUSCustomary(this, denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUSCustomary] (
 *  [NumeratorUnit],
 *  [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([DenominatorUnit])
 * )
 */
@JvmName("metricAndUSCustomaryUndefinedPerMetricAndUSCustomaryDefined")
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

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUSCustomary] (
 *  [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([NumeratorUnit]),
 *  [DenominatorUnit]
 * )
 */
@JvmName("metricAndUSCustomaryDefinedPerMetricAndUSCustomaryUndefined")
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

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUSCustomary] (
 *  [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([NumeratorUnit]),
 *  [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([DenominatorUnit])
 * )
 */
@JvmName("metricAndUSCustomaryDefinedPerMetricAndUSCustomaryDefined")
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

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.MetricAndUSCustomary] (
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [ReciprocalUnit],
 *   [DenominatorUnit]
 *  )
 * )
 */
@JvmName("metricAndUSCustomaryReciprocalPerMetricAndUSCustomaryUndefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,

      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (inverse x denominator).reciprocal()

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.MetricAndUSCustomary] (
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [ReciprocalUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([DenominatorUnit])
 *  )
 * )
 */
@JvmName("metricAndUSCustomaryReciprocalPerMetricAndUSCustomaryDefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,

      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [NumeratorUnit],
 *   [ReciprocalUnit]
 * )
 */
@JvmName("metricAndUSCustomaryUndefinedPerMetricAndUSCustomaryReciprocal")
infix fun<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,

      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this x denominator.inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.MetricAndUSCustomary] (
 *  [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([NumeratorUnit])
 *   [ReciprocalUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDefinedPerMetricAndUSCustomaryReciprocal")
infix fun<
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,

      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [DenominatorReciprocalUnit],
 *   [NumeratorReciprocalUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryReciprocalPerMetricAndUSCustomaryReciprocal")
infix fun<
    NumeratorReciprocalQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit,
    NumeratorUnit,
    DenominatorReciprocalQuantity : UndefinedQuantityType,
    DenominatorReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
      NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,

      NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,

      DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = denominator.inverse per inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [DenominatorDenominatorUnit],
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *    [NumeratorReciprocalUnit],
 *    [DenominatorNumeratorUnit]
 *   )
 *  )
 */
@JvmName("metricAndUSCustomaryReciprocalPerMetricAndUSCustomaryDivided")
infix fun<
    NumeratorReciprocalQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit,
    NumeratorUnit,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,
      NumeratorReciprocalUnit : MeasurementUsage.UsedInMetric,

      NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = denominator.denominator per (inverse x denominator.numerator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *      [UndefinedMultipliedUnit.MetricAndUSCustomary] (NumeratorNumeratorUnit, DenominatorReciprocalUnit)
 *      [NumeratorDenominatorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDividedPerMetricAndUSCustomaryReciprocal")
infix fun<
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit,
    NumeratorUnit,
    DenominatorReciprocalQuantity : UndefinedQuantityType,
    DenominatorReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
      NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,

      DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (numerator x denominator.inverse) per this.denominator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] ([NumeratorDenominatorUnit], [DenominatorUnit])
 *  )
 */
@JvmName("metricAndUSCustomaryDividedPerMetricAndUSCustomaryUndefined")
infix fun<
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
      NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = numerator per (this.denominator x denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] (
 *       [NumeratorDenominatorUnit],
 *       [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([DenominatorUnit])
 *      )
 *  )
 */
@JvmName("metricAndUSCustomaryDividedPerMetricAndUSCustomaryDefined")
infix fun<
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
      NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *    [NumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryUndefinedPerMetricAndUSCustomaryDivided")
infix fun<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (this x denominator.denominator) per denominator.numerator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *    [WrappedUndefinedExtendedUnit.MetricAndUSCustomary] ([NumeratorUnit]),
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("metricAndUSCustomaryDefinedPerMetricAndUSCustomaryDivided")
infix fun<
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUSCustomary] (
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *    [NumeratorNumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.MetricAndUSCustomary] (
 *    [NumeratorDenominatorUnit],
 *    [DenominatorNumeratorUnit]
 *   ),
 *  )
 */
@JvmName("metricAndUSCustomaryDividedPerMetricAndUSCustomaryDivided")
infix fun<
    NumeratorNumeratorQuantity : UndefinedQuantityType,
    NumeratorNumeratorUnit,
    NumeratorDenominatorQuantity : UndefinedQuantityType,
    NumeratorDenominatorUnit,
    NumeratorUnit,
    DenominatorNumeratorQuantity : UndefinedQuantityType,
    DenominatorNumeratorUnit,
    DenominatorDenominatorQuantity : UndefinedQuantityType,
    DenominatorDenominatorUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorNumeratorUnit : UndefinedScientificUnit<NumeratorNumeratorQuantity>,
      NumeratorNumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,

      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,

      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (numerator x denominator.denominator) per (this.denominator x denominator.numerator)
