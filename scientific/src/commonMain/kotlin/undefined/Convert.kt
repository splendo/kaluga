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

package com.splendo.kaluga.scientific.undefined

import UndefinedQuantityType
import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.RoundingMode
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.convertValue
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ExtendedUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
import kotlin.jvm.JvmName

fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    TargetUnit : UndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(convertValue(target), target)

fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    TargetUnit : UndefinedScientificUnit<Quantity>,
    > UndefinedScientificValue<Quantity, Unit>.convert(target: TargetUnit) = convert(target, ::DefaultUndefinedScientificValue)

fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    TargetUnit : UndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(convertValue(target, round, roundingMode), target)

fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    TargetUnit : UndefinedScientificUnit<Quantity>,
    > UndefinedScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    ) = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndImperial
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedMetricValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetric
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInImperial
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUKImperial
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUSCustomary
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedMetricAndUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUKImperial
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedMetricAndUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUSCustomary
    = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedMetricAndImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndImperial
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetric
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInImperial
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUKImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUKImperial
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUSCustomaryValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUSCustomary
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUKImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUKImperial
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUSCustomaryValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
    Unit : AbstractScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUSCustomary
    = convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndImperial
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetric
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInImperial
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUKImperial
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUSCustomary
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUKImperial
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUSCustomary
    = convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndImperial
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetric
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInImperial
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUKImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUKImperial
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUSCustomaryValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInUSCustomary
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUKImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : ScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUKImperial
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUSCustomaryValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : ExtendedUndefinedScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
    Unit : AbstractScientificUnit<Quantity>,
    Unit : MeasurementUsage.UsedInMetricAndUSCustomary
    = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertMetricAndImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndImperial = factory(convertValue(target.asUndefined()), target)

@JvmName("convertMetricExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetric = factory(convertValue(target.asUndefined()), target)

@JvmName("convertImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInImperial = factory(convertValue(target.asUndefined()), target)

@JvmName("convertUKImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUKImperial = factory(convertValue(target.asUndefined()), target)

@JvmName("convertUSCustomaryExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUSCustomary = factory(convertValue(target.asUndefined()), target)

@JvmName("convertMetricAndUKImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUKImperial = factory(convertValue(target.asUndefined()), target)

@JvmName("convertMetricAndUSCustomaryExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUSCustomary = factory(convertValue(target.asUndefined()), target)

@JvmName("convertMetricAndImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetric = convert(target, ::DefaultScientificValue)

@JvmName("convertImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUKImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUSCustomary = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndImperial = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertMetricExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetric = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInImperial = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertUKImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUKImperial = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertUSCustomaryExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUSCustomary = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertMetricAndUKImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUKImperial = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertMetricAndUSCustomaryExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where TargetUnit : ScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUSCustomary = factory(convertValue(target.asUndefined(), round, roundingMode), target)

@JvmName("convertMetricAndImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndImperial = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertMetricExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetric = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInImperial = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertMetricAndUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUKImperial = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ExtendedUndefinedScientificUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where TargetUnit : AbstractScientificUnit<Quantity>, TargetUnit : MeasurementUsage.UsedInMetricAndUSCustomary = convert(target, round, roundingMode, ::DefaultScientificValue)
