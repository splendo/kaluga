/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands
 
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

interface ScientificArray<ValueType : Number, Type : MeasurementType, Unit : ScientificUnit<Type>> {
    operator fun get(index: Int): ValueType
    val size: Int
    val unit: Unit

    operator fun iterator(): Iterator<ValueType>

    val decimalValues: List<Decimal> get() {
        val values = mutableListOf<Decimal>()
        iterator().forEach {
            values.add(it.toDecimal())
        }
        return values
    }
}

interface ByteScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>> : ScientificArray<Byte, Type, Unit> {
    val values: ByteArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Byte = values[index]
    override fun iterator(): Iterator<Byte> = values.iterator()
}

interface DoubleScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>> : ScientificArray<Double, Type, Unit> {
    val values: DoubleArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Double = values[index]
    override fun iterator(): Iterator<Double> = values.iterator()
}

interface FloatScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>> : ScientificArray<Float, Type, Unit> {
    val values: FloatArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Float = values[index]
    override fun iterator(): Iterator<Float> = values.iterator()
}

interface IntScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>> : ScientificArray<Int, Type, Unit> {
    val values: IntArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Int = values[index]
    override fun iterator(): Iterator<Int> = values.iterator()
}

interface ShortScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>> : ScientificArray<Short, Type, Unit> {
    val values: ShortArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Short = values[index]
    override fun iterator(): Iterator<Short> = values.iterator()
}

interface LongScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>> : ScientificArray<Long, Type, Unit> {
    val values: LongArray

    override val size: Int
        get() = values.size

    override fun get(index: Int): Long = values[index]
    override fun iterator(): Iterator<Long> = values.iterator()
}

@Serializable
data class DefaultScientificArray<Type : MeasurementType, Unit : ScientificUnit<Type>>(
    override val values: DoubleArray,
    override val unit: Unit
) : DoubleScientificArray<Type, Unit> {
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
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > List<Number>.invoke(unit: UnitType) = this.toDecimalList()(unit)

@JvmName("scientificArrayFromListOfNumber")
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    NumberType : Number,
    ValueType : ScientificArray<NumberType, Type, UnitType>
    > List<Number>.invoke(unit: UnitType, factory: (List<Decimal>, UnitType) -> ValueType) = this.toDecimalList()(unit, factory)

@JvmName("scientificArrayFromListOfDecimalDefault")
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > List<Decimal>.invoke(unit: UnitType) = this(unit, ::DefaultScientificArray)

@JvmName("scientificArrayFromListOfDecimal")
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    NumberType : Number,
    ValueType : ScientificArray<NumberType, Type, UnitType>
    > List<Decimal>.invoke(unit: UnitType, factory: (List<Decimal>, UnitType) -> ValueType) = factory(this, unit)

// Group and Split

fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    TargetUnitType : ScientificUnit<Type>
    > List<ScientificValue<Type, UnitType>>.toScientificArray(
    unit: TargetUnitType,
) = toScientificArray(unit, ::DefaultScientificArray)

fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    TargetUnitType : ScientificUnit<Type>,
    NumberType : Number,
    ArrayType : ScientificArray<NumberType, Type, TargetUnitType>
    > List<ScientificValue<Type, UnitType>>.toScientificArray(
    unit: TargetUnitType,
    factory: (List<Decimal>, TargetUnitType) -> ArrayType
) = factory(map { it.unit.convert(it.decimalValue, unit) }, unit)

fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    NumberType : Number
    >ScientificArray<NumberType, Type, UnitType>.split(
) = split(unit, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    NumberType : Number,
    TargetUnitType : ScientificUnit<Type>
    >ScientificArray<NumberType, Type, UnitType>.split(
    targetUnit : TargetUnitType
) = split(targetUnit, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    NumberType : Number,
    TargetUnitType : ScientificUnit<Type>,
    TargetValue : ScientificValue<Type, TargetUnitType>
    > ScientificArray<NumberType, Type, UnitType>.split(
    targetUnit : TargetUnitType,
    factory: (Decimal, TargetUnitType) -> TargetValue
): List<TargetValue> {
    val target = mutableListOf<TargetValue>()
    iterator().forEach {
        target.add(unit.convert(it, targetUnit)(targetUnit, factory))
    }
    return target
}

// Conversion

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificArray<NumberType, Type, Unit>.convert(target: TargetUnit) = convert(target, ::DefaultScientificArray)

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>,
    TargetNumberType : Number,
    TargetValue : ScientificArray<TargetNumberType, Type, TargetUnit>
    > ScientificArray<NumberType, Type, Unit>.convert(
    target: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> TargetValue
) : TargetValue = map(target, factory) {
    convert(target)
}

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificArray<NumberType, Type, Unit>.convertValues(target: TargetUnit) : List<Decimal> {
    val values = mutableListOf<Decimal>()
    iterator().forEach {
        values.add(unit.convert(it.toDecimal(), target))
    }
    return values
}

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>
    > ScientificArray<NumberType, Type, Unit>.map(
    transform: ScientificValue<Type, Unit>.() -> ScientificValue<TargetType, TargetUnit>
) = map(1(unit).transform().unit, ::DefaultScientificArray, transform)

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>,
    TargetNumberType : Number,
    ArrayType : ScientificArray<TargetNumberType, TargetType, TargetUnit>
    > ScientificArray<NumberType, Type, Unit>.map(
    factory: (List<Decimal>, TargetUnit) -> ArrayType,
    transform: ScientificValue<Type, Unit>.() -> ScientificValue<TargetType, TargetUnit>
) = map(1(unit).transform().unit, factory, transform)

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>
    > ScientificArray<NumberType, Type, Unit>.map(
    unit: TargetUnit,
    transform: ScientificValue<Type, Unit>.() -> ScientificValue<TargetType, TargetUnit>
) = map(unit, ::DefaultScientificArray, transform)

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>,
    TargetNumberType : Number,
    ArrayType : ScientificArray<TargetNumberType, TargetType, TargetUnit>
    > ScientificArray<NumberType, Type, Unit>.map(
    targetUnit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> ArrayType,
    transform: ScientificValue<Type, Unit>.() -> ScientificValue<TargetType, TargetUnit>
): ArrayType {
    val newValues = mutableListOf<Decimal>()
    iterator().forEach {
        newValues.add(it(unit).transform().decimalValue)
    }
    return factory(newValues, targetUnit)
}

fun <
    LeftType : MeasurementType,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightType>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>
    > ScientificArray<LeftNumberType, LeftType, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightType, RightUnit>,
    transform: ScientificValue<LeftType, LeftUnit>.(ScientificValue<RightType, RightUnit>) -> ScientificValue<TargetType, TargetUnit>
) = combine(right, 1(unit).transform(1(right.unit)).unit, ::DefaultScientificArray, transform)

fun <
    LeftType : MeasurementType,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightType>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>,
    TargetNumberType : Number,
    ArrayType : ScientificArray<TargetNumberType, TargetType, TargetUnit>
    > ScientificArray<LeftNumberType, LeftType, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightType, RightUnit>,
    factory: (List<Decimal>, TargetUnit) -> ArrayType,
    transform: ScientificValue<LeftType, LeftUnit>.(ScientificValue<RightType, RightUnit>) -> ScientificValue<TargetType, TargetUnit>
) = combine(right, 1(unit).transform(1(right.unit)).unit, factory, transform)

fun <
    LeftType : MeasurementType,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightType>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>
    > ScientificArray<LeftNumberType, LeftType, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightType, RightUnit>,
    unit: TargetUnit,
    transform: ScientificValue<LeftType, LeftUnit>.(ScientificValue<RightType, RightUnit>) -> ScientificValue<TargetType, TargetUnit>
) = combine(right, unit, ::DefaultScientificArray, transform)

