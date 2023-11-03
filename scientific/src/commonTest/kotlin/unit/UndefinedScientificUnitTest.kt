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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UndefinedScientificUnitTest {

    object UndefinedUnitA : CustomUndefinedScientificUnit.MetricAndImperial<UndefinedUnitA>() {
        override val customQuantity: UndefinedUnitA = this
        override val symbol: String = "A"
        override fun fromSIUnit(value: Decimal): Decimal = value
        override fun toSIUnit(value: Decimal): Decimal = value
    }

    object UndefinedUnitB : CustomUndefinedScientificUnit.MetricAndImperial<UndefinedUnitB>() {
        override val customQuantity: UndefinedUnitB = this
        override val symbol: String = "B"
        override fun fromSIUnit(value: Decimal): Decimal = value
        override fun toSIUnit(value: Decimal): Decimal = value
    }

    object UndefinedUnitC : CustomUndefinedScientificUnit.MetricAndImperial<UndefinedUnitC>() {
        override val customQuantity: UndefinedUnitC = this
        override val symbol: String = "C"
        override fun fromSIUnit(value: Decimal): Decimal = value
        override fun toSIUnit(value: Decimal): Decimal = value
    }

    object UndefinedUnitD : CustomUndefinedScientificUnit.MetricAndImperial<UndefinedUnitD>() {
        override val customQuantity: UndefinedUnitD = this
        override val symbol: String = "D"
        override fun fromSIUnit(value: Decimal): Decimal = value
        override fun toSIUnit(value: Decimal): Decimal = value
    }

    @Test
    fun testAReciprocal() {
        val aReciprocal = UndefinedUnitA.reciprocal()
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(aReciprocal)
        assertEquals("${UndefinedUnitA.symbol}-1", aReciprocal.symbol)
        assertEquals(UndefinedUnitA, aReciprocal.inverse)

        val hourReciprocal = Hour.reciprocal()
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(hourReciprocal)
        assertEquals("${Hour.symbol}-1", hourReciprocal.symbol)
        assertEquals(Hour.asUndefined(), hourReciprocal.inverse)
    }

    @Test
    fun testAPerB() {
        val aPerB = (UndefinedUnitA per UndefinedUnitB)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerB)
        assertEquals("${UndefinedUnitA.symbol} / ${UndefinedUnitB.symbol}", aPerB.symbol)
        assertEquals(UndefinedUnitA, aPerB.numerator)
        assertEquals(UndefinedUnitB, aPerB.denominator)

        val hourPerB = (Hour per UndefinedUnitB)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(hourPerB)
        assertEquals("${Hour.symbol} / ${UndefinedUnitB.symbol}", hourPerB.symbol)
        assertEquals(Hour.asUndefined(), hourPerB.numerator)
        assertEquals(UndefinedUnitB, hourPerB.denominator)

        val aPerWatt = (UndefinedUnitA per Watt)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerWatt)
        assertEquals("${UndefinedUnitA.symbol} / ${Watt.symbol}", aPerWatt.symbol)
        assertEquals(UndefinedUnitA, aPerWatt.numerator)
        assertEquals(Watt.asUndefined(), aPerWatt.denominator)

        val hourPerWatt = (Hour per Watt)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(hourPerWatt)
        assertEquals("${Hour.symbol} / ${Watt.symbol}", hourPerWatt.symbol)
        assertEquals(Hour.asUndefined(), hourPerWatt.numerator)
        assertEquals(Watt.asUndefined(), hourPerWatt.denominator)
    }

    @Test
    fun testAPerReciprocalB() {
        val aPerReciprocalB = UndefinedUnitA per UndefinedUnitB.reciprocal()
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(aPerReciprocalB)
        assertEquals("${UndefinedUnitA.symbol} * ${UndefinedUnitB.symbol}", aPerReciprocalB.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitB, aPerReciprocalB)

        val hourPerReciprocalB = Hour per UndefinedUnitB.reciprocal()
        assertIs<UndefinedMultipliedUnit.MetricAndImperial<*, *, *, *>>(hourPerReciprocalB)
        assertEquals("${Hour.symbol} * ${UndefinedUnitB.symbol}", hourPerReciprocalB.symbol)
        assertEquals(Hour x UndefinedUnitB, hourPerReciprocalB)
    }

    @Test
    fun testAPerDividedBC() {
        val aPerBOverC = UndefinedUnitA per (UndefinedUnitB per UndefinedUnitC)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerBOverC)
        assertEquals("${UndefinedUnitA.symbol} * ${UndefinedUnitC.symbol} / ${UndefinedUnitB.symbol}", aPerBOverC.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitC, aPerBOverC.numerator)
        assertEquals(UndefinedUnitB, aPerBOverC.denominator)

        val aPerBOverA = UndefinedUnitA per (UndefinedUnitB per UndefinedUnitA)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerBOverA)
        assertEquals("${UndefinedUnitA.symbol}2 / ${UndefinedUnitB.symbol}", aPerBOverA.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitA, aPerBOverA.numerator)
        assertEquals(UndefinedUnitB, aPerBOverA.denominator)

        val hourPerBOverC = Hour per (UndefinedUnitB per UndefinedUnitC)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(hourPerBOverC)
        assertEquals("${Hour.symbol} * ${UndefinedUnitC.symbol} / ${UndefinedUnitB.symbol}", hourPerBOverC.symbol)
        assertEquals(Hour x UndefinedUnitC, hourPerBOverC.numerator)
        assertEquals(UndefinedUnitB, hourPerBOverC.denominator)
    }

    @Test
    fun testReciprocalAReciprocal() {
        val reciprocalAReciprocal = UndefinedUnitA.reciprocal().reciprocal()
        assertEquals(UndefinedUnitA, reciprocalAReciprocal)

        val hourReciprocalReciprocal = Hour.reciprocal().reciprocal()
        assertEquals(Hour, hourReciprocalReciprocal)
    }

    @Test
    fun testReciprocalAPerB() {
        val reciprocalAPerB = UndefinedUnitA.reciprocal() per UndefinedUnitB
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(reciprocalAPerB)
        assertEquals("${UndefinedUnitA.symbol}-1 * ${UndefinedUnitB.symbol}-1", reciprocalAPerB.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitB, reciprocalAPerB.inverse)

        val reciprocalAPerWatt = UndefinedUnitA.reciprocal() per Watt
        assertIs<UndefinedReciprocalUnit.MetricAndImperial<*, *>>(reciprocalAPerWatt)
        assertEquals("${UndefinedUnitA.symbol}-1 * ${Watt.symbol}-1", reciprocalAPerWatt.symbol)
        assertEquals(UndefinedUnitA x Watt, reciprocalAPerWatt.inverse)
    }

    @Test
    fun testReciprocalAPerReciprocalB() {
        val reciprocalAPerReciprocalB = UndefinedUnitA.reciprocal() per UndefinedUnitB.reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(reciprocalAPerReciprocalB)
        assertEquals("${UndefinedUnitB.symbol} / ${UndefinedUnitA.symbol}", reciprocalAPerReciprocalB.symbol)
        assertEquals(UndefinedUnitB per UndefinedUnitA, reciprocalAPerReciprocalB)
    }

    @Test
    fun testReciprocalAPerDivided() {
        val reciprocalAPerDividedBOverC = UndefinedUnitA.reciprocal() per (UndefinedUnitB per UndefinedUnitC)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(reciprocalAPerDividedBOverC)
        assertEquals("${UndefinedUnitC.symbol} / ${UndefinedUnitA.symbol} * ${UndefinedUnitB.symbol}", reciprocalAPerDividedBOverC.symbol)
        assertEquals(UndefinedUnitC, reciprocalAPerDividedBOverC.numerator)
        assertEquals(UndefinedUnitA x UndefinedUnitB, reciprocalAPerDividedBOverC.denominator)

        val reciprocalAPerDividedBOverA = UndefinedUnitA.reciprocal() per (UndefinedUnitA per UndefinedUnitB)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(reciprocalAPerDividedBOverA)
        assertEquals("${UndefinedUnitB.symbol} / ${UndefinedUnitA.symbol}2", reciprocalAPerDividedBOverA.symbol)
        assertEquals(UndefinedUnitB, reciprocalAPerDividedBOverA.numerator)
        assertEquals(UndefinedUnitA x UndefinedUnitA, reciprocalAPerDividedBOverA.denominator)
    }

    @Test
    fun testDivedAOverBReciprocal() {
        val aPerBReciprocal = (UndefinedUnitA per UndefinedUnitB).reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(aPerBReciprocal)
        assertEquals("${UndefinedUnitB.symbol} / ${UndefinedUnitA.symbol}", aPerBReciprocal.symbol)
        assertEquals(UndefinedUnitB, aPerBReciprocal.numerator)
        assertEquals(UndefinedUnitA, aPerBReciprocal.denominator)
    }

    @Test
    fun testDividedABPerC() {
        val dividedABPerC = UndefinedUnitA per UndefinedUnitB per UndefinedUnitC
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedABPerC)
        assertEquals("${UndefinedUnitA.symbol} / ${UndefinedUnitB.symbol} * ${UndefinedUnitC.symbol}", dividedABPerC.symbol)
        assertEquals(UndefinedUnitA, dividedABPerC.numerator)
        assertEquals(UndefinedUnitB x UndefinedUnitC, dividedABPerC.denominator)

        val dividedABPerB = UndefinedUnitA per UndefinedUnitB per UndefinedUnitB
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedABPerC)
        assertEquals("${UndefinedUnitA.symbol} / ${UndefinedUnitB.symbol}2", dividedABPerB.symbol)
        assertEquals(UndefinedUnitA, dividedABPerB.numerator)
        assertEquals(UndefinedUnitB x UndefinedUnitB, dividedABPerB.denominator)

        val dividedABPerWatt = UndefinedUnitA per UndefinedUnitB per Watt
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedABPerWatt)
        assertEquals("${UndefinedUnitA.symbol} / ${UndefinedUnitB.symbol} * ${Watt.symbol}", dividedABPerWatt.symbol)
        assertEquals(UndefinedUnitA, dividedABPerWatt.numerator)
        assertEquals(UndefinedUnitB x Watt, dividedABPerWatt.denominator)
    }

    @Test
    fun testDividedPerReciprocalB() {
        val dividedAOverCPerReciprocalB = UndefinedUnitA per UndefinedUnitC per UndefinedUnitB.reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverCPerReciprocalB)
        assertEquals("${UndefinedUnitA.symbol} * ${UndefinedUnitB.symbol} / ${UndefinedUnitC.symbol}", dividedAOverCPerReciprocalB.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitB, dividedAOverCPerReciprocalB.numerator)
        assertEquals(UndefinedUnitC, dividedAOverCPerReciprocalB.denominator)

        val dividedAOverCPerReciprocalA = UndefinedUnitA per UndefinedUnitC per UndefinedUnitA.reciprocal()
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverCPerReciprocalA)
        assertEquals("${UndefinedUnitA.symbol}2 / ${UndefinedUnitC.symbol}", dividedAOverCPerReciprocalA.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitA, dividedAOverCPerReciprocalA.numerator)
        assertEquals(UndefinedUnitC, dividedAOverCPerReciprocalA.denominator)
    }

    @Test
    fun testDividedABPerDividedCD() {
        val dividedAOverBPerDividedCOverD = (UndefinedUnitA per UndefinedUnitB) per (UndefinedUnitC per UndefinedUnitD)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverBPerDividedCOverD)
        assertEquals("${UndefinedUnitA.symbol} * ${UndefinedUnitD.symbol} / ${UndefinedUnitB.symbol} * ${UndefinedUnitC.symbol}", dividedAOverBPerDividedCOverD.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitD, dividedAOverBPerDividedCOverD.numerator)
        assertEquals(UndefinedUnitB x UndefinedUnitC, dividedAOverBPerDividedCOverD.denominator)

        val dividedAOverBPerDividedBOverA = (UndefinedUnitA per UndefinedUnitB) per (UndefinedUnitB per UndefinedUnitA)
        assertIs<UndefinedDividedUnit.MetricAndImperial<*, *, *, *>>(dividedAOverBPerDividedBOverA)
        assertEquals("${UndefinedUnitA.symbol}2 / ${UndefinedUnitB.symbol}2", dividedAOverBPerDividedBOverA.symbol)
        assertEquals(UndefinedUnitA x UndefinedUnitA, dividedAOverBPerDividedBOverA.numerator)
        assertEquals(UndefinedUnitB x UndefinedUnitB, dividedAOverBPerDividedBOverA.denominator)
    }
}
