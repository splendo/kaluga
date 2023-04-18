/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDecimalList
import com.splendo.kaluga.base.utils.toDoubleArray
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.convert
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

/**
 * A collection of values in a given [ScientificUnit]
 * @param NumberType the type of [Number] this value is stored in
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface ScientificArray<NumberType : Number, Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : com.splendo.kaluga.base.utils.Serializable {

    /**
     * Gets the [NumberType] at the given index
     * @param index the index of the [NumberType] to get from this collection
     * @return the [NumberType] at the given index
     * @throws [IndexOutOfBoundsException] if the index is out of bounds for this array
     */
    operator fun get(index: Int): NumberType

    /**
     * The size of the array
     */
    val size: Int

    /**
     * The [Unit] component
     */
    val unit: Unit

    /**
     * Creates an iterator over the elements of the array
     */
    operator fun iterator(): Iterator<NumberType>

    /**
     * The List of [Decimal] for all values in this array
     */
    val decimalValues: List<Decimal> get() {
        val values = mutableListOf<Decimal>()
        iterator().forEach {
            values.add(it.toDecimal())
        }
        return values
    }
}

/**
 * A [ScientificArray] containing [Byte] values
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface ByteScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Byte, Quantity, Unit> {

    /**
     * The [ByteArray] containing the values
     */
    val values: ByteArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Byte = values[index]
    override fun iterator(): Iterator<Byte> = values.iterator()
}

/**
 * A [ScientificArray] containing [Double] values
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface DoubleScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Double, Quantity, Unit> {

    /**
     * The [DoubleArray] containing the values
     */
    val values: DoubleArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Double = values[index]
    override fun iterator(): Iterator<Double> = values.iterator()
}

/**
 * A [ScientificArray] containing [Float] values
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface FloatScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Float, Quantity, Unit> {

    /**
     * The [FloatArray] containing the values
     */
    val values: FloatArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Float = values[index]
    override fun iterator(): Iterator<Float> = values.iterator()
}

/**
 * A [ScientificArray] containing [Int] values
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface IntScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Int, Quantity, Unit> {

    /**
     * The [IntArray] containing the values
     */
    val values: IntArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Int = values[index]
    override fun iterator(): Iterator<Int> = values.iterator()
}

/**
 * A [ScientificArray] containing [Short] values
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface ShortScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Short, Quantity, Unit> {

    /**
     * The [ShortArray] containing the values
     */
    val values: ShortArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Short = values[index]
    override fun iterator(): Iterator<Short> = values.iterator()
}

/**
 * A [ScientificArray] containing [Long] values
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 */
interface LongScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Long, Quantity, Unit> {

    /**
     * The [LongArray] containing the values
     */
    val values: LongArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Long = values[index]
    override fun iterator(): Iterator<Long> = values.iterator()
}

/**
 * A class implementation of [DoubleScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this array represents
 * @param values the [DoubleArray] containing the values
 * @param unit the [Unit] component
 */
@Serializable
data class DefaultScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>>(
    override val values: DoubleArray,
    override val unit: Unit
) : DoubleScientificArray<Quantity, Unit> {
    constructor(value: List<Decimal>, unit: Unit) : this(value.toDoubleArray(), unit)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultScientificArray<*, *>

        if (!values.contentEquals(other.values)) return false
        if (unit != other.unit) return false

        return true
    }

    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + unit.hashCode()
        return result
    }
}

// Creation

/**
 * Creates a [DefaultScientificArray] containing this list using a given [unit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] the array should represent
 * @param unit the [Unit] of the [DefaultScientificArray] to be created
 * @return the created [DefaultScientificArray]
 */
@JvmName("scientificArrayFromListOfNumberDefault")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > List<Number>.invoke(unit: Unit) = this.toDecimalList()(unit)

/**
 * Creates an [Array] containing this list using a given [unit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] the array should represent
 * @param NumberType the type of [Number] the values are is stored in
 * @param Array the type of [ScientificArray] to create
 * @param unit the [Unit] of the [Array] to be created
 * @param factory method for creating [Array] using a list of [Decimal] and a [Unit]
 * @return the created [Array]
 */