fun <
    LeftType : MeasurementType,
    LeftNumberType : Number,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightNumberType : Number,
    RightUnit : ScientificUnit<RightType>,
    TargetType : MeasurementType,
    TargetUnit : ScientificUnit<TargetType>,
    TargetNumberType : Number,
    ArrayType : ScientificArray<TargetNumberType, TargetType, TargetUnit>
    > ScientificArray<LeftNumberType, LeftType, LeftUnit>.combine(
    right: ScientificArray<RightNumberType, RightType, RightUnit>,
    targetUnit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> ArrayType,
    transform: ScientificValue<LeftType, LeftUnit>.(ScientificValue<RightType, RightUnit>) -> ScientificValue<TargetType, TargetUnit>
): ArrayType {
    if (size != right.size)
        throw IndexOutOfBoundsException("Cannot combine Scientific Array of unequal size. Left: $size Right: ${right.size}")
    val newValues = mutableListOf<Decimal>()
    iterator().withIndex().forEach {
        newValues.add(it.value(unit).transform(right[it.index](right.unit)).decimalValue)
    }
    return factory(newValues, targetUnit)
}

infix operator fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    RightUnit : ScientificUnit<Type>
    > ScientificArray<NumberType, Type, Unit>.plus(right: ScientificArray<NumberType, Type, RightUnit>) = concat(right)

infix operator fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    RightUnit : ScientificUnit<Type>
    > ScientificArray<NumberType, Type, Unit>.plus(right: ScientificValue<Type, RightUnit>) = concat(listOf(right).toScientificArray(right.unit))

infix operator fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    RightUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.plus(right: ScientificArray<NumberType, Type, RightUnit>) = listOf(this).toScientificArray(unit).concat(right)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    RightUnit : ScientificUnit<Type>,
    > ScientificArray<*, Type, Unit>.concat(
    right: ScientificArray<*, Type, RightUnit>
) = concat(right, unit)

fun <
    Type : MeasurementType,
    NumberType : Number,
    Unit : ScientificUnit<Type>,
    ArrayType : ScientificArray<NumberType, Type, Unit>
    > ScientificArray<*, Type, Unit>.concat(
    right: ScientificArray<*, Type, Unit>,
    factory: (List<Decimal>, Unit) -> ArrayType
) = concat(right, unit, factory)

fun <
    Type : MeasurementType,
    LeftUnit : ScientificUnit<Type>,
    RightUnit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificArray<*, Type, LeftUnit>.concat(
    right: ScientificArray<*, Type, RightUnit>,
    targetUnit: TargetUnit
) = concat(right, targetUnit, ::DefaultScientificArray)

fun <
    Type : MeasurementType,
    LeftUnit : ScientificUnit<Type>,
    RightUnit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>,
    TargetNumberType : Number,
    ArrayType : ScientificArray<TargetNumberType, Type, TargetUnit>
    > ScientificArray<*, Type, LeftUnit>.concat(
    right: ScientificArray<*, Type, RightUnit>,
    targetUnit: TargetUnit,
    factory: (List<Decimal>, TargetUnit) -> ArrayType
) = factory((convertValues(targetUnit) + right.convertValues(targetUnit)), targetUnit)
