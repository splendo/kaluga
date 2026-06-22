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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.base.decimal.RoundingMode
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.AbstractUndefinedScientificUnit
import com.splendo.kaluga.scientific.unit.DefinedScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.WrappedUndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.asUndefined
import com.splendo.kaluga.scientific.unit.convert
import kotlin.jvm.JvmName

/**
 * Converts a [ScientificValue] into another [ScientificValue] with a [ScientificUnit] of the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of the current [ScientificValue] as well as the [TargetValue]
 * @param Unit the type of [ScientificUnit] of the current [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] of the [TargetValue]
 * @param TargetValue the type of [ScientificValue] to convert to
 * @param target the [TargetUnit] to convert to
 * @param factory method for creating the [TargetValue] given the [Decimal] value in [TargetUnit]
 * @return an instance of [TargetValue] with its value equal to the value of this [ScientificUnit] in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(convertValue(target), target)

/**
 * Converts a [ScientificValue] into a [DefaultScientificValue] with an [AbstractScientificUnit] of the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of the current [ScientificValue] as well as the [DefaultScientificValue] to be created
 * @param Unit the type of [AbstractScientificUnit] of the current [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] of the [DefaultScientificValue]
 * @param target the [TargetUnit] to convert to
 * @return an instance of [DefaultScientificValue] with its value equal to the value of this [ScientificUnit] in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) = convert(target, ::DefaultScientificValue)

/**
 * Converts [ScientificValue.value] into the equivalent [Decimal] in [TargetUnit]
 * @param Quantity the type of [PhysicalQuantity] of the [Unit] and [TargetUnit]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] to convert to
 * @param target the [TargetUnit] to convert to
 * @return the [Decimal] value in [TargetUnit] equivalent to [ScientificValue.value]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convertValue(
    target: TargetUnit,
): Decimal = unit.convert(decimalValue, target)

/**
 * Converts a [ScientificValue] into another [ScientificValue] with a [ScientificUnit] of the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of the current [ScientificValue] as well as the [TargetValue]
 * @param Unit the type of [ScientificUnit] of the current [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] of the [TargetValue]
 * @param TargetValue the type of [ScientificValue] to convert to
 * @param target the [TargetUnit] to convert to
 * @param round The number of digits a rounded value should have after its decimal point
 * @param roundingMode The [RoundingMode] to apply when scaling
 * @param factory method for creating the [TargetValue] given the [Decimal] value in [TargetUnit]
 * @return an instance of [TargetValue] with its value equal to the value of this [ScientificUnit] in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(convertValue(target, round, roundingMode), target)

/**
 * Converts a [ScientificValue] into a [DefaultScientificValue] with an [AbstractScientificUnit] of the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of the current [ScientificValue] as well as the [DefaultScientificValue] to be created
 * @param Unit the type of [ScientificUnit] of the current [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] of the [DefaultScientificValue]
 * @param target the [TargetUnit] to convert to
 * @param round The number of digits a rounded value should have after its decimal point
 * @param roundingMode The [RoundingMode] to apply when scaling
 * @return an instance of [DefaultScientificValue] with its value equal to the value of this [ScientificUnit] in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) = convert(target, round, roundingMode, ::DefaultScientificValue)

/**
 * Converts [ScientificValue.value] into the equivalent [Decimal] in [TargetUnit]
 * @param Quantity the type of [PhysicalQuantity] of the [Unit] and [TargetUnit]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] to convert to
 * @param target the [TargetUnit] to convert to
 * @param round The number of digits a rounded value should have after its decimal point
 * @param roundingMode The [RoundingMode] to apply when scaling
 * @return the [Decimal] value in [TargetUnit] equivalent to [ScientificValue.value]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convertValue(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
): Decimal = unit.convert(decimalValue, target, round, roundingMode)

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : DefinedScientificUnit<Quantity>,
    UndefinedUnit : WrappedUndefinedExtendedUnit<Quantity, Unit>,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    asUndefined: ScientificValue<Quantity, Unit>.() -> UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, UndefinedUnit>,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(asUndefined().convertValue(target), target)

@JvmName("convertDefinedMetricAndImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric,
                     Unit : MeasurementUsage.UsedInUKImperial,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedMetricValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInUKImperial,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedMetricAndUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric,
                     Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedMetricAndUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedMetricAndImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        Unit : MeasurementUsage.UsedInUKImperial,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedMetricValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInUKImperial,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedUKImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedUSCustomaryValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedMetricAndUKImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        Unit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedMetricAndUSCustomaryValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, ::DefaultScientificValue)

@JvmName("convertDefinedMetricAndImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric,
                     Unit : MeasurementUsage.UsedInUKImperial,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInUKImperial,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInUKImperial =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndUKImperialValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric,
                     Unit : MeasurementUsage.UsedInUKImperial =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndUSCustomaryValueToExtended")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     Unit : DefinedScientificUnit<Quantity>,
                     Unit : MeasurementUsage.UsedInMetric,
                     Unit : MeasurementUsage.UsedInUSCustomary =
    asUndefined().convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        Unit : MeasurementUsage.UsedInUKImperial,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertDefinedMetricValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertDefinedImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInUKImperial,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertDefinedUKImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertDefinedUSCustomaryValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertDefinedMetricAndUKImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        Unit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertDefinedMetricAndUSCustomaryValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        Unit : DefinedScientificUnit<Quantity>,
        Unit : MeasurementUsage.UsedInMetric,
        Unit : MeasurementUsage.UsedInUSCustomary,
        TargetUnit : AbstractUndefinedScientificUnit<UndefinedQuantityType.Extended<Quantity>>,
        TargetUnit : UndefinedExtendedUnit<Quantity> =
    asUndefined().convert(target, round, roundingMode, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    UndefinedUnit : WrappedUndefinedExtendedUnit<Quantity, TargetUnit>,
    TargetUnit : DefinedScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    asUndefined: TargetUnit.() -> UndefinedUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(
    convertValue(target.asUndefined()),
    target,
)

@JvmName("convertMetricAndImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric,
                     TargetUnit : MeasurementUsage.UsedInUKImperial,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertMetricExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric =
    convert(target, { asUndefined() }, factory)

@JvmName("convertImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInUKImperial,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertUKImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, { asUndefined() }, factory)

@JvmName("convertUSCustomaryExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertMetricAndUKImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric,
                     TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, { asUndefined() }, factory)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertMetricAndImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric,
        TargetUnit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultScientificValue)

@JvmName("convertMetricExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric = convert(target, ::DefaultScientificValue)

@JvmName("convertImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultScientificValue)

@JvmName("convertUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric,
        TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    UndefinedUnit : WrappedUndefinedExtendedUnit<Quantity, TargetUnit>,
    TargetUnit : DefinedScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    asUndefined: TargetUnit.() -> UndefinedUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(
    convertValue(target.asUndefined(), round, roundingMode),
    target,
)

@JvmName("convertMetricAndImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric,
                     TargetUnit : MeasurementUsage.UsedInUKImperial,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInUKImperial,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertUKImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertUSCustomaryExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricAndUKImperialExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric,
                     TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
                     TargetUnit : DefinedScientificUnit<Quantity>,
                     TargetUnit : MeasurementUsage.UsedInMetric,
                     TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricAndImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric,
        TargetUnit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertMetricExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric = convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInUKImperial,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertMetricAndUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric,
        TargetUnit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, ::DefaultScientificValue)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
        TargetUnit : DefinedScientificUnit<Quantity>,
        TargetUnit : MeasurementUsage.UsedInMetric,
        TargetUnit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultScientificValue)