@JvmName("scientificArrayFromListOfNumber")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    Array : ScientificArray<NumberType, Quantity, Unit>
    > List<Number>.invoke(unit: Unit, factory: (List<Decimal>, Unit) -> Array) = this.toDecimalList()(unit, factory)

/**
 * Creates a [DefaultScientificArray] containing this list of [Decimal] using a given [unit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] the array should represent
 * @param unit the [Unit] of the [DefaultScientificArray] to be created
 * @return the created [DefaultScientificArray]
 */
@JvmName("scientificArrayFromListOfDecimalDefault")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > List<Decimal>.invoke(unit: Unit) = this(unit, ::DefaultScientificArray)

/**
 * Creates an [Array] containing this list of [Decimal] using a given [unit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] the array should represent
 * @param NumberType the type of [Number] the values are is stored in
 * @param Array the type of [ScientificArray] to create
 * @param unit the [Unit] of the [Array] to be created
 * @param factory method for creating [Array] using a list of [Decimal] and a [Unit]
 * @return the created [Array]
 */
@JvmName("scientificArrayFromListOfDecimal")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    Array : ScientificArray<NumberType, Quantity, Unit>
    > List<Decimal>.invoke(unit: Unit, factory: (List<Decimal>, Unit) -> Array) = factory(this, unit)

// Group and Split

