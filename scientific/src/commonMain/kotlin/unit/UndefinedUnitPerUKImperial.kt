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
 * [UndefinedDividedUnit.UKImperial] (
 *  [NumeratorUnit],
 *  [DenominatorUnit]
 * )
 */
@JvmName("ukImperialUndefinedPerUKImperialUndefined")
infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = UndefinedDividedUnit.UKImperial(this, denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.UKImperial] (
 *  [NumeratorUnit],
 *  [WrappedUndefinedExtendedUnit.UKImperial] ([DenominatorUnit])
 * )
 */
@JvmName("ukImperialUndefinedPerUKImperialDefined")
infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.UKImperial] (
 *  [WrappedUndefinedExtendedUnit.UKImperial] ([NumeratorUnit]),
 *  [DenominatorUnit]
 * )
 */
@JvmName("ukImperialDefinedPerUKImperialUndefined")
infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.UKImperial] (
 *  [WrappedUndefinedExtendedUnit.UKImperial] ([NumeratorUnit]),
 *  [WrappedUndefinedExtendedUnit.UKImperial] ([DenominatorUnit])
 * )
 */
@JvmName("ukImperialDefinedPerUKImperialDefined")
infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.UKImperial] (
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [ReciprocalUnit],
 *   [DenominatorUnit]
 *  )
 * )
 */
@JvmName("ukImperialReciprocalPerUKImperialUndefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (inverse x denominator).reciprocal()

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.UKImperial] (
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [ReciprocalUnit],
 *   [WrappedUndefinedExtendedUnit.UKImperial] ([DenominatorUnit])
 *  )
 * )
 */
@JvmName("ukImperialReciprocalPerUKImperialDefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedMultipliedUnit.UKImperial] (
 *   [NumeratorUnit],
 *   [ReciprocalUnit]
 * )
 */
@JvmName("ukImperialUndefinedPerUKImperialReciprocal")
infix fun<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this x denominator.inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.UKImperial] (
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [WrappedUndefinedExtendedUnit.UKImperial] ([NumeratorUnit])
 *   [ReciprocalUnit]
 *  )
 */
@JvmName("ukImperialDefinedPerUKImperialReciprocal")
infix fun<
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [DenominatorReciprocalUnit],
 *   [NumeratorReciprocalUnit]
 *  )
 */
@JvmName("ukImperialReciprocalPerUKImperialReciprocal")
infix fun<
    NumeratorReciprocalQuantity : UndefinedQuantityType,
    NumeratorReciprocalUnit,
    NumeratorUnit,
    DenominatorReciprocalQuantity : UndefinedQuantityType,
    DenominatorReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorReciprocalUnit : UndefinedScientificUnit<NumeratorReciprocalQuantity>,

      NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,

      DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = denominator.inverse per inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [DenominatorDenominatorUnit],
 *   [UndefinedMultipliedUnit.UKImperial] (
 *    [NumeratorReciprocalUnit],
 *    [DenominatorNumeratorUnit]
 *   )
 *  )
 */
@JvmName("ukImperialReciprocalPerUKImperialDivided")
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

      NumeratorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = denominator.denominator per (inverse x denominator.numerator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *      [UndefinedMultipliedUnit.UKImperial] (NumeratorNumeratorUnit, DenominatorReciprocalUnit)
 *      [NumeratorDenominatorUnit]
 *  )
 */
@JvmName("ukImperialDividedPerUKImperialReciprocal")
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

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,

      DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (numerator x denominator.inverse) per this.denominator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] ([NumeratorDenominatorUnit], [DenominatorUnit])
 *  )
 */
@JvmName("ukImperialDividedPerUKImperialUndefined")
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

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = numerator per (this.denominator x denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] (
 *       [NumeratorDenominatorUnit],
 *       [WrappedUndefinedExtendedUnit.UKImperial] ([DenominatorUnit])
 *      )
 *  )
 */
@JvmName("ukImperialDividedPerUKImperialDefined")
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

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *    [NumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("ukImperialUndefinedPerUKImperialDivided")
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

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (this x denominator.denominator) per denominator.numerator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *    [WrappedUndefinedExtendedUnit.UKImperial] ([NumeratorUnit]),
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("ukImperialDefinedPerUKImperialDivided")
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

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *    [NumeratorNumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.UKImperial] (
 *    [NumeratorDenominatorUnit],
 *    [DenominatorNumeratorUnit]
 *   ),
 *  )
 */
@JvmName("ukImperialDividedPerUKImperialDivided")
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

      NumeratorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial = (numerator x denominator.denominator) per (this.denominator x denominator.numerator)
