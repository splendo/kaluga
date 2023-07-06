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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.RoundingMode
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementUsage
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedExtendedUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit
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
    > ScientificValue<Quantity, Unit>.convert(target: TargetUnit) = convert(target, ::DefaultScientificValue)

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
    > ScientificValue<Quantity, Unit>.convertValue(target: TargetUnit): Decimal {
    return unit.convert(decimalValue, target)
}

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
): Decimal {
    return unit.convert(decimalValue, target, round, roundingMode)
}

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

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : ScientificUnit<Quantity>,
    UndefinedUnit : WrappedUndefinedExtendedUnit<Quantity, Unit>,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    TargetValue : UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, TargetUnit>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    asUndefined: ScientificValue<Quantity, Unit>.() -> UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, UndefinedUnit>,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue =
    factory(asUndefined().convertValue(target), target)

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
      Unit : ScientificUnit<Quantity>,
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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric = convert(target, { asUndefined() }, factory)

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
      Unit : ScientificUnit<Quantity>,
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
      Unit : ScientificUnit<Quantity>,
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
      Unit : ScientificUnit<Quantity>,
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
      Unit : ScientificUnit<Quantity>,
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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, { asUndefined() }, factory)

@JvmName("convertDefinedMetricAndImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric =
    convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUKImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUSCustomaryValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUKImperialValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUSCustomaryValueToDefaultUndefinedValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
) where
      Unit : AbstractScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, ::DefaultUndefinedScientificValue)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, factory)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric =
    convert(target, round, roundingMode, factory)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, factory)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, factory)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, factory)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, factory)

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
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, factory)

@JvmName("convertDefinedMetricAndImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric =
    convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUKImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedUSCustomaryValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInUSCustomary =
    convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUKImperialValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : ScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUKImperial =
    convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

@JvmName("convertDefinedMetricAndUSCustomaryValueToDefaultUndefined")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit,
    TargetUnit : UndefinedExtendedUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) where
      Unit : AbstractScientificUnit<Quantity>,
      Unit : MeasurementUsage.UsedInMetric,
      Unit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, ::DefaultUndefinedScientificValue)

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    UndefinedUnit : WrappedUndefinedExtendedUnit<Quantity, TargetUnit>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    asUndefined: TargetUnit.() -> UndefinedUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue = factory(
    convertValue(target.asUndefined()),
    target,
)

@JvmName("convertMetricAndImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, { asUndefined() }, factory)

@JvmName("convertMetricExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric = convert(target, { asUndefined() }, factory)

@JvmName("convertImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, { asUndefined() }, factory)

@JvmName("convertUKImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, { asUndefined() }, factory)

@JvmName("convertUSCustomaryExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, { asUndefined() }, factory)

@JvmName("convertMetricAndUKImperialExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, { asUndefined() }, factory)

@JvmName("convertMetricAndUSCustomaryExtendedValueToScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    TargetValue : ScientificValue<Quantity, TargetUnit>,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue,
): TargetValue where
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, { asUndefined() }, factory)

@JvmName("convertMetricAndImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric = convert(target, ::DefaultScientificValue)

@JvmName("convertImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, ::DefaultScientificValue)

@JvmName("convertUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndUKImperialExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, ::DefaultScientificValue)

@JvmName("convertMetricAndUSCustomaryExtendedValueToDefaultScientificValue")
fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    TargetUnit,
    > UndefinedScientificValue<UndefinedQuantityType.Extended<Quantity>, Unit>.convert(
    target: TargetUnit,
) where
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity.DefinedPhysicalQuantityWithDimension,
    Unit : UndefinedExtendedUnit<Quantity>,
    UndefinedUnit : WrappedUndefinedExtendedUnit<Quantity, TargetUnit>,
    TargetUnit : ScientificUnit<Quantity>,
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

@JvmName("convertMetricAndImperialExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric = convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertImperialExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertUKImperialExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertUSCustomaryExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricAndUKImperialExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, round, roundingMode, { asUndefined() }, factory)

@JvmName("convertMetricAndUSCustomaryExtendedValueToScientificValue")
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
      TargetUnit : ScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, { asUndefined() }, factory)

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
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, ::DefaultScientificValue)

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
      TargetUnit : AbstractScientificUnit<Quantity>,
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
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, ::DefaultScientificValue)

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
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, round, roundingMode, ::DefaultScientificValue)

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
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, ::DefaultScientificValue)

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
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUKImperial = convert(target, round, roundingMode, ::DefaultScientificValue)

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
      TargetUnit : AbstractScientificUnit<Quantity>,
      TargetUnit : MeasurementUsage.UsedInMetric,
      TargetUnit : MeasurementUsage.UsedInUSCustomary = convert(target, round, roundingMode, ::DefaultScientificValue)