/**
 * Creates a [DefaultScientificArray] from a list of [ScientificValue]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] representing all elements in the list of [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] the array should represent
 * @param unit the [TargetUnit] to convert all values to
 * @return A [DefaultScientificArray] containing all values of the list in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > List<ScientificValue<Quantity, Unit>>.toScientificArray(
    unit: TargetUnit
) = toScientificArray(unit, ::DefaultScientificArray)

/**
 * Creates an [ArrayType] from a list of [ScientificValue]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] representing all elements in the list of [ScientificValue]
 * @param TargetUnit the type of [ScientificUnit] the array should represent
 * @param NumberType the type of [Number] the values are is stored in
 * @param ArrayType the type of [ScientificArray] to create
 * @param unit the [TargetUnit] to convert all values to
 * @param factory method for creating an [ArrayType] from a list of [Decimal] and a [TargetUnit]
 * @return A [ArrayType] containing all values of the list in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    NumberType : Number,
    ArrayType : ScientificArray<NumberType, Quantity, TargetUnit>
    > List<ScientificValue<Quantity, Unit>>.toScientificArray(
    unit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> ArrayType
) = factory(map { it.unit.convert(it.decimalValue, unit) }, unit)

/**
 * Splits a [ScientificArray] into a list of [DefaultScientificValue] of all the values in the array
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] of the array
 * @param NumberType the type of [Number] stored in the array
 * @return a list of [DefaultScientificValue] in [Unit] for all values stored in the array
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number
    > ScientificArray<NumberType, Quantity, Unit>.split() = split(unit, ::DefaultScientificValue)

/**
 * Splits a [ScientificArray] into a list of [DefaultScientificValue] in [TargetUnit] of all the values in the array
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] of the array
 * @param NumberType the type of [Number] stored in the array
 * @param TargetUnit the type of [ScientificUnit] the resulting [DefaultScientificValue] should be in
 * @param targetUnit the [TargetUnit] the resulting [DefaultScientificValue] should be in
 * @return a list of [DefaultScientificValue] in [TargetUnit] for all values stored in the array
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.split(
    targetUnit: TargetUnit
) = split(targetUnit, ::DefaultScientificValue)

/**
 * Splits a [ScientificArray] into a list of [TargetValue] of all the values in the array
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] of the array
 * @param NumberType the type of [Number] stored in the array
 * @param TargetValue the type of [ScientificValue] to return
 * @param factory method for creating [TargetValue] from a [Decimal] and [Unit]
 * @return a list of [TargetValue] for all values stored in the array
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    TargetValue : ScientificValue<Quantity, Unit>
    > ScientificArray<NumberType, Quantity, Unit>.split(
    factory: (Decimal, Unit) -> TargetValue
) = split(unit, factory)

/**
 * Splits a [ScientificArray] into a list of [TargetValue] of all the values in the array
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] of the array
 * @param NumberType the type of [Number] stored in the array
 * @param TargetUnit the type of [ScientificUnit] the resulting [TargetValue] should be in
 * @param TargetValue the type of [ScientificValue] to return
 * @param targetUnit the [TargetUnit] the resulting [TargetValue] should be in
 * @param factory method for creating [TargetValue] from a [Decimal] and [TargetUnit]
 * @return a list of [TargetValue] for all values stored in the array
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    TargetUnit : ScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>
    > ScientificArray<NumberType, Quantity, Unit>.split(
    targetUnit: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue
): List<TargetValue> {
    val target = mutableListOf<TargetValue>()
    iterator().forEach {
        target.add(unit.convert(it, targetUnit)(targetUnit, factory))
    }
    return target
}

// Conversion

/**
 * Creates a [DefaultScientificArray] that contains all values of a [ScientificArray] converted to [TargetUnit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetUnit the type of [ScientificUnit] the resulting [DefaultScientificArray] should be in
 * @param target the [TargetUnit] the resulting [DefaultScientificArray] should be in
 * @return a [DefaultScientificArray] containing all values of the [ScientificArray] converted to [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.convert(
    target: TargetUnit
) = convert(target, ::DefaultScientificArray)

/**
 * Creates a [TargetArray] that contains all values of a [ScientificArray] converted to [TargetUnit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetUnit the type of [ScientificUnit] the resulting [TargetArray] should be in
 * @param TargetNumberType the type of [Number] stored in [TargetArray]
 * @param TargetArray the type of [ScientificArray] to create
 * @param target the [TargetUnit] the resulting [TargetArray] should be in
 * @param factory method for creating [TargetArray] from a list of [Decimal] and [TargetUnit]
 * @return a [TargetArray] containing all values of the [ScientificArray] converted to [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetNumberType : Number,
    TargetArray : ScientificArray<TargetNumberType, Quantity, TargetUnit>
    > ScientificArray<NumberType, Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> TargetArray
) = map(target, factory) {
    convert(target)
}

/**
 * Converts the values of a [ScientificArray] into their [Decimal] values in a [TargetUnit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetUnit the type of [ScientificUnit] the resulting list of [Decimal] should be in
 * @param target the [TargetUnit] the resulting list of [Decimal] should be in
 * @return the list of [Decimal] of all values of the [ScientificArray] in [TargetUnit]
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.convertValues(
    target: TargetUnit
): List<Decimal> {
    val values = mutableListOf<Decimal>()
    iterator().forEach {
        values.add(unit.convert(it.toDecimal(), target))
    }
    return values
}

/**
 * Maps all the values of a [ScientificArray] into a [DefaultScientificArray] using a transformation method
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param transform method for transforming a [ScientificValue] of [Unit] into a [ScientificValue] of [TargetUnit]
 * @return a [DefaultScientificArray] containing all values transformed using the transform method
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>
    > ScientificArray<NumberType, Quantity, Unit>.map(
    transform: ScientificValue<Quantity, Unit>.() -> ScientificValue<TargetQuantity, TargetUnit>
) = map(1(unit).transform().unit, ::DefaultScientificArray, transform)

/**
 * Maps all the values of a [ScientificArray] into a [TargetArray] using a transformation method
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param TargetNumberType the type of [Number] stored in [TargetArray]
 * @param TargetArray the type of [ScientificArray] to create
 * @param factory method for creating a [TargetArray] from a list of [Decimal] and a [TargetUnit]
 * @param transform method for transforming a [ScientificValue] of [Unit] into a [ScientificValue] of [TargetUnit]
 * @return a [TargetArray] containing all values transformed using the transform method
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    TargetNumberType : Number,
    TargetArray : ScientificArray<TargetNumberType, TargetQuantity, TargetUnit>
    > ScientificArray<NumberType, Quantity, Unit>.map(
    factory: (List<Decimal>, TargetUnit) -> TargetArray,
    transform: ScientificValue<Quantity, Unit>.() -> ScientificValue<TargetQuantity, TargetUnit>
) = map(1(unit).transform().unit, factory, transform)

/**
 * Maps all the values of a [ScientificArray] into a [DefaultScientificArray] using a transformation method
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param targetUnit the [TargetUnit] to transform all values into
 * @param transform method for transforming a [ScientificValue] of [Unit] into a [ScientificValue] of [TargetUnit]
 * @return a [DefaultScientificArray] containing all values transformed using the transform method
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>
    > ScientificArray<NumberType, Quantity, Unit>.map(
    targetUnit: TargetUnit,
    transform: ScientificValue<Quantity, Unit>.() -> ScientificValue<TargetQuantity, TargetUnit>
) = map(targetUnit, ::DefaultScientificArray, transform)

/**
 * Maps all the values of a [ScientificArray] into a [TargetArray] using a transformation method
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param TargetNumberType the type of [Number] stored in [TargetArray]
 * @param TargetArray the type of [ScientificArray] to create
 * @param targetUnit the [TargetUnit] to transform all values into
 * @param factory method for creating a [TargetArray] from a list of [Decimal] and a [TargetUnit]
 * @param transform method for transforming a [ScientificValue] of [Unit] into a [ScientificValue] of [TargetUnit]
 * @return a [TargetArray] containing all values transformed using the transform method
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    TargetNumberType : Number,
    TargetArray : ScientificArray<TargetNumberType, TargetQuantity, TargetUnit>
    > ScientificArray<NumberType, Quantity, Unit>.map(
    targetUnit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> TargetArray,
    transform: ScientificValue<Quantity, Unit>.() -> ScientificValue<TargetQuantity, TargetUnit>
): TargetArray {
    val newValues = mutableListOf<Decimal>()
    iterator().forEach {
        newValues.add(it(unit).transform().decimalValue)
    }
    return factory(newValues, targetUnit)
}

/**
 * Combines two [ScientificArray] into a [DefaultScientificArray] using a transformation method
 * @param LeftQuantity the type of [PhysicalQuantity] of the unit
 * @param LeftNumberType the type of [Number] stored in the array
 * @param LeftUnit the type of [ScientificUnit] of the array
 * @param RightQuantity the type of [PhysicalQuantity] of the [right] unit
 * @param RightNumberType the type of [Number] stored in the [right] array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param right a [ScientificArray] of [RightUnit] to combine with
 * @param transform method for converting a [ScientificValue] in [LeftUnit] and [ScientificValue] in [RightUnit] into A [ScientificValue] of [TargetUnit]
 * @return A [DefaultScientificArray] that combines the values of both arrays using the [transform] method
 * @throws [IndexOutOfBoundsException] if the size of both arrays does not match
 */
