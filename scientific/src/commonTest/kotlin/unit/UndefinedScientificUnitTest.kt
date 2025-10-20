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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.UndefinedQuantityType
import com.splendo.kaluga.scientific.scientificSerializationModule
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UndefinedScientificUnitTest {

    companion object {

        val json = Json {
            serializersModule = scientificSerializationModule
        }
    }

    @Serializable
    data class UnitContainer(val unit: AbstractScientificUnit<*>)

    val undefinedUnitA = CustomUndefinedExtendedUnit.MetricAndImperial(
        PhysicalQuantity.Length,
        siFactor = 1.0,
        siOffset = 0.0,
        symbol = "A",
    )

    val undefinedUnitB = CustomUndefinedExtendedUnit.MetricAndImperial(
        PhysicalQuantity.Weight,
        siFactor = 2.0,
        siOffset = 0.0,
        symbol = "B",
    )

    val undefinedUnitC = CustomUndefinedExtendedUnit.MetricAndImperial(
        PhysicalQuantity.AmountOfSubstance,
        siFactor = 3.0,
        siOffset = 2.0,
        symbol = "C",
    )

    val undefinedUnitD = CustomUndefinedExtendedUnit.MetricAndImperial(
        PhysicalQuantity.ElectricCharge,
        siFactor = 4.0,
        siOffset = 2.0,
        symbol = "D",
    )

    @Test
    fun testToFromSIUnit() {
        assertEquals(Decimal.ONE, undefinedUnitA.toSIUnit(Decimal.ONE))
        assertEquals(Decimal.ONE, undefinedUnitA.fromSIUnit(Decimal.ONE))

        assertEquals(0.5.toDecimal(), undefinedUnitB.toSIUnit(Decimal.ONE))
        assertEquals(2.toDecimal(), undefinedUnitB.fromSIUnit(Decimal.ONE))

        assertEquals((-1).toDecimal() / 3.toDecimal(), undefinedUnitC.toSIUnit(Decimal.ONE))
        assertEquals(5.toDecimal(), undefinedUnitC.fromSIUnit(Decimal.ONE))

        assertEquals((-0.25).toDecimal(), undefinedUnitD.toSIUnit(Decimal.ONE))
        assertEquals(6.toDecimal(), undefinedUnitD.fromSIUnit(Decimal.ONE))

        assertEquals(Hour.toSIUnit(Decimal.ONE), Hour.asUndefined().toSIUnit(Decimal.ONE))
        assertEquals(Hour.fromSIUnit(Decimal.ONE), Hour.asUndefined().fromSIUnit(Decimal.ONE))
    }

    @Test
    fun testAReciprocal() {
        val aReciprocal = undefinedUnitA.reciprocal()
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(aReciprocal)
        assertEquals("${undefinedUnitA.symbol}-1", aReciprocal.symbol)
        assertEquals(undefinedUnitA, aReciprocal.inverse)
        assertSerialization(aReciprocal)

        val hourReciprocal = Hour.reciprocal()
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(hourReciprocal)
        assertEquals("${Hour.symbol}-1", hourReciprocal.symbol)
        assertEquals(Hour.asUndefined(), hourReciprocal.inverse)
        assertSerialization(hourReciprocal)

        assertEquals((1.0.toDecimal() / 60.0.toDecimal()).round(32), hourReciprocal.convert(1.toDecimal(), Minute.reciprocal()).round(32))
    }

    @Test
    fun testAPerB() {
        val aPerB = (undefinedUnitA per undefinedUnitB)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerB)
        assertEquals("${undefinedUnitA.symbol} / ${undefinedUnitB.symbol}", aPerB.symbol)
        assertEquals(undefinedUnitA, aPerB.numerator)
        assertEquals(undefinedUnitB, aPerB.denominator)
        assertSerialization(aPerB)

        val hourPerB = (Hour per undefinedUnitB)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(hourPerB)
        assertEquals("${Hour.symbol} / ${undefinedUnitB.symbol}", hourPerB.symbol)
        assertEquals(Hour.asUndefined(), hourPerB.numerator)
        assertEquals(undefinedUnitB, hourPerB.denominator)
        assertSerialization(hourPerB)

        val aPerWatt = (undefinedUnitA per Watt)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerWatt)
        assertEquals("${undefinedUnitA.symbol} / ${Watt.symbol}", aPerWatt.symbol)
        assertEquals(undefinedUnitA, aPerWatt.numerator)
        assertEquals(Watt.asUndefined(), aPerWatt.denominator)
        assertSerialization(aPerWatt)

        val hourPerWatt = (Hour per Watt)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(hourPerWatt)
        assertEquals("${Hour.symbol} / ${Watt.symbol}", hourPerWatt.symbol)
        assertEquals(Hour.asUndefined(), hourPerWatt.numerator)
        assertEquals(Watt.asUndefined(), hourPerWatt.denominator)
        assertSerialization(hourPerWatt)

        assertEquals(60000.0, hourPerWatt.convert(1, Minute per Kilowatt).toDouble())
    }

    @Test
    fun testAXB() {
        val aXB = (undefinedUnitA x undefinedUnitB)
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(aXB)
        assertEquals("${undefinedUnitA.symbol} * ${undefinedUnitB.symbol}", aXB.symbol)
        assertEquals(undefinedUnitA, aXB.left)
        assertEquals(undefinedUnitB, aXB.right)
        assertSerialization(aXB)

        val hourXB = (Hour x undefinedUnitB)
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(hourXB)
        assertEquals("${Hour.symbol} * ${undefinedUnitB.symbol}", hourXB.symbol)
        assertEquals(Hour.asUndefined(), hourXB.left)
        assertEquals(undefinedUnitB, hourXB.right)
        assertSerialization(hourXB)

        val aXWatt = (undefinedUnitA x Watt)
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(aXWatt)
        assertEquals("${undefinedUnitA.symbol} * ${Watt.symbol}", aXWatt.symbol)
        assertEquals(undefinedUnitA, aXWatt.left)
        assertEquals(Watt.asUndefined(), aXWatt.right)
        assertSerialization(aXWatt)

        val hourXWatt = (Hour x Watt)
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(hourXWatt)
        assertEquals("${Hour.symbol} * ${Watt.symbol}", hourXWatt.symbol)
        assertEquals(Hour.asUndefined(), hourXWatt.left)
        assertEquals(Watt.asUndefined(), hourXWatt.right)
        assertSerialization(hourXWatt)

        assertEquals(0.06, hourXWatt.convert(1, Minute x Kilowatt).toDouble())
    }

    @Test
    fun testAPerReciprocalB() {
        val aPerReciprocalB = undefinedUnitA per undefinedUnitB.reciprocal()
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(aPerReciprocalB)
        assertEquals("${undefinedUnitA.symbol} * ${undefinedUnitB.symbol}", aPerReciprocalB.symbol)
        assertEquals(undefinedUnitA x undefinedUnitB, aPerReciprocalB)
        assertSerialization(aPerReciprocalB)

        val hourPerReciprocalB = Hour per undefinedUnitB.reciprocal()
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(hourPerReciprocalB)
        assertEquals("${Hour.symbol} * ${undefinedUnitB.symbol}", hourPerReciprocalB.symbol)
        assertEquals(Hour x undefinedUnitB, hourPerReciprocalB)
        assertSerialization(hourPerReciprocalB)
    }

    @Test
    fun testAPerDividedBC() {
        val aPerBOverC = undefinedUnitA per (undefinedUnitB per undefinedUnitC)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerBOverC)
        assertEquals("${undefinedUnitA.symbol} * ${undefinedUnitC.symbol} / ${undefinedUnitB.symbol}", aPerBOverC.symbol)
        assertEquals(undefinedUnitA x undefinedUnitC, aPerBOverC.numerator)
        assertEquals(undefinedUnitB, aPerBOverC.denominator)
        assertSerialization(aPerBOverC)

        val aPerBOverA = undefinedUnitA per (undefinedUnitB per undefinedUnitA)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerBOverA)
        assertEquals("${undefinedUnitA.symbol}2 / ${undefinedUnitB.symbol}", aPerBOverA.symbol)
        assertEquals(undefinedUnitA x undefinedUnitA, aPerBOverA.numerator)
        assertEquals(undefinedUnitB, aPerBOverA.denominator)
        assertSerialization(aPerBOverA)

        val hourPerBOverC = Hour per (undefinedUnitB per undefinedUnitC)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(hourPerBOverC)
        assertEquals("${Hour.symbol} * ${undefinedUnitC.symbol} / ${undefinedUnitB.symbol}", hourPerBOverC.symbol)
        assertEquals(Hour x undefinedUnitC, hourPerBOverC.numerator)
        assertEquals(undefinedUnitB, hourPerBOverC.denominator)
        assertSerialization(hourPerBOverC)
    }

    @Test
    fun testReciprocalAReciprocal() {
        val reciprocalAReciprocal = undefinedUnitA.reciprocal().reciprocal()
        assertEquals(undefinedUnitA, reciprocalAReciprocal)
        assertSerialization(reciprocalAReciprocal)

        val hourReciprocalReciprocal = Hour.reciprocal().reciprocal()
        assertEquals(Hour, hourReciprocalReciprocal)
    }

    @Test
    fun testReciprocalAPerB() {
        val reciprocalAPerB = undefinedUnitA.reciprocal() per undefinedUnitB
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(reciprocalAPerB)
        assertEquals("${undefinedUnitA.symbol}-1 * ${undefinedUnitB.symbol}-1", reciprocalAPerB.symbol)
        assertEquals(undefinedUnitA x undefinedUnitB, reciprocalAPerB.inverse)
        assertSerialization(reciprocalAPerB)

        val reciprocalAPerWatt = undefinedUnitA.reciprocal() per Watt
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(reciprocalAPerWatt)
        assertEquals("${undefinedUnitA.symbol}-1 * ${Watt.symbol}-1", reciprocalAPerWatt.symbol)
        assertEquals(undefinedUnitA x Watt, reciprocalAPerWatt.inverse)
        assertSerialization(reciprocalAPerWatt)
    }

    @Test
    fun testReciprocalAPerReciprocalB() {
        val reciprocalAPerReciprocalB = undefinedUnitA.reciprocal() per undefinedUnitB.reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(reciprocalAPerReciprocalB)
        assertEquals("${undefinedUnitB.symbol} / ${undefinedUnitA.symbol}", reciprocalAPerReciprocalB.symbol)
        assertEquals(undefinedUnitB per undefinedUnitA, reciprocalAPerReciprocalB)
        assertSerialization(reciprocalAPerReciprocalB)
    }

    @Test
    fun testReciprocalAPerDivided() {
        val reciprocalAPerDividedBOverC = undefinedUnitA.reciprocal() per (undefinedUnitB per undefinedUnitC)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(reciprocalAPerDividedBOverC)
        assertEquals("${undefinedUnitC.symbol} / ${undefinedUnitA.symbol} * ${undefinedUnitB.symbol}", reciprocalAPerDividedBOverC.symbol)
        assertEquals(undefinedUnitC, reciprocalAPerDividedBOverC.numerator)
        assertEquals(undefinedUnitA x undefinedUnitB, reciprocalAPerDividedBOverC.denominator)
        assertSerialization(reciprocalAPerDividedBOverC)

        val reciprocalAPerDividedBOverA = undefinedUnitA.reciprocal() per (undefinedUnitA per undefinedUnitB)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(reciprocalAPerDividedBOverA)
        assertEquals("${undefinedUnitB.symbol} / ${undefinedUnitA.symbol}2", reciprocalAPerDividedBOverA.symbol)
        assertEquals(undefinedUnitB, reciprocalAPerDividedBOverA.numerator)
        assertEquals(undefinedUnitA x undefinedUnitA, reciprocalAPerDividedBOverA.denominator)
        assertSerialization(reciprocalAPerDividedBOverA)
    }

    @Test
    fun testDivedAOverBReciprocal() {
        val aPerBReciprocal = (undefinedUnitA per undefinedUnitB).reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerBReciprocal)
        assertEquals("${undefinedUnitB.symbol} / ${undefinedUnitA.symbol}", aPerBReciprocal.symbol)
        assertEquals(undefinedUnitB, aPerBReciprocal.numerator)
        assertEquals(undefinedUnitA, aPerBReciprocal.denominator)
        assertSerialization(aPerBReciprocal)
    }

    @Test
    fun testDividedABPerC() {
        val dividedABPerC = undefinedUnitA per undefinedUnitB per undefinedUnitC
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedABPerC)
        assertEquals("${undefinedUnitA.symbol} / ${undefinedUnitB.symbol} * ${undefinedUnitC.symbol}", dividedABPerC.symbol)
        assertEquals(undefinedUnitA, dividedABPerC.numerator)
        assertEquals(undefinedUnitB x undefinedUnitC, dividedABPerC.denominator)
        assertSerialization(dividedABPerC)

        val dividedABPerB = undefinedUnitA per undefinedUnitB per undefinedUnitB
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedABPerC)
        assertEquals("${undefinedUnitA.symbol} / ${undefinedUnitB.symbol}2", dividedABPerB.symbol)
        assertEquals(undefinedUnitA, dividedABPerB.numerator)
        assertEquals(undefinedUnitB x undefinedUnitB, dividedABPerB.denominator)
        assertSerialization(dividedABPerB)

        val dividedABPerWatt = undefinedUnitA per undefinedUnitB per Watt
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedABPerWatt)
        assertEquals("${undefinedUnitA.symbol} / ${undefinedUnitB.symbol} * ${Watt.symbol}", dividedABPerWatt.symbol)
        assertEquals(undefinedUnitA, dividedABPerWatt.numerator)
        assertEquals(undefinedUnitB x Watt, dividedABPerWatt.denominator)
        assertSerialization(dividedABPerWatt)
    }

    @Test
    fun testDividedPerReciprocalB() {
        val dividedAOverCPerReciprocalB = undefinedUnitA per undefinedUnitC per undefinedUnitB.reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverCPerReciprocalB)
        assertEquals("${undefinedUnitA.symbol} * ${undefinedUnitB.symbol} / ${undefinedUnitC.symbol}", dividedAOverCPerReciprocalB.symbol)
        assertEquals(undefinedUnitA x undefinedUnitB, dividedAOverCPerReciprocalB.numerator)
        assertEquals(undefinedUnitC, dividedAOverCPerReciprocalB.denominator)
        assertSerialization(dividedAOverCPerReciprocalB)

        val dividedAOverCPerReciprocalA = undefinedUnitA per undefinedUnitC per undefinedUnitA.reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverCPerReciprocalA)
        assertEquals("${undefinedUnitA.symbol}2 / ${undefinedUnitC.symbol}", dividedAOverCPerReciprocalA.symbol)
        assertEquals(undefinedUnitA x undefinedUnitA, dividedAOverCPerReciprocalA.numerator)
        assertEquals(undefinedUnitC, dividedAOverCPerReciprocalA.denominator)
        assertSerialization(dividedAOverCPerReciprocalA)
    }

    @Test
    fun testDividedABPerDividedCD() {
        val dividedAOverBPerDividedCOverD = (undefinedUnitA per undefinedUnitB) per (undefinedUnitC per undefinedUnitD)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverBPerDividedCOverD)
        assertEquals("${undefinedUnitA.symbol} * ${undefinedUnitD.symbol} / ${undefinedUnitB.symbol} * ${undefinedUnitC.symbol}", dividedAOverBPerDividedCOverD.symbol)
        assertEquals(undefinedUnitA x undefinedUnitD, dividedAOverBPerDividedCOverD.numerator)
        assertEquals(undefinedUnitB x undefinedUnitC, dividedAOverBPerDividedCOverD.denominator)
        assertSerialization(dividedAOverBPerDividedCOverD)

        val dividedAOverBPerDividedBOverA = (undefinedUnitA per undefinedUnitB) per (undefinedUnitB per undefinedUnitA)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverBPerDividedBOverA)
        assertEquals("${undefinedUnitA.symbol}2 / ${undefinedUnitB.symbol}2", dividedAOverBPerDividedBOverA.symbol)
        assertEquals(undefinedUnitA x undefinedUnitA, dividedAOverBPerDividedBOverA.numerator)
        assertEquals(undefinedUnitB x undefinedUnitB, dividedAOverBPerDividedBOverA.denominator)
        assertSerialization(dividedAOverBPerDividedBOverA)
    }

    private inline fun <reified Quantity : UndefinedQuantityType, reified Unit : AbstractUndefinedScientificUnit<Quantity>> assertSerialization(unit: Unit) {
        val jsonString = Json.encodeToString(unit)
        val decoded: Unit = Json.decodeFromString(jsonString)
        assertEquals(unit, decoded)
        val moduleJsonString = json.encodeToString(unit)
        assertEquals(jsonString, moduleJsonString)
        val moduleDecoded: Unit = json.decodeFromString(moduleJsonString)
        assertEquals(unit, moduleDecoded)
        val container = UnitContainer(unit)
        val containerJsonString = json.encodeToString(UnitContainer.serializer(), container)
        val containerDecoded = json.decodeFromString(UnitContainer.serializer(), containerJsonString)
        assertEquals(container, containerDecoded)
    }
}
