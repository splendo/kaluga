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
 * [UndefinedDividedUnit.Imperial] (
 *  [NumeratorUnit],
 *  [DenominatorUnit]
 * )
 */
@JvmName("imperialUndefinedPerImperialUndefined")
infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = UndefinedDividedUnit.Imperial(this, denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.Imperial] (
 *  [NumeratorUnit],
 *  [WrappedUndefinedExtendedUnit.Imperial] ([DenominatorUnit])
 * )
 */
@JvmName("imperialUndefinedPerImperialDefined")
infix fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.Imperial] (
 *  [WrappedUndefinedExtendedUnit.Imperial] ([NumeratorUnit]),
 *  [DenominatorUnit]
 * )
 */
@JvmName("imperialDefinedPerImperialUndefined")
infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedDividedUnit.Imperial] (
 *  [WrappedUndefinedExtendedUnit.Imperial] ([NumeratorUnit]),
 *  [WrappedUndefinedExtendedUnit.Imperial] ([DenominatorUnit])
 * )
 */
@JvmName("imperialDefinedPerImperialDefined")
infix fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.Imperial] (
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [ReciprocalUnit],
 *   [DenominatorUnit]
 *  )
 * )
 */
@JvmName("imperialReciprocalPerImperialUndefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (inverse x denominator).reciprocal()

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.Imperial] (
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [ReciprocalUnit],
 *   [WrappedUndefinedExtendedUnit.Imperial] ([DenominatorUnit])
 *  )
 * )
 */
@JvmName("imperialReciprocalPerImperialDefined")
infix fun<
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    NumeratorUnit,
    DenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedMultipliedUnit.Imperial] (
 *   [NumeratorUnit],
 *   [ReciprocalUnit]
 * )
 */
@JvmName("imperialUndefinedPerImperialReciprocal")
infix fun<
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this x denominator.inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 * [UndefinedReciprocalUnit.Imperial] (
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [WrappedUndefinedExtendedUnit.Imperial] ([NumeratorUnit])
 *   [ReciprocalUnit]
 *  )
 */
@JvmName("imperialDefinedPerImperialReciprocal")
infix fun<
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit,
    ReciprocalQuantity : UndefinedQuantityType,
    ReciprocalUnit,
    DenominatorUnit,
    > NumeratorUnit.per(denominator: DenominatorUnit) where
      NumeratorUnit : ScientificUnit<NumeratorQuantity>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      ReciprocalUnit : UndefinedScientificUnit<ReciprocalQuantity>,

      ReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      ReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<ReciprocalQuantity, ReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [DenominatorReciprocalUnit],
 *   [NumeratorReciprocalUnit]
 *  )
 */
@JvmName("imperialReciprocalPerImperialReciprocal")
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
      NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,

      DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = denominator.inverse per inverse

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [DenominatorDenominatorUnit],
 *   [UndefinedMultipliedUnit.Imperial] (
 *    [NumeratorReciprocalUnit],
 *    [DenominatorNumeratorUnit]
 *   )
 *  )
 */
@JvmName("imperialReciprocalPerImperialDivided")
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
      NumeratorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedReciprocalUnit<NumeratorReciprocalQuantity, NumeratorReciprocalUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = denominator.denominator per (inverse x denominator.numerator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *      [UndefinedMultipliedUnit.Imperial] (NumeratorNumeratorUnit, DenominatorReciprocalUnit)
 *      [NumeratorDenominatorUnit]
 *  )
 */
@JvmName("imperialDividedPerImperialReciprocal")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorReciprocalUnit : UndefinedScientificUnit<DenominatorReciprocalQuantity>,

      DenominatorReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedReciprocalUnit<DenominatorReciprocalQuantity, DenominatorReciprocalUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (numerator x denominator.inverse) per this.denominator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] ([NumeratorDenominatorUnit], [DenominatorUnit])
 *  )
 */
@JvmName("imperialDividedPerImperialUndefined")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = numerator per (this.denominator x denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *      [NumeratorNumeratorUnit]
 *      [UndefinedMultipliedUnit] (
 *       [NumeratorDenominatorUnit],
 *       [WrappedUndefinedExtendedUnit.Imperial] ([DenominatorUnit])
 *      )
 *  )
 */
@JvmName("imperialDividedPerImperialDefined")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : ScientificUnit<DenominatorQuantity>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = this.per(denominator.asUndefined())

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *    [NumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("imperialUndefinedPerImperialDivided")
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
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (this x denominator.denominator) per denominator.numerator

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *    [WrappedUndefinedExtendedUnit.Imperial] ([NumeratorUnit]),
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [DenominatorNumeratorUnit]
 *  )
 */
@JvmName("imperialDefinedPerImperialDivided")
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
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().per(denominator)

/**
 * [NumeratorUnit] per [DenominatorUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *    [NumeratorNumeratorUnit],
 *    [DenominatorDenominatorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.Imperial] (
 *    [NumeratorDenominatorUnit],
 *    [DenominatorNumeratorUnit]
 *   ),
 *  )
 */
@JvmName("imperialDividedPerImperialDivided")
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
      NumeratorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorDenominatorUnit : UndefinedScientificUnit<NumeratorDenominatorQuantity>,

      NumeratorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      NumeratorUnit : UndefinedDividedUnit<NumeratorNumeratorQuantity, NumeratorNumeratorUnit, NumeratorDenominatorQuantity, NumeratorDenominatorUnit>,

      NumeratorUnit : MeasurementUsage.UsedInUKImperial,
      NumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorNumeratorUnit : UndefinedScientificUnit<DenominatorNumeratorQuantity>,

      DenominatorNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorDenominatorUnit : UndefinedScientificUnit<DenominatorDenominatorQuantity>,

      DenominatorDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      DenominatorUnit : UndefinedDividedUnit<DenominatorNumeratorQuantity, DenominatorNumeratorUnit, DenominatorDenominatorQuantity, DenominatorDenominatorUnit>,

      DenominatorUnit : MeasurementUsage.UsedInUKImperial,
      DenominatorUnit : MeasurementUsage.UsedInUSCustomary = (numerator x denominator.denominator) per (this.denominator x denominator.numerator)