fun <
    LeftQuantity : PhysicalQuantity,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightQuantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>
    > ScientificArray<LeftNumberType, LeftQuantity, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightQuantity, RightUnit>,
    transform: ScientificValue<LeftQuantity, LeftUnit>.(ScientificValue<RightQuantity, RightUnit>) -> ScientificValue<TargetQuantity, TargetUnit>
) = combine(right, 1(unit).transform(1(right.unit)).unit, ::DefaultScientificArray, transform)

/**
 * Combines two [ScientificArray] into a [TargetArray] using a transformation method
 * @param LeftQuantity the type of [PhysicalQuantity] of the unit
 * @param LeftNumberType the type of [Number] stored in the array
 * @param LeftUnit the type of [ScientificUnit] of the array
 * @param RightQuantity the type of [PhysicalQuantity] of the [right] unit
 * @param RightNumberType the type of [Number] stored in the [right] array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param TargetNumberType the type of [Number] stored in [TargetArray]
 * @param TargetArray the type of [ScientificArray] to create
 * @param right a [ScientificArray] of [RightUnit] to combine with
 * @param transform method for converting a [ScientificValue] in [LeftUnit] and [ScientificValue] in [RightUnit] into A [ScientificValue] of [TargetUnit]
 * @return A [TargetArray] that combines the values of both arrays using the [transform] method
 * @throws [IndexOutOfBoundsException] if the size of both arrays does not match
 */
