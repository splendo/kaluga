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

package com.splendo.kaluga.scientific.converter.undefined

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.UndefinedScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.One
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedDividedUnit
import com.splendo.kaluga.scientific.unit.UndefinedReciprocalUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit

fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
    TargetValue : UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, LeftAndDenominatorQuantity>, DividerUnit>,
    factory: (Decimal, NumeratorUnit) -> TargetValue,
) = right.unit.numerator.byMultiplying(this, right, factory)

// To add cases for

fun <
    LeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : ScientificUnit<LeftAndDenominatorQuantity>,
    WrappedLeftUnit : UndefinedScientificUnit<UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>>,
    WrappedLeftValue : UndefinedScientificValue<UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>, WrappedLeftUnit>,
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>>,
    DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>, DenominatorUnit>,
    TargetValue : UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>,
    > ScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>>, DividerUnit>,
    leftAsUndefined: ScientificValue<LeftAndDenominatorQuantity, LeftUnit>.() -> WrappedLeftValue,
    factory: (Decimal, NumeratorUnit) -> TargetValue,
) = leftAsUndefined().times(right, factory)

fun <
    LeftAndDenominatorQuantity : UndefinedQuantityType,
    LeftUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedNumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorQuantity, WrappedNumeratorUnit>,
    DenominatorUnit : UndefinedScientificUnit<LeftAndDenominatorQuantity>,
    DividerUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorQuantity>, NumeratorUnit, LeftAndDenominatorQuantity, DenominatorUnit>,
    WrappedTargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorQuantity>, NumeratorUnit>,
    TargetValue : ScientificValue<NumeratorQuantity, WrappedNumeratorUnit>,
    > UndefinedScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorQuantity>, LeftAndDenominatorQuantity>, DividerUnit>,
    wrappedFactory: (Decimal, NumeratorUnit) -> WrappedTargetValue,
    factory: (Decimal, WrappedNumeratorUnit) -> TargetValue,
) = times(right, wrappedFactory).let { wrapped -> factory(wrapped.decimalValue, wrapped.unit.wrapped) }

fun <
    LeftAndDenominatorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    LeftUnit : ScientificUnit<LeftAndDenominatorQuantity>,
    WrappedLeftUnit : UndefinedScientificUnit<UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>>,
    WrappedLeftValue : UndefinedScientificValue<UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>, WrappedLeftUnit>,
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    WrappedNumeratorUnit : ScientificUnit<NumeratorQuantity>,
    NumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorQuantity, WrappedNumeratorUnit>,
    DenominatorUnit : UndefinedScientificUnit<UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>>,
    DividerUnit : UndefinedDividedUnit<
        UndefinedQuantityType.Extended<NumeratorQuantity>,
        NumeratorUnit,
        UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>,
        DenominatorUnit,
        >,
    WrappedTargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorQuantity>, NumeratorUnit>,
    TargetValue : ScientificValue<NumeratorQuantity, WrappedNumeratorUnit>,
    > ScientificValue<LeftAndDenominatorQuantity, LeftUnit>.times(
    right: UndefinedScientificValue<
        UndefinedQuantityType.Dividing<
            UndefinedQuantityType.Extended<NumeratorQuantity>,
            UndefinedQuantityType.Extended<LeftAndDenominatorQuantity>,
            >,
        DividerUnit,
        >,
    leftAsUndefined: ScientificValue<LeftAndDenominatorQuantity, LeftUnit>.() -> WrappedLeftValue,
    wrappedFactory: (Decimal, NumeratorUnit) -> WrappedTargetValue,
    factory: (Decimal, WrappedNumeratorUnit) -> TargetValue,
) = leftAsUndefined().times(right, wrappedFactory, factory)

fun <
    LeftNumeratorRightDenominatorQuantity : UndefinedQuantityType,
    LeftDenominatorRightNumeratorQuantity : UndefinedQuantityType,
    LeftNumeratorUnit : UndefinedScientificUnit<LeftNumeratorRightDenominatorQuantity>,
    LeftDenominatorUnit : UndefinedScientificUnit<LeftDenominatorRightNumeratorQuantity>,
    LeftDividerUnit : UndefinedDividedUnit<LeftNumeratorRightDenominatorQuantity, LeftNumeratorUnit, LeftDenominatorRightNumeratorQuantity, LeftDenominatorUnit>,
    RightNumeratorUnit : UndefinedScientificUnit<LeftDenominatorRightNumeratorQuantity>,
    RightDenominatorUnit : UndefinedScientificUnit<LeftNumeratorRightDenominatorQuantity>,
    RightDividerUnit : UndefinedDividedUnit<LeftDenominatorRightNumeratorQuantity, RightNumeratorUnit, LeftNumeratorRightDenominatorQuantity, RightDenominatorUnit>,
    TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, One>,
    > UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftNumeratorRightDenominatorQuantity, LeftDenominatorRightNumeratorQuantity>, LeftDividerUnit>.times(
    right: UndefinedScientificValue<UndefinedQuantityType.Dividing<LeftDenominatorRightNumeratorQuantity, LeftNumeratorRightDenominatorQuantity>, RightDividerUnit>,
    factory: (Decimal, One) -> TargetValue,
) = One.byMultiplying(this, right, factory)

