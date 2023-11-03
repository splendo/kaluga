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
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftUnit],
 *   [RightUnit]
 *  )
 */
@JvmName("uKImperialUndefinedXUKImperialUndefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = UndefinedMultipliedUnit.UKImperial(this, right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftUnit],
 *   [WrappedUndefinedExtendedUnit.UKImperial] ([RightUnit])
 *  )
 */
@JvmName("uKImperialUndefinedXUKImperialDefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [WrappedUndefinedExtendedUnit.UKImperial] ([LeftUnit]),
 *   [RightUnit]
 *  )
 */
@JvmName("uKImperialDefinedXUKImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = asUndefined().x(right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.UKImperial] (
 *   [WrappedUndefinedExtendedUnit.UKImperial] ([LeftUnit]),
 *   [WrappedUndefinedExtendedUnit.UKImperial] ([RightUnit])
 *  )
 */
@JvmName("uKImperialDefinedXUKImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [RightUnit],
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("uKImperialReciprocalXUKImperialUndefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = right per inverse

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [WrappedUndefinedExtendedUnit.UKImperial] ( [RightUnit] ),
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("uKImperialReciprocalXUKImperialDefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [LeftUnit],
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("uKImperialUndefinedXUKImperialReciprocal")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [WrappedUndefinedExtendedUnit.UKImperial] ( [LeftUnit] ),
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("uKImperialDefinedXUKImperialReciprocal")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedReciprocalUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftReciprocalUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("uKImperialReciprocalXUKImperialReciprocal")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = (inverse x right.inverse).reciprocal()

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [RightNumeratorUnit]
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftReciprocalUnit],
 *   [RightDenominatorUnit]
 *   )
 *  )
 */
@JvmName("uKImperialReciprocalXUKImperialDivided")
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
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = right.numerator per (inverse x right.denominator)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [LeftNumeratorUnit]
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftDenominatorUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("uKImperialDividedXUKImperialReciprocal")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = numerator per (denominator x right.inverse)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftNumeratorUnit],
 *   [RightUnit]
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("uKImperialDividedXUKImperialUndefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = (numerator x right) per denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftNumeratorUnit],
 *   [WrappedUndefinedExtendedUnit.UKImperial] ( [RightUnit] )
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("uKImperialDividedXUKImperialDefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("uKImperialUndefinedXUKImperialDivided")
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
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = (this x right.numerator) per right.denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [WrappedUndefinedExtendedUnit.UKImperial] ( [LeftUnit] ),
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("uKImperialDefinedXUKImperialDivided")
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
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = asUndefined() x right

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.UKImperial] (
 *   [UndefinedMultipliedUnit.UKImperial] (
 *   [LeftNumeratorUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.UKImperial] (
 *  *   [LeftDenominatorUnit],
 *  *   [RightDenominatorUnit]
 *  *   )
 *  )
 */
@JvmName("uKImperialDividedXUKImperialDivided")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial = (numerator x right.numerator) per (denominator x right.denominator)