fun <
    LeftQuantity : PhysicalQuantity,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightQuantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    TargetNumberType : Number,
    TargetArray : ScientificArray<TargetNumberType, TargetQuantity, TargetUnit>
    > ScientificArray<LeftNumberType, LeftQuantity, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightQuantity, RightUnit>,
    factory: (List<Decimal>, TargetUnit) -> TargetArray,
    transform: ScientificValue<LeftQuantity, LeftUnit>.(ScientificValue<RightQuantity, RightUnit>) -> ScientificValue<TargetQuantity, TargetUnit>
) = combine(right, 1(unit).transform(1(right.unit)).unit, factory, transform)

/**
 * Combines two [ScientificArray] into a [DefaultScientificArray] using a transformation method
 * @param LeftQuantity the type of [PhysicalQuantity] of the unit
 * @param LeftNumberType the type of [Number] stored in the array
 * @param LeftUnit the type of [ScientificUnit] of the array
 * @param RightQuantity the type of [PhysicalQuantity] of the [right] unit
 * @param RightNumberType the type of [Number] stored in the [right] array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param right a [ScientificArray] of [RightUnit] to combine with
 * @param targetUnit the [TargetUnit] to convert to
 * @param transform method for converting a [ScientificValue] in [LeftUnit] and [ScientificValue] in [RightUnit] into A [ScientificValue] of [TargetUnit]
 * @return A [DefaultScientificArray] that combines the values of both arrays using the [transform] method
 * @throws [IndexOutOfBoundsException] if the size of both arrays does not match
 */
fun <
    LeftQuantity : PhysicalQuantity,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightQuantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>
    > ScientificArray<LeftNumberType, LeftQuantity, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightQuantity, RightUnit>,
    targetUnit: TargetUnit,
    transform: ScientificValue<LeftQuantity, LeftUnit>.(ScientificValue<RightQuantity, RightUnit>) -> ScientificValue<TargetQuantity, TargetUnit>
) = combine(right, targetUnit, ::DefaultScientificArray, transform)

/**
 * Combines two [ScientificArray] into a [TargetArray] using a transformation method
 * @param LeftQuantity the type of [PhysicalQuantity] of the unit
 * @param LeftNumberType the type of [Number] stored in the array
 * @param LeftUnit the type of [ScientificUnit] of the array
 * @param RightQuantity the type of [PhysicalQuantity] of the [right] unit
 * @param RightNumberType the type of [Number] stored in the [right] array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param TargetQuantity the type of [PhysicalQuantity] to convert the values into
 * @param TargetUnit the type of [ScientificUnit] to convert the values into
 * @param TargetNumberType the type of [Number] stored in [TargetArray]
 * @param TargetArray the type of [ScientificArray] to create
 * @param right a [ScientificArray] of [RightUnit] to combine with
 * @param targetUnit the [TargetUnit] to convert to
 * @param transform method for converting a [ScientificValue] in [LeftUnit] and [ScientificValue] in [RightUnit] into A [ScientificValue] of [TargetUnit]
 * @return A [TargetArray] that combines the values of both arrays using the [transform] method
 * @throws [IndexOutOfBoundsException] if the size of both arrays does not match
 */
fun <
    LeftQuantity : PhysicalQuantity,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightQuantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    TargetNumberType : Number,
    TargetArray : ScientificArray<TargetNumberType, TargetQuantity, TargetUnit>
    > ScientificArray<LeftNumberType, LeftQuantity, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightQuantity, RightUnit>,
    targetUnit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> TargetArray,
    transform: ScientificValue<LeftQuantity, LeftUnit>.(ScientificValue<RightQuantity, RightUnit>) -> ScientificValue<TargetQuantity, TargetUnit>
): TargetArray {
    if (size != right.size) {
        throw IndexOutOfBoundsException("Cannot combine Scientific Array of unequal size. Left: $size Right: ${right.size}")
    }
    val newValues = mutableListOf<Decimal>()
    iterator().withIndex().forEach {
        newValues.add(it.value(unit).transform(right[it.index](right.unit)).decimalValue)
    }
    return factory(newValues, targetUnit)
}