fun <
    ReciprocalQuantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<ReciprocalQuantity>,
    ReciprocalUnit : UndefinedScientificUnit<UndefinedQuantityType.Reciprocal<ReciprocalQuantity>>,
    TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, One>,
    > UndefinedScientificValue<ReciprocalQuantity, Unit>.times(
    reciprocal: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<ReciprocalQuantity>, ReciprocalUnit>,
    factory: (Decimal, One) -> TargetValue,
) = One.byMultiplying(this, reciprocal, factory)

fun <
    ReciprocalQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ScientificUnit<ReciprocalQuantity>,
    WrappedUnit : WrappedUndefinedExtendedUnit<ReciprocalQuantity, Unit>,
    WrappedValue : UndefinedScientificValue<UndefinedQuantityType.Extended<ReciprocalQuantity>, WrappedUnit>,
    ReciprocalUnit : UndefinedScientificUnit<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<ReciprocalQuantity>>>,
    TargetValue : ScientificValue<PhysicalQuantity.Dimensionless, One>,
    > ScientificValue<ReciprocalQuantity, Unit>.times(
    reciprocal: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<UndefinedQuantityType.Extended<ReciprocalQuantity>>, ReciprocalUnit>,
    asUndefined: ScientificValue<ReciprocalQuantity, Unit>.() -> WrappedValue,
    factory: (Decimal, One) -> TargetValue,
) = asUndefined().times(reciprocal, factory)

fun <
    NumeratorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    ReciprocalUnit : UndefinedReciprocalUnit<DenominatorQuantity, DenominatorUnit>,
    DividingUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, DenominatorQuantity, DenominatorUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, DenominatorQuantity>, DividingUnit>,
    > UndefinedScientificValue<NumeratorQuantity, NumeratorUnit>.times(
    reciprocal: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorQuantity>, ReciprocalUnit>,
    per: NumeratorUnit.(DenominatorUnit) -> DividingUnit,
    factory: (Decimal, DividingUnit) -> TargetValue,
) = unit.per(reciprocal.unit.inverse).byMultiplying(this, reciprocal, factory)

fun <
    NumeratorQuantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    NumeratorUnit : ScientificUnit<NumeratorQuantity>,
    WrappedNumeratorUnit : WrappedUndefinedExtendedUnit<NumeratorQuantity, NumeratorUnit>,
    WrappedValue : UndefinedScientificValue<UndefinedQuantityType.Extended<NumeratorQuantity>, WrappedNumeratorUnit>,
    DenominatorQuantity : UndefinedQuantityType,
    DenominatorUnit : UndefinedScientificUnit<DenominatorQuantity>,
    ReciprocalUnit : UndefinedReciprocalUnit<DenominatorQuantity, DenominatorUnit>,
    DividingUnit : UndefinedDividedUnit<UndefinedQuantityType.Extended<NumeratorQuantity>, WrappedNumeratorUnit, DenominatorQuantity, DenominatorUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Dividing<UndefinedQuantityType.Extended<NumeratorQuantity>, DenominatorQuantity>, DividingUnit>,
    > ScientificValue<NumeratorQuantity, NumeratorUnit>.times(
    reciprocal: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<DenominatorQuantity>, ReciprocalUnit>,
    asUndefined: ScientificValue<NumeratorQuantity, NumeratorUnit>.() -> WrappedValue,
    per: WrappedNumeratorUnit.(DenominatorUnit) -> DividingUnit,
    factory: (Decimal, DividingUnit) -> TargetValue,
) = asUndefined().times(reciprocal, per, factory)

fun <
    NumeratorQuantity : UndefinedQuantityType,
    ReciprocalAndDenominatorQuantity : UndefinedQuantityType,
    NumeratorUnit : UndefinedScientificUnit<NumeratorQuantity>,
    DenominatorUnit : UndefinedScientificUnit<ReciprocalAndDenominatorQuantity>,
    DividerUnit : UndefinedDividedUnit<NumeratorQuantity, NumeratorUnit, ReciprocalAndDenominatorQuantity, DenominatorUnit>,
    ReciprocalInverseUnit : UndefinedScientificUnit<ReciprocalAndDenominatorQuantity>,
    ReciprocalUnit : UndefinedReciprocalUnit<ReciprocalAndDenominatorQuantity, ReciprocalInverseUnit>,
    TargetUnit : UndefinedReciprocalUnit<NumeratorQuantity, NumeratorUnit>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Reciprocal<NumeratorQuantity>, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Dividing<NumeratorQuantity, ReciprocalAndDenominatorQuantity>, DividerUnit>.times(
    reciprocal: UndefinedScientificValue<UndefinedQuantityType.Reciprocal<ReciprocalAndDenominatorQuantity>, ReciprocalUnit>,
    inverse: NumeratorUnit.() -> TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
) = unit.numerator.inverse().byMultiplying(this, reciprocal, factory)

// Mul<A, B> * Inv<B> -> A
// Inv<B> * Mul<A, B> -> A

// Mul<Wr<A>, B> * Inv<B> -> A
// Inv<B> * Mul<Wr<A>, B> -> A

// Mul<A, B> * Inv<A> -> B
// Inv<A> * Mul<A, B> -> B

// Mul<A, Wr<B>> * Inv<A> -> B
// Inv<A> * Mul<A, Wr<B>> -> B
