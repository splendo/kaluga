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
 *  [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftUnit],
 *   [RightUnit]
 *  )
 */
@JvmName("uSCustomaryUndefinedXUSCustomaryUndefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = UndefinedMultipliedUnit.USCustomary(this, right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftUnit],
 *   [WrappedUndefinedExtendedUnit.USCustomary] ([RightUnit])
 *  )
 */
@JvmName("uSCustomaryUndefinedXUSCustomaryDefined")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.USCustomary] (
 *   [WrappedUndefinedExtendedUnit.USCustomary] ([LeftUnit]),
 *   [RightUnit]
 *  )
 */
@JvmName("uSCustomaryDefinedXUSCustomaryUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined().x(right)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedMultipliedUnit.USCustomary] (
 *   [WrappedUndefinedExtendedUnit.USCustomary] ([LeftUnit]),
 *   [WrappedUndefinedExtendedUnit.USCustomary] ([RightUnit])
 *  )
 */
@JvmName("uSCustomaryDefinedXUSCustomaryUndefined")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [RightUnit],
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("uSCustomaryReciprocalXUSCustomaryUndefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right per inverse

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [WrappedUndefinedExtendedUnit.USCustomary] ( [RightUnit] ),
 *   [LeftReciprocalUnit]
 *  )
 */
@JvmName("uSCustomaryReciprocalXUSCustomaryDefined")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [LeftUnit],
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("uSCustomaryUndefinedXUSCustomaryReciprocal")
infix fun <
    LeftQuantity : UndefinedQuantityType,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : UndefinedScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [WrappedUndefinedExtendedUnit.USCustomary] ( [LeftUnit] ),
 *   [RightReciprocalUnit]
 *  )
 */
@JvmName("uSCustomaryDefinedXUSCustomaryReciprocal")
infix fun <
    LeftQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftUnit : ScientificUnit<LeftQuantity>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right x this

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedReciprocalUnit.USCustomary] (
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftReciprocalUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("uSCustomaryReciprocalXUSCustomaryReciprocal")
infix fun <
    LeftReciprocalQuantity : UndefinedQuantityType,
    LeftReciprocalUnit,
    LeftUnit,
    RightQuantity : UndefinedQuantityType,
    RightReciprocalUnit,
    RightUnit,
    > LeftUnit.x(right: RightUnit) where
      LeftReciprocalUnit : UndefinedScientificUnit<LeftReciprocalQuantity>,
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (inverse x right.inverse).reciprocal()

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [RightNumeratorUnit]
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftReciprocalUnit],
 *   [RightDenominatorUnit]
 *   )
 *  )
 */
@JvmName("uSCustomaryReciprocalXUSCustomaryDivided")
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
      LeftReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedReciprocalUnit<LeftReciprocalQuantity, LeftReciprocalUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = right.numerator per (inverse x right.denominator)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [LeftNumeratorUnit]
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftDenominatorUnit],
 *   [RightReciprocalUnit]
 *   )
 *  )
 */
@JvmName("uSCustomaryDividedXUSCustomaryReciprocal")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightReciprocalUnit : UndefinedScientificUnit<RightReciprocalQuantity>,
      RightReciprocalUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedReciprocalUnit<RightReciprocalQuantity, RightReciprocalUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = numerator per (denominator x right.inverse)

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftNumeratorUnit],
 *   [RightUnit]
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("uSCustomaryDividedXUSCustomaryUndefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right) per denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftNumeratorUnit],
 *   [WrappedUndefinedExtendedUnit.USCustomary] ( [RightUnit] )
 *   ),
 *   [LeftDenominatorUnit]
 *  )
 */
@JvmName("uSCustomaryDividedXUSCustomaryDefined")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : ScientificUnit<RightQuantity>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = x(right.asUndefined())

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("uSCustomaryUndefinedXUSCustomaryDivided")
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
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (this x right.numerator) per right.denominator

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [WrappedUndefinedExtendedUnit.USCustomary] ( [LeftUnit] ),
 *   [RightNumeratorUnit]
 *   ),
 *   [RightDenominatorUnit]
 *  )
 */
@JvmName("uSCustomaryDefinedXUSCustomaryDivided")
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
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = asUndefined() x right

/**
 * [LeftUnit] x [RightUnit] ->
 *  [UndefinedDividedUnit.USCustomary] (
 *   [UndefinedMultipliedUnit.USCustomary] (
 *   [LeftNumeratorUnit],
 *   [RightNumeratorUnit]
 *   ),
 *   [UndefinedMultipliedUnit.USCustomary] (
 *  *   [LeftDenominatorUnit],
 *  *   [RightDenominatorUnit]
 *  *   )
 *  )
 */
@JvmName("uSCustomaryDividedXUSCustomaryDivided")
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
      LeftNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorQuantity>,
      LeftDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      LeftUnit : UndefinedDividedUnit<LeftNumeratorQuantity, LeftNumeratorUnit, LeftDenominatorQuantity, LeftDenominatorUnit>,
      LeftUnit : MeasurementUsage.UsedInUSCustomary,
      RightNumeratorUnit : UndefinedScientificUnit<RightNumeratorQuantity>,
      RightNumeratorUnit : MeasurementUsage.UsedInUSCustomary,
      RightDenominatorUnit : UndefinedScientificUnit<RightDenominatorQuantity>,
      RightDenominatorUnit : MeasurementUsage.UsedInUSCustomary,
      RightUnit : UndefinedDividedUnit<RightNumeratorQuantity, RightNumeratorUnit, RightDenominatorQuantity, RightDenominatorUnit>,
      RightUnit : MeasurementUsage.UsedInUSCustomary = (numerator x right.numerator) per (denominator x right.denominator)