/**
 * Creates a [DefaultScientificArray] in [Unit] containing all values of two [ScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param right the [ScientificArray] to add to this [ScientificArray]
 * @return the [DefaultScientificArray] containing all values of both [ScientificArray]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.plus(right: ScientificArray<NumberType, Quantity, RightUnit>) = concat(right)

/**
 * Creates a [DefaultScientificArray] in [Unit] by adding a [ScientificValue] to the [ScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param right the [ScientificValue] to add to this [ScientificArray]
 * @return the [DefaultScientificArray] containing all values of this [ScientificArray] as well as [right] in [Unit]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.plus(right: ScientificValue<Quantity, RightUnit>) = concat(listOf(right).toScientificArray(right.unit))

/**
 * Creates a [DefaultScientificArray] in [Unit] by adding a [ScientificArray] to the [ScientificValue]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in [right]
 * @param Unit the type of [ScientificUnit] of the array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param right the [ScientificArray] to add to this [ScientificValue]
 * @return the [DefaultScientificArray] containing this [ScientificValue] and all values of this [right] in [Unit]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.plus(right: ScientificArray<NumberType, Quantity, RightUnit>) = listOf(this).toScientificArray(unit).concat(right)

/**
 * Creates a [DefaultScientificArray] in [Unit] containing all values of two [ScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] of the array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param right the [ScientificArray] to add to this [ScientificArray]
 * @return the [DefaultScientificArray] containing all values of both [ScientificArray]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificArray<*, Quantity, Unit>.concat(
    right: ScientificArray<*, Quantity, RightUnit>
) = concat(right, unit)

/**
 * Creates an [Array] containing all values of two [ScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param NumberType the type of [Number] stored in the array
 * @param Unit the type of [ScientificUnit] of the array
 * @param Array the type of [ScientificArray] to return
 * @param right the [ScientificArray] to add to this [ScientificArray]
 * @param factory method for creating [Array] from a list of [Decimal] and [Unit]
 * @return the [Array] containing all values of both [ScientificArray]
 */
fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    Array : ScientificArray<NumberType, Quantity, Unit>
    > ScientificArray<*, Quantity, Unit>.concat(
    right: ScientificArray<*, Quantity, Unit>,
    factory: (List<Decimal>, Unit) -> Array
) = concat(right, unit, factory)

/**
 * Creates a [DefaultScientificArray] in [TargetUnit] containing all values of two [ScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param LeftUnit the type of [ScientificUnit] of the array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param TargetUnit the type of [ScientificUnit] of the [DefaultScientificArray] to be returned
 * @param right the [ScientificArray] to add to this [ScientificArray]
 * @param targetUnit the [TargetUnit] of the [DefaultScientificArray] to be returned
 * @return the [DefaultScientificArray] in [TargetUnit] containing all values of both [ScientificArray]
 */
fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<*, Quantity, LeftUnit>.concat(
    right: ScientificArray<*, Quantity, RightUnit>,
    targetUnit: TargetUnit
) = concat(right, targetUnit, ::DefaultScientificArray)

/**
 * Creates a [TargetArray] containing all values of two [ScientificArray]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param LeftUnit the type of [ScientificUnit] of the array
 * @param RightUnit the type of [ScientificUnit] of [right]
 * @param TargetUnit the type of [ScientificUnit] of the [TargetArray] to be returned
 * @param TargetNumberType the type of [Number] stored in [TargetArray]
 * @param TargetArray the type of [ScientificArray] to create
 * @param right the [ScientificArray] to add to this [ScientificArray]
 * @param targetUnit the [TargetUnit] of the [TargetArray] to be returned
 * @param factory method for creating [TargetArray] from a list of [Decimal] and [TargetUnit]
 * @return the [TargetArray] containing all values of both [ScientificArray]
 */
fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetNumberType : Number,
    TargetArray : ScientificArray<TargetNumberType, Quantity, TargetUnit>
    > ScientificArray<*, Quantity, LeftUnit>.concat(
    right: ScientificArray<*, Quantity, RightUnit>,
    targetUnit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> TargetArray
) = factory((convertValues(targetUnit) + right.convertValues(targetUnit)), targetUnit)
