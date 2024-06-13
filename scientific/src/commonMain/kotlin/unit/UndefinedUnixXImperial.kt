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
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [LeftUnit],
 *   [RightUnit]
 *  )
 */
@JvmName("imperialUndefinedXImperialUndefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = UndefinedMultipliedUnit.Imperial(this, right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [LeftUnit],
 *   [WrappedUndefinedExtendedUnit.Imperial] ([RightUnit])
 *  )
 */
@JvmName("imperialUndefinedXImperialDefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [WrappedUndefinedExtendedUnit.Imperial] ([LeftUnit]),
 *   [RightUnit]
 *  )
 */
@JvmName("imperialDefinedXImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().x(right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.Imperial] (
 *   [WrappedUndefinedExtendedUnit.Imperial] ([LeftUnit]),
 *   [WrappedUndefinedExtendedUnit.Imperial] ([RightUnit])
 *  )
 */
@JvmName("imperialDefinedXImperialUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [RightUnit],
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("imperialReciprocalXImperialUndefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right per inverse

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [WrappedUndefinedExtendedUnit.Imperial] ( [RightUnit] ),
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("imperialReciprocalXImperialDefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [LeftUnit],
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("imperialUndefinedXImperialReciprocal")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [WrappedUndefinedExtendedUnit.Imperial] ( [LeftUnit] ),
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("imperialDefinedXImperialReciprocal")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedReciprocalUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftReciprocalUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("imperialReciprocalXImperialReciprocal")
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
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (inverse x right.inverse).reciprocal()

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [RightNumeratorUnit]
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftReciprocalUnit],
 *   [RightDenominatorUnit]
 *   )
 *  )
 */
@JvmName("imperialReciprocalXImperialDivided")
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
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right.numerator per (inverse x right.denominator)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [LeftNumeratorUnit]
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftDenominatorUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("imperialDividedXImperialReciprocal")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUKImperial,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = numerator per (denominator x right.inverse)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftNumeratorUnit],
 *   [RightUnit]
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("imperialDividedXImperialUndefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right) per denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftNumeratorUnit],
 *   [WrappedUndefinedExtendedUnit.Imperial] ( [RightUnit] )
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("imperialDividedXImperialDefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("imperialUndefinedXImperialDivided")
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
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (this x right.numerator) per right.denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [WrappedUndefinedExtendedUnit.Imperial] ( [LeftUnit] ),
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("imperialDefinedXImperialDivided")
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
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined() x right

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.Imperial] (
 *   [UndefinedMultipliedUnit.Imperial] (
 *   [LeftNumeratorUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.Imperial] (
 *  *   [LeftDenominatorUnit],
 *  *   [RightDenominatorUnit]
 *  *   )
 *  )
 */
@JvmName("imperialDividedXImperialDivided")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUKImperial,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUKImperial,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUKImperial,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUKImperial,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right.numerator) per (denominator x right.denominator)
