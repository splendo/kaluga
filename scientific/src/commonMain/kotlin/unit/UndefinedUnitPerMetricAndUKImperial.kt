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
 * [UndefinedDividedUnit.MetricAndUKImperial] (
 *  [NumeratorUnit],
 *  [DenominatorUnit]
 * )
 */
@JvmName("metricAndUKImperialUndefinedPerMetricAndUKImperialUndefined")
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
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = UndefinedDividedUnit.MetricAndUKImperial(this, denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUKImperial] (
 *  [NumeratorUnit],
 *  [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([DenominatorUnit])
 * )
 */
@JvmName("metricAndUKImperialUndefinedPerMetricAndUKImperialDefined")
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
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUKImperial] (
 *  [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([NumeratorUnit]),
 *  [DenominatorUnit]
 * )
 */
@JvmName("metricAndUKImperialDefinedPerMetricAndUKImperialUndefined")
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
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.MetricAndUKImperial] (
 *  [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([NumeratorUnit]),
 *  [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([DenominatorUnit])
 * )
 */
@JvmName("metricAndUKImperialDefinedPerMetricAndUKImperialDefined")
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
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.MetricAndUKImperial] (
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [ReciprocalUnit],
 *   [DenominatorUnit]
 *  )
 * )
 */
@JvmName("metricAndUKImperialReciprocalPerMetricAndUKImperialUndefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (inverse x denominator).reciprocal()

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.MetricAndUKImperial] (
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [ReciprocalUnit],
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([DenominatorUnit])
 *  )
 * )
 */
@JvmName("metricAndUKImperialReciprocalPerMetricAndUKImperialDefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [NumeratorUnit],
 *   [ReciprocalUnit]
 * )
 */
@JvmName("metricAndUKImperialUndefinedPerMetricAndUKImperialReciprocal")
infix fun<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this x denominator.inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.MetricAndUKImperial] (
 *  [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *   [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([NumeratorUnit])
 *   [ReciprocalUnit]
 *  )
 */
@JvmName("metricAndUKImperialDefinedPerMetricAndUKImperialReciprocal")
infix fun<
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,
      ReciprocalUnit : MeasurementUsage.UsedInMetric,
      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [DenominatorReciprocalUnit],
 *   [NumeratorReciprocalUnit]
 *  )
 */
@JvmName("metricAndUKImperialReciprocalPerMetricAndUKImperialReciprocal")
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
      NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = denominator.inverse per inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [DenominatorDenominatorUnit],
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *    [NumeratorReciprocalUnit],
 *    [DenominatorNumeratorUnit]
 *   )
 *  )
 */
@JvmName("metricAndUKImperialReciprocalPerMetricAndUKImperialDivided")
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
      NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = denominator.denominator per (inverse x denominator.numerator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *      [UndefinedMultipliedUnit.MetricAndUKImperial] (NumeratorNumeratorUnit, DenominatorReciprocalUnit)
 *      [NumeratorDenominatorUnit]
 *  )
 */
@JvmName("metricAndUKImperialDividedPerMetricAndUKImperialReciprocal")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInMetric,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (numerator x denominator.inverse) per this.denominator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] ([NumeratorDenominatorUnit], [DenominatorUnit])
 *  )
 */
@JvmName("metricAndUKImperialDividedPerMetricAndUKImperialUndefined")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = numerator per (this.denominator x denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] (
 *       [NumeratorDenominatorUnit],
 *       [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([DenominatorUnit])
 *      )
 *  )
 */
@JvmName("metricAndUKImperialDividedPerMetricAndUKImperialDefined")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *    [NumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("metricAndUKImperialUndefinedPerMetricAndUKImperialDivided")
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
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (this x denominator.denominator) per denominator.numerator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *    [WrappedUndefinedExtendedUnit.MetricAndUKImperial] ([NumeratorUnit]),
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("metricAndUKImperialDefinedPerMetricAndUKImperialDivided")
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
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.MetricAndUKImperial] (
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *    [NumeratorNumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.MetricAndUKImperial] (
 *    [NumeratorDenominatorUnit],
 *    [DenominatorNumeratorUnit]
 *   ),
 *  )
 */
@JvmName("metricAndUKImperialDividedPerMetricAndUKImperialDivided")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInMetric,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,
      NumeratorUnit : MeasurementUsage.UsedInMetric,
      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInMetric,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,
      DenominatorUnit : MeasurementUsage.UsedInMetric,
      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (numerator x denominator.denominator) per (this.denominator x denominator.numerator)
