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

interface ScientificArray<NumberType : Number, Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : com.splendo.kaluga.base.utils.Serializable {
    operator fun get(index: Int): NumberType
    val size: Int
    val unit: Unit

    operator fun iterator(): Iterator<NumberType>

    val decimalValues: List<Decimal> get() {
        val values = mutableListOf<Decimal>()
        iterator().forEach {
            values.add(it.toDecimal())
        }
        return values
    }
}

interface ByteScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Byte, Quantity, Unit> {
    val values: ByteArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Byte = values[index]
    override fun iterator(): Iterator<Byte> = values.iterator()
}

interface DoubleScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Double, Quantity, Unit> {
    val values: DoubleArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Double = values[index]
    override fun iterator(): Iterator<Double> = values.iterator()
}

interface FloatScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Float, Quantity, Unit> {
    val values: FloatArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Float = values[index]
    override fun iterator(): Iterator<Float> = values.iterator()
}

interface IntScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Int, Quantity, Unit> {
    val values: IntArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Int = values[index]
    override fun iterator(): Iterator<Int> = values.iterator()
}

interface ShortScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Short, Quantity, Unit> {
    val values: ShortArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Short = values[index]
    override fun iterator(): Iterator<Short> = values.iterator()
}

interface LongScientificArray<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : ScientificArray<Long, Quantity, Unit> {
    val values: LongArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Long = values[index]
    override fun iterator(): Iterator<Long> = values.iterator()
}

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
@JvmName("scientificArrayFromListOfNumberDefault")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > List<Number>.invoke(unit: Unit) = this.toDecimalList()(unit)

@JvmName("scientificArrayFromListOfNumber")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    Array : ScientificArray<NumberType, Quantity, Unit>
    > List<Number>.invoke(unit: Unit, factory: (List<Decimal>, Unit) -> Array) = this.toDecimalList()(unit, factory)

@JvmName("scientificArrayFromListOfDecimalDefault")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > List<Decimal>.invoke(unit: Unit) = this(unit, ::DefaultScientificArray)

@JvmName("scientificArrayFromListOfDecimal")
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    Array : ScientificArray<NumberType, Quantity, Unit>
    > List<Decimal>.invoke(unit: Unit, factory: (List<Decimal>, Unit) -> Array) = factory(this, unit)

// Group and Split

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > List<ScientificValue<Quantity, Unit>>.toScientificArray(
    unit: TargetUnit,
) = toScientificArray(unit, ::DefaultScientificArray)

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

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number
    > ScientificArray<NumberType, Quantity, Unit>.split() = split(unit, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.split(
    targetUnit: TargetUnit
) = split(targetUnit, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    NumberType : Number,
    TargetValue : ScientificValue<Quantity, Unit>
    > ScientificArray<NumberType, Quantity, Unit>.split(
    factory: (Decimal, Unit) -> TargetValue
) = split(unit, factory)

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

fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.convert(
    target: TargetUnit
) = convert(target, ::DefaultScientificArray)

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

fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>
    > ScientificArray<NumberType, Quantity, Unit>.map(
    transform: ScientificValue<Quantity, Unit>.() -> ScientificValue<TargetQuantity, TargetUnit>
) = map(1(unit).transform().unit, ::DefaultScientificArray, transform)

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

fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>
    > ScientificArray<NumberType, Quantity, Unit>.map(
    unit: TargetUnit,
    transform: ScientificValue<Quantity, Unit>.() -> ScientificValue<TargetQuantity, TargetUnit>
) = map(unit, ::DefaultScientificArray, transform)

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
    unit: TargetUnit,
    transform: ScientificValue<LeftQuantity, LeftUnit>.(ScientificValue<RightQuantity, RightUnit>) -> ScientificValue<TargetQuantity, TargetUnit>
) = combine(right, unit, ::DefaultScientificArray, transform)

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

infix operator fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.plus(right: ScientificArray<NumberType, Quantity, RightUnit>) = concat(right)

infix operator fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificArray<NumberType, Quantity, Unit>.plus(right: ScientificValue<Quantity, RightUnit>) = concat(listOf(right).toScientificArray(right.unit))

infix operator fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.plus(right: ScientificArray<NumberType, Quantity, RightUnit>) = listOf(this).toScientificArray(unit).concat(right)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    > ScientificArray<*, Quantity, Unit>.concat(
    right: ScientificArray<*, Quantity, RightUnit>
) = concat(right, unit)

fun <
    Quantity : PhysicalQuantity,
    NumberType : Number,
    Unit : ScientificUnit<Quantity>,
    Array : ScientificArray<NumberType, Quantity, Unit>
    > ScientificArray<*, Quantity, Unit>.concat(
    right: ScientificArray<*, Quantity, Unit>,
    factory: (List<Decimal>, Unit) -> Array
) = concat(right, unit, factory)

fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificArray<*, Quantity, LeftUnit>.concat(
    right: ScientificArray<*, Quantity, RightUnit>,
    targetUnit: TargetUnit
) = concat(right, targetUnit, ::DefaultScientificArray)

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
