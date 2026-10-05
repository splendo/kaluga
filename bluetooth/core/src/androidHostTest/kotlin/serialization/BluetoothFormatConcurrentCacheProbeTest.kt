/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.serialization

import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
private data class CacheProbeType0(val v: Int)

@Serializable
private data class CacheProbeType1(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType2(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType3(val items: List<Byte>)

@Serializable
private data class CacheProbeType4(val v: Int)

@Serializable
private data class CacheProbeType5(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType6(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType7(val items: List<Byte>)

@Serializable
private data class CacheProbeType8(val v: Int)

@Serializable
private data class CacheProbeType9(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType10(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType11(val items: List<Byte>)

@Serializable
private data class CacheProbeType12(val v: Int)

@Serializable
private data class CacheProbeType13(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType14(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType15(val items: List<Byte>)

@Serializable
private data class CacheProbeType16(val v: Int)

@Serializable
private data class CacheProbeType17(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType18(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType19(val items: List<Byte>)

@Serializable
private data class CacheProbeType20(val v: Int)

@Serializable
private data class CacheProbeType21(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType22(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType23(val items: List<Byte>)

@Serializable
private data class CacheProbeType24(val v: Int)

@Serializable
private data class CacheProbeType25(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType26(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType27(val items: List<Byte>)

@Serializable
private data class CacheProbeType28(val v: Int)

@Serializable
private data class CacheProbeType29(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType30(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType31(val items: List<Byte>)

@Serializable
private data class CacheProbeType32(val v: Int)

@Serializable
private data class CacheProbeType33(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType34(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType35(val items: List<Byte>)

@Serializable
private data class CacheProbeType36(val v: Int)

@Serializable
private data class CacheProbeType37(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType38(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType39(val items: List<Byte>)

@Serializable
private data class CacheProbeType40(val v: Int)

@Serializable
private data class CacheProbeType41(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType42(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType43(val items: List<Byte>)

@Serializable
private data class CacheProbeType44(val v: Int)

@Serializable
private data class CacheProbeType45(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType46(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType47(val items: List<Byte>)

@Serializable
private data class CacheProbeType48(val v: Int)

@Serializable
private data class CacheProbeType49(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType50(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType51(val items: List<Byte>)

@Serializable
private data class CacheProbeType52(val v: Int)

@Serializable
private data class CacheProbeType53(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType54(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType55(val items: List<Byte>)

@Serializable
private data class CacheProbeType56(val v: Int)

@Serializable
private data class CacheProbeType57(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType58(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType59(val items: List<Byte>)

@Serializable
private data class CacheProbeType60(val v: Int)

@Serializable
private data class CacheProbeType61(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType62(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType63(val items: List<Byte>)

@Serializable
private data class CacheProbeType64(val v: Int)

@Serializable
private data class CacheProbeType65(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType66(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType67(val items: List<Byte>)

@Serializable
private data class CacheProbeType68(val v: Int)

@Serializable
private data class CacheProbeType69(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType70(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType71(val items: List<Byte>)

@Serializable
private data class CacheProbeType72(val v: Int)

@Serializable
private data class CacheProbeType73(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType74(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType75(val items: List<Byte>)

@Serializable
private data class CacheProbeType76(val v: Int)

@Serializable
private data class CacheProbeType77(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType78(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType79(val items: List<Byte>)

@Serializable
private data class CacheProbeType80(val v: Int)

@Serializable
private data class CacheProbeType81(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType82(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType83(val items: List<Byte>)

@Serializable
private data class CacheProbeType84(val v: Int)

@Serializable
private data class CacheProbeType85(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType86(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType87(val items: List<Byte>)

@Serializable
private data class CacheProbeType88(val v: Int)

@Serializable
private data class CacheProbeType89(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType90(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType91(val items: List<Byte>)

@Serializable
private data class CacheProbeType92(val v: Int)

@Serializable
private data class CacheProbeType93(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType94(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType95(val items: List<Byte>)

@Serializable
private data class CacheProbeType96(val v: Int)

@Serializable
private data class CacheProbeType97(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType98(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType99(val items: List<Byte>)

@Serializable
private data class CacheProbeType100(val v: Int)

@Serializable
private data class CacheProbeType101(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType102(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType103(val items: List<Byte>)

@Serializable
private data class CacheProbeType104(val v: Int)

@Serializable
private data class CacheProbeType105(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType106(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType107(val items: List<Byte>)

@Serializable
private data class CacheProbeType108(val v: Int)

@Serializable
private data class CacheProbeType109(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType110(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType111(val items: List<Byte>)

@Serializable
private data class CacheProbeType112(val v: Int)

@Serializable
private data class CacheProbeType113(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType114(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType115(val items: List<Byte>)

@Serializable
private data class CacheProbeType116(val v: Int)

@Serializable
private data class CacheProbeType117(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType118(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType119(val items: List<Byte>)

@Serializable
private data class CacheProbeType120(val v: Int)

@Serializable
private data class CacheProbeType121(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType122(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType123(val items: List<Byte>)

@Serializable
private data class CacheProbeType124(val v: Int)

@Serializable
private data class CacheProbeType125(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType126(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType127(val items: List<Byte>)

@Serializable
private data class CacheProbeType128(val v: Int)

@Serializable
private data class CacheProbeType129(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType130(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType131(val items: List<Byte>)

@Serializable
private data class CacheProbeType132(val v: Int)

@Serializable
private data class CacheProbeType133(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType134(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType135(val items: List<Byte>)

@Serializable
private data class CacheProbeType136(val v: Int)

@Serializable
private data class CacheProbeType137(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType138(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType139(val items: List<Byte>)

@Serializable
private data class CacheProbeType140(val v: Int)

@Serializable
private data class CacheProbeType141(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType142(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType143(val items: List<Byte>)

@Serializable
private data class CacheProbeType144(val v: Int)

@Serializable
private data class CacheProbeType145(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType146(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType147(val items: List<Byte>)

@Serializable
private data class CacheProbeType148(val v: Int)

@Serializable
private data class CacheProbeType149(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType150(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType151(val items: List<Byte>)

@Serializable
private data class CacheProbeType152(val v: Int)

@Serializable
private data class CacheProbeType153(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType154(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType155(val items: List<Byte>)

@Serializable
private data class CacheProbeType156(val v: Int)

@Serializable
private data class CacheProbeType157(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType158(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType159(val items: List<Byte>)

@Serializable
private data class CacheProbeType160(val v: Int)

@Serializable
private data class CacheProbeType161(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType162(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType163(val items: List<Byte>)

@Serializable
private data class CacheProbeType164(val v: Int)

@Serializable
private data class CacheProbeType165(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType166(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType167(val items: List<Byte>)

@Serializable
private data class CacheProbeType168(val v: Int)

@Serializable
private data class CacheProbeType169(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType170(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType171(val items: List<Byte>)

@Serializable
private data class CacheProbeType172(val v: Int)

@Serializable
private data class CacheProbeType173(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType174(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType175(val items: List<Byte>)

@Serializable
private data class CacheProbeType176(val v: Int)

@Serializable
private data class CacheProbeType177(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType178(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType179(val items: List<Byte>)

@Serializable
private data class CacheProbeType180(val v: Int)

@Serializable
private data class CacheProbeType181(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType182(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType183(val items: List<Byte>)

@Serializable
private data class CacheProbeType184(val v: Int)

@Serializable
private data class CacheProbeType185(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType186(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType187(val items: List<Byte>)

@Serializable
private data class CacheProbeType188(val v: Int)

@Serializable
private data class CacheProbeType189(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType190(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType191(val items: List<Byte>)

@Serializable
private data class CacheProbeType192(val v: Int)

@Serializable
private data class CacheProbeType193(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType194(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType195(val items: List<Byte>)

@Serializable
private data class CacheProbeType196(val v: Int)

@Serializable
private data class CacheProbeType197(@Unsigned val v: UInt)

@Serializable
private data class CacheProbeType198(val a: Boolean, val b: Boolean, val c: Byte)

@Serializable
private data class CacheProbeType199(val items: List<Byte>)

private class ProbeCase(val label: String, val encode: () -> ByteArray, val decodeMatches: (ByteArray) -> Boolean)

private fun allIterations(): List<List<ProbeCase>> = listOf(
    listOf(
        ProbeCase(
            "CacheProbeType0",
            { BluetoothFormat.encodeToByteArray(CacheProbeType0.serializer(), CacheProbeType0(7)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType0.serializer(), bytes) == CacheProbeType0(7) },
        ),
        ProbeCase(
            "CacheProbeType1",
            { BluetoothFormat.encodeToByteArray(CacheProbeType1.serializer(), CacheProbeType1(1007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType1.serializer(), bytes) == CacheProbeType1(1007u) },
        ),
        ProbeCase(
            "CacheProbeType2",
            { BluetoothFormat.encodeToByteArray(CacheProbeType2.serializer(), CacheProbeType2(a = true, b = false, c = 2)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType2.serializer(), bytes) == CacheProbeType2(a = true, b = false, c = 2) },
        ),
        ProbeCase(
            "CacheProbeType3",
            { BluetoothFormat.encodeToByteArray(CacheProbeType3.serializer(), CacheProbeType3(items = listOf(3, 4, 5))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType3.serializer(), bytes) == CacheProbeType3(items = listOf(3, 4, 5)) },
        ),
        ProbeCase(
            "CacheProbeType4",
            { BluetoothFormat.encodeToByteArray(CacheProbeType4.serializer(), CacheProbeType4(4007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType4.serializer(), bytes) == CacheProbeType4(4007) },
        ),
        ProbeCase(
            "CacheProbeType5",
            { BluetoothFormat.encodeToByteArray(CacheProbeType5.serializer(), CacheProbeType5(5007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType5.serializer(), bytes) == CacheProbeType5(5007u) },
        ),
        ProbeCase(
            "CacheProbeType6",
            { BluetoothFormat.encodeToByteArray(CacheProbeType6.serializer(), CacheProbeType6(a = true, b = true, c = 6)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType6.serializer(), bytes) == CacheProbeType6(a = true, b = true, c = 6) },
        ),
        ProbeCase(
            "CacheProbeType7",
            { BluetoothFormat.encodeToByteArray(CacheProbeType7.serializer(), CacheProbeType7(items = listOf(7, 8, 9))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType7.serializer(), bytes) == CacheProbeType7(items = listOf(7, 8, 9)) },
        ),
        ProbeCase(
            "CacheProbeType8",
            { BluetoothFormat.encodeToByteArray(CacheProbeType8.serializer(), CacheProbeType8(8007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType8.serializer(), bytes) == CacheProbeType8(8007) },
        ),
        ProbeCase(
            "CacheProbeType9",
            { BluetoothFormat.encodeToByteArray(CacheProbeType9.serializer(), CacheProbeType9(9007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType9.serializer(), bytes) == CacheProbeType9(9007u) },
        ),
        ProbeCase(
            "CacheProbeType10",
            { BluetoothFormat.encodeToByteArray(CacheProbeType10.serializer(), CacheProbeType10(a = true, b = false, c = 10)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType10.serializer(), bytes) == CacheProbeType10(a = true, b = false, c = 10) },
        ),
        ProbeCase(
            "CacheProbeType11",
            { BluetoothFormat.encodeToByteArray(CacheProbeType11.serializer(), CacheProbeType11(items = listOf(11, 12, 13))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType11.serializer(), bytes) == CacheProbeType11(items = listOf(11, 12, 13)) },
        ),
        ProbeCase(
            "CacheProbeType12",
            { BluetoothFormat.encodeToByteArray(CacheProbeType12.serializer(), CacheProbeType12(12007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType12.serializer(), bytes) == CacheProbeType12(12007) },
        ),
        ProbeCase(
            "CacheProbeType13",
            { BluetoothFormat.encodeToByteArray(CacheProbeType13.serializer(), CacheProbeType13(13007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType13.serializer(), bytes) == CacheProbeType13(13007u) },
        ),
        ProbeCase(
            "CacheProbeType14",
            { BluetoothFormat.encodeToByteArray(CacheProbeType14.serializer(), CacheProbeType14(a = true, b = false, c = 14)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType14.serializer(), bytes) == CacheProbeType14(a = true, b = false, c = 14) },
        ),
        ProbeCase(
            "CacheProbeType15",
            { BluetoothFormat.encodeToByteArray(CacheProbeType15.serializer(), CacheProbeType15(items = listOf(15, 16, 17))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType15.serializer(), bytes) == CacheProbeType15(items = listOf(15, 16, 17)) },
        ),
        ProbeCase(
            "CacheProbeType16",
            { BluetoothFormat.encodeToByteArray(CacheProbeType16.serializer(), CacheProbeType16(16007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType16.serializer(), bytes) == CacheProbeType16(16007) },
        ),
        ProbeCase(
            "CacheProbeType17",
            { BluetoothFormat.encodeToByteArray(CacheProbeType17.serializer(), CacheProbeType17(17007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType17.serializer(), bytes) == CacheProbeType17(17007u) },
        ),
        ProbeCase(
            "CacheProbeType18",
            { BluetoothFormat.encodeToByteArray(CacheProbeType18.serializer(), CacheProbeType18(a = true, b = true, c = 18)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType18.serializer(), bytes) == CacheProbeType18(a = true, b = true, c = 18) },
        ),
        ProbeCase(
            "CacheProbeType19",
            { BluetoothFormat.encodeToByteArray(CacheProbeType19.serializer(), CacheProbeType19(items = listOf(19, 20, 21))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType19.serializer(), bytes) == CacheProbeType19(items = listOf(19, 20, 21)) },
        ),
        ProbeCase(
            "CacheProbeType20",
            { BluetoothFormat.encodeToByteArray(CacheProbeType20.serializer(), CacheProbeType20(20007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType20.serializer(), bytes) == CacheProbeType20(20007) },
        ),
        ProbeCase(
            "CacheProbeType21",
            { BluetoothFormat.encodeToByteArray(CacheProbeType21.serializer(), CacheProbeType21(21007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType21.serializer(), bytes) == CacheProbeType21(21007u) },
        ),
        ProbeCase(
            "CacheProbeType22",
            { BluetoothFormat.encodeToByteArray(CacheProbeType22.serializer(), CacheProbeType22(a = true, b = false, c = 22)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType22.serializer(), bytes) == CacheProbeType22(a = true, b = false, c = 22) },
        ),
        ProbeCase(
            "CacheProbeType23",
            { BluetoothFormat.encodeToByteArray(CacheProbeType23.serializer(), CacheProbeType23(items = listOf(23, 24, 25))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType23.serializer(), bytes) == CacheProbeType23(items = listOf(23, 24, 25)) },
        ),
        ProbeCase(
            "CacheProbeType24",
            { BluetoothFormat.encodeToByteArray(CacheProbeType24.serializer(), CacheProbeType24(24007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType24.serializer(), bytes) == CacheProbeType24(24007) },
        ),
        ProbeCase(
            "CacheProbeType25",
            { BluetoothFormat.encodeToByteArray(CacheProbeType25.serializer(), CacheProbeType25(25007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType25.serializer(), bytes) == CacheProbeType25(25007u) },
        ),
        ProbeCase(
            "CacheProbeType26",
            { BluetoothFormat.encodeToByteArray(CacheProbeType26.serializer(), CacheProbeType26(a = true, b = false, c = 26)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType26.serializer(), bytes) == CacheProbeType26(a = true, b = false, c = 26) },
        ),
        ProbeCase(
            "CacheProbeType27",
            { BluetoothFormat.encodeToByteArray(CacheProbeType27.serializer(), CacheProbeType27(items = listOf(27, 28, 29))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType27.serializer(), bytes) == CacheProbeType27(items = listOf(27, 28, 29)) },
        ),
        ProbeCase(
            "CacheProbeType28",
            { BluetoothFormat.encodeToByteArray(CacheProbeType28.serializer(), CacheProbeType28(28007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType28.serializer(), bytes) == CacheProbeType28(28007) },
        ),
        ProbeCase(
            "CacheProbeType29",
            { BluetoothFormat.encodeToByteArray(CacheProbeType29.serializer(), CacheProbeType29(29007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType29.serializer(), bytes) == CacheProbeType29(29007u) },
        ),
        ProbeCase(
            "CacheProbeType30",
            { BluetoothFormat.encodeToByteArray(CacheProbeType30.serializer(), CacheProbeType30(a = true, b = true, c = 30)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType30.serializer(), bytes) == CacheProbeType30(a = true, b = true, c = 30) },
        ),
        ProbeCase(
            "CacheProbeType31",
            { BluetoothFormat.encodeToByteArray(CacheProbeType31.serializer(), CacheProbeType31(items = listOf(31, 32, 33))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType31.serializer(), bytes) == CacheProbeType31(items = listOf(31, 32, 33)) },
        ),
        ProbeCase(
            "CacheProbeType32",
            { BluetoothFormat.encodeToByteArray(CacheProbeType32.serializer(), CacheProbeType32(32007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType32.serializer(), bytes) == CacheProbeType32(32007) },
        ),
        ProbeCase(
            "CacheProbeType33",
            { BluetoothFormat.encodeToByteArray(CacheProbeType33.serializer(), CacheProbeType33(33007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType33.serializer(), bytes) == CacheProbeType33(33007u) },
        ),
        ProbeCase(
            "CacheProbeType34",
            { BluetoothFormat.encodeToByteArray(CacheProbeType34.serializer(), CacheProbeType34(a = true, b = false, c = 34)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType34.serializer(), bytes) == CacheProbeType34(a = true, b = false, c = 34) },
        ),
        ProbeCase(
            "CacheProbeType35",
            { BluetoothFormat.encodeToByteArray(CacheProbeType35.serializer(), CacheProbeType35(items = listOf(35, 36, 37))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType35.serializer(), bytes) == CacheProbeType35(items = listOf(35, 36, 37)) },
        ),
        ProbeCase(
            "CacheProbeType36",
            { BluetoothFormat.encodeToByteArray(CacheProbeType36.serializer(), CacheProbeType36(36007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType36.serializer(), bytes) == CacheProbeType36(36007) },
        ),
        ProbeCase(
            "CacheProbeType37",
            { BluetoothFormat.encodeToByteArray(CacheProbeType37.serializer(), CacheProbeType37(37007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType37.serializer(), bytes) == CacheProbeType37(37007u) },
        ),
        ProbeCase(
            "CacheProbeType38",
            { BluetoothFormat.encodeToByteArray(CacheProbeType38.serializer(), CacheProbeType38(a = true, b = false, c = 38)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType38.serializer(), bytes) == CacheProbeType38(a = true, b = false, c = 38) },
        ),
        ProbeCase(
            "CacheProbeType39",
            { BluetoothFormat.encodeToByteArray(CacheProbeType39.serializer(), CacheProbeType39(items = listOf(39, 40, 41))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType39.serializer(), bytes) == CacheProbeType39(items = listOf(39, 40, 41)) },
        ),
        ProbeCase(
            "CacheProbeType40",
            { BluetoothFormat.encodeToByteArray(CacheProbeType40.serializer(), CacheProbeType40(40007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType40.serializer(), bytes) == CacheProbeType40(40007) },
        ),
        ProbeCase(
            "CacheProbeType41",
            { BluetoothFormat.encodeToByteArray(CacheProbeType41.serializer(), CacheProbeType41(41007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType41.serializer(), bytes) == CacheProbeType41(41007u) },
        ),
        ProbeCase(
            "CacheProbeType42",
            { BluetoothFormat.encodeToByteArray(CacheProbeType42.serializer(), CacheProbeType42(a = true, b = true, c = 42)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType42.serializer(), bytes) == CacheProbeType42(a = true, b = true, c = 42) },
        ),
        ProbeCase(
            "CacheProbeType43",
            { BluetoothFormat.encodeToByteArray(CacheProbeType43.serializer(), CacheProbeType43(items = listOf(43, 44, 45))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType43.serializer(), bytes) == CacheProbeType43(items = listOf(43, 44, 45)) },
        ),
        ProbeCase(
            "CacheProbeType44",
            { BluetoothFormat.encodeToByteArray(CacheProbeType44.serializer(), CacheProbeType44(44007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType44.serializer(), bytes) == CacheProbeType44(44007) },
        ),
        ProbeCase(
            "CacheProbeType45",
            { BluetoothFormat.encodeToByteArray(CacheProbeType45.serializer(), CacheProbeType45(45007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType45.serializer(), bytes) == CacheProbeType45(45007u) },
        ),
        ProbeCase(
            "CacheProbeType46",
            { BluetoothFormat.encodeToByteArray(CacheProbeType46.serializer(), CacheProbeType46(a = true, b = false, c = 46)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType46.serializer(), bytes) == CacheProbeType46(a = true, b = false, c = 46) },
        ),
        ProbeCase(
            "CacheProbeType47",
            { BluetoothFormat.encodeToByteArray(CacheProbeType47.serializer(), CacheProbeType47(items = listOf(47, 48, 49))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType47.serializer(), bytes) == CacheProbeType47(items = listOf(47, 48, 49)) },
        ),
        ProbeCase(
            "CacheProbeType48",
            { BluetoothFormat.encodeToByteArray(CacheProbeType48.serializer(), CacheProbeType48(48007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType48.serializer(), bytes) == CacheProbeType48(48007) },
        ),
        ProbeCase(
            "CacheProbeType49",
            { BluetoothFormat.encodeToByteArray(CacheProbeType49.serializer(), CacheProbeType49(49007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType49.serializer(), bytes) == CacheProbeType49(49007u) },
        ),
    ),
    listOf(
        ProbeCase(
            "CacheProbeType50",
            { BluetoothFormat.encodeToByteArray(CacheProbeType50.serializer(), CacheProbeType50(a = true, b = false, c = 50)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType50.serializer(), bytes) == CacheProbeType50(a = true, b = false, c = 50) },
        ),
        ProbeCase(
            "CacheProbeType51",
            { BluetoothFormat.encodeToByteArray(CacheProbeType51.serializer(), CacheProbeType51(items = listOf(51, 52, 53))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType51.serializer(), bytes) == CacheProbeType51(items = listOf(51, 52, 53)) },
        ),
        ProbeCase(
            "CacheProbeType52",
            { BluetoothFormat.encodeToByteArray(CacheProbeType52.serializer(), CacheProbeType52(52007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType52.serializer(), bytes) == CacheProbeType52(52007) },
        ),
        ProbeCase(
            "CacheProbeType53",
            { BluetoothFormat.encodeToByteArray(CacheProbeType53.serializer(), CacheProbeType53(53007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType53.serializer(), bytes) == CacheProbeType53(53007u) },
        ),
        ProbeCase(
            "CacheProbeType54",
            { BluetoothFormat.encodeToByteArray(CacheProbeType54.serializer(), CacheProbeType54(a = true, b = true, c = 54)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType54.serializer(), bytes) == CacheProbeType54(a = true, b = true, c = 54) },
        ),
        ProbeCase(
            "CacheProbeType55",
            { BluetoothFormat.encodeToByteArray(CacheProbeType55.serializer(), CacheProbeType55(items = listOf(55, 56, 57))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType55.serializer(), bytes) == CacheProbeType55(items = listOf(55, 56, 57)) },
        ),
        ProbeCase(
            "CacheProbeType56",
            { BluetoothFormat.encodeToByteArray(CacheProbeType56.serializer(), CacheProbeType56(56007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType56.serializer(), bytes) == CacheProbeType56(56007) },
        ),
        ProbeCase(
            "CacheProbeType57",
            { BluetoothFormat.encodeToByteArray(CacheProbeType57.serializer(), CacheProbeType57(57007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType57.serializer(), bytes) == CacheProbeType57(57007u) },
        ),
        ProbeCase(
            "CacheProbeType58",
            { BluetoothFormat.encodeToByteArray(CacheProbeType58.serializer(), CacheProbeType58(a = true, b = false, c = 58)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType58.serializer(), bytes) == CacheProbeType58(a = true, b = false, c = 58) },
        ),
        ProbeCase(
            "CacheProbeType59",
            { BluetoothFormat.encodeToByteArray(CacheProbeType59.serializer(), CacheProbeType59(items = listOf(59, 60, 61))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType59.serializer(), bytes) == CacheProbeType59(items = listOf(59, 60, 61)) },
        ),
        ProbeCase(
            "CacheProbeType60",
            { BluetoothFormat.encodeToByteArray(CacheProbeType60.serializer(), CacheProbeType60(60007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType60.serializer(), bytes) == CacheProbeType60(60007) },
        ),
        ProbeCase(
            "CacheProbeType61",
            { BluetoothFormat.encodeToByteArray(CacheProbeType61.serializer(), CacheProbeType61(61007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType61.serializer(), bytes) == CacheProbeType61(61007u) },
        ),
        ProbeCase(
            "CacheProbeType62",
            { BluetoothFormat.encodeToByteArray(CacheProbeType62.serializer(), CacheProbeType62(a = true, b = false, c = 62)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType62.serializer(), bytes) == CacheProbeType62(a = true, b = false, c = 62) },
        ),
        ProbeCase(
            "CacheProbeType63",
            { BluetoothFormat.encodeToByteArray(CacheProbeType63.serializer(), CacheProbeType63(items = listOf(63, 64, 65))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType63.serializer(), bytes) == CacheProbeType63(items = listOf(63, 64, 65)) },
        ),
        ProbeCase(
            "CacheProbeType64",
            { BluetoothFormat.encodeToByteArray(CacheProbeType64.serializer(), CacheProbeType64(64007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType64.serializer(), bytes) == CacheProbeType64(64007) },
        ),
        ProbeCase(
            "CacheProbeType65",
            { BluetoothFormat.encodeToByteArray(CacheProbeType65.serializer(), CacheProbeType65(65007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType65.serializer(), bytes) == CacheProbeType65(65007u) },
        ),
        ProbeCase(
            "CacheProbeType66",
            { BluetoothFormat.encodeToByteArray(CacheProbeType66.serializer(), CacheProbeType66(a = true, b = true, c = 66)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType66.serializer(), bytes) == CacheProbeType66(a = true, b = true, c = 66) },
        ),
        ProbeCase(
            "CacheProbeType67",
            { BluetoothFormat.encodeToByteArray(CacheProbeType67.serializer(), CacheProbeType67(items = listOf(67, 68, 69))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType67.serializer(), bytes) == CacheProbeType67(items = listOf(67, 68, 69)) },
        ),
        ProbeCase(
            "CacheProbeType68",
            { BluetoothFormat.encodeToByteArray(CacheProbeType68.serializer(), CacheProbeType68(68007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType68.serializer(), bytes) == CacheProbeType68(68007) },
        ),
        ProbeCase(
            "CacheProbeType69",
            { BluetoothFormat.encodeToByteArray(CacheProbeType69.serializer(), CacheProbeType69(69007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType69.serializer(), bytes) == CacheProbeType69(69007u) },
        ),
        ProbeCase(
            "CacheProbeType70",
            { BluetoothFormat.encodeToByteArray(CacheProbeType70.serializer(), CacheProbeType70(a = true, b = false, c = 70)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType70.serializer(), bytes) == CacheProbeType70(a = true, b = false, c = 70) },
        ),
        ProbeCase(
            "CacheProbeType71",
            { BluetoothFormat.encodeToByteArray(CacheProbeType71.serializer(), CacheProbeType71(items = listOf(71, 72, 73))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType71.serializer(), bytes) == CacheProbeType71(items = listOf(71, 72, 73)) },
        ),
        ProbeCase(
            "CacheProbeType72",
            { BluetoothFormat.encodeToByteArray(CacheProbeType72.serializer(), CacheProbeType72(72007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType72.serializer(), bytes) == CacheProbeType72(72007) },
        ),
        ProbeCase(
            "CacheProbeType73",
            { BluetoothFormat.encodeToByteArray(CacheProbeType73.serializer(), CacheProbeType73(73007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType73.serializer(), bytes) == CacheProbeType73(73007u) },
        ),
        ProbeCase(
            "CacheProbeType74",
            { BluetoothFormat.encodeToByteArray(CacheProbeType74.serializer(), CacheProbeType74(a = true, b = false, c = 74)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType74.serializer(), bytes) == CacheProbeType74(a = true, b = false, c = 74) },
        ),
        ProbeCase(
            "CacheProbeType75",
            { BluetoothFormat.encodeToByteArray(CacheProbeType75.serializer(), CacheProbeType75(items = listOf(75, 76, 77))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType75.serializer(), bytes) == CacheProbeType75(items = listOf(75, 76, 77)) },
        ),
        ProbeCase(
            "CacheProbeType76",
            { BluetoothFormat.encodeToByteArray(CacheProbeType76.serializer(), CacheProbeType76(76007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType76.serializer(), bytes) == CacheProbeType76(76007) },
        ),
        ProbeCase(
            "CacheProbeType77",
            { BluetoothFormat.encodeToByteArray(CacheProbeType77.serializer(), CacheProbeType77(77007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType77.serializer(), bytes) == CacheProbeType77(77007u) },
        ),
        ProbeCase(
            "CacheProbeType78",
            { BluetoothFormat.encodeToByteArray(CacheProbeType78.serializer(), CacheProbeType78(a = true, b = true, c = 78)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType78.serializer(), bytes) == CacheProbeType78(a = true, b = true, c = 78) },
        ),
        ProbeCase(
            "CacheProbeType79",
            { BluetoothFormat.encodeToByteArray(CacheProbeType79.serializer(), CacheProbeType79(items = listOf(79, 80, 81))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType79.serializer(), bytes) == CacheProbeType79(items = listOf(79, 80, 81)) },
        ),
        ProbeCase(
            "CacheProbeType80",
            { BluetoothFormat.encodeToByteArray(CacheProbeType80.serializer(), CacheProbeType80(80007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType80.serializer(), bytes) == CacheProbeType80(80007) },
        ),
        ProbeCase(
            "CacheProbeType81",
            { BluetoothFormat.encodeToByteArray(CacheProbeType81.serializer(), CacheProbeType81(81007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType81.serializer(), bytes) == CacheProbeType81(81007u) },
        ),
        ProbeCase(
            "CacheProbeType82",
            { BluetoothFormat.encodeToByteArray(CacheProbeType82.serializer(), CacheProbeType82(a = true, b = false, c = 82)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType82.serializer(), bytes) == CacheProbeType82(a = true, b = false, c = 82) },
        ),
        ProbeCase(
            "CacheProbeType83",
            { BluetoothFormat.encodeToByteArray(CacheProbeType83.serializer(), CacheProbeType83(items = listOf(83, 84, 85))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType83.serializer(), bytes) == CacheProbeType83(items = listOf(83, 84, 85)) },
        ),
        ProbeCase(
            "CacheProbeType84",
            { BluetoothFormat.encodeToByteArray(CacheProbeType84.serializer(), CacheProbeType84(84007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType84.serializer(), bytes) == CacheProbeType84(84007) },
        ),
        ProbeCase(
            "CacheProbeType85",
            { BluetoothFormat.encodeToByteArray(CacheProbeType85.serializer(), CacheProbeType85(85007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType85.serializer(), bytes) == CacheProbeType85(85007u) },
        ),
        ProbeCase(
            "CacheProbeType86",
            { BluetoothFormat.encodeToByteArray(CacheProbeType86.serializer(), CacheProbeType86(a = true, b = false, c = 86)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType86.serializer(), bytes) == CacheProbeType86(a = true, b = false, c = 86) },
        ),
        ProbeCase(
            "CacheProbeType87",
            { BluetoothFormat.encodeToByteArray(CacheProbeType87.serializer(), CacheProbeType87(items = listOf(87, 88, 89))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType87.serializer(), bytes) == CacheProbeType87(items = listOf(87, 88, 89)) },
        ),
        ProbeCase(
            "CacheProbeType88",
            { BluetoothFormat.encodeToByteArray(CacheProbeType88.serializer(), CacheProbeType88(88007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType88.serializer(), bytes) == CacheProbeType88(88007) },
        ),
        ProbeCase(
            "CacheProbeType89",
            { BluetoothFormat.encodeToByteArray(CacheProbeType89.serializer(), CacheProbeType89(89007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType89.serializer(), bytes) == CacheProbeType89(89007u) },
        ),
        ProbeCase(
            "CacheProbeType90",
            { BluetoothFormat.encodeToByteArray(CacheProbeType90.serializer(), CacheProbeType90(a = true, b = true, c = 90)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType90.serializer(), bytes) == CacheProbeType90(a = true, b = true, c = 90) },
        ),
        ProbeCase(
            "CacheProbeType91",
            { BluetoothFormat.encodeToByteArray(CacheProbeType91.serializer(), CacheProbeType91(items = listOf(91, 92, 93))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType91.serializer(), bytes) == CacheProbeType91(items = listOf(91, 92, 93)) },
        ),
        ProbeCase(
            "CacheProbeType92",
            { BluetoothFormat.encodeToByteArray(CacheProbeType92.serializer(), CacheProbeType92(92007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType92.serializer(), bytes) == CacheProbeType92(92007) },
        ),
        ProbeCase(
            "CacheProbeType93",
            { BluetoothFormat.encodeToByteArray(CacheProbeType93.serializer(), CacheProbeType93(93007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType93.serializer(), bytes) == CacheProbeType93(93007u) },
        ),
        ProbeCase(
            "CacheProbeType94",
            { BluetoothFormat.encodeToByteArray(CacheProbeType94.serializer(), CacheProbeType94(a = true, b = false, c = 94)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType94.serializer(), bytes) == CacheProbeType94(a = true, b = false, c = 94) },
        ),
        ProbeCase(
            "CacheProbeType95",
            { BluetoothFormat.encodeToByteArray(CacheProbeType95.serializer(), CacheProbeType95(items = listOf(95, 96, 97))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType95.serializer(), bytes) == CacheProbeType95(items = listOf(95, 96, 97)) },
        ),
        ProbeCase(
            "CacheProbeType96",
            { BluetoothFormat.encodeToByteArray(CacheProbeType96.serializer(), CacheProbeType96(96007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType96.serializer(), bytes) == CacheProbeType96(96007) },
        ),
        ProbeCase(
            "CacheProbeType97",
            { BluetoothFormat.encodeToByteArray(CacheProbeType97.serializer(), CacheProbeType97(97007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType97.serializer(), bytes) == CacheProbeType97(97007u) },
        ),
        ProbeCase(
            "CacheProbeType98",
            { BluetoothFormat.encodeToByteArray(CacheProbeType98.serializer(), CacheProbeType98(a = true, b = false, c = 98)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType98.serializer(), bytes) == CacheProbeType98(a = true, b = false, c = 98) },
        ),
        ProbeCase(
            "CacheProbeType99",
            { BluetoothFormat.encodeToByteArray(CacheProbeType99.serializer(), CacheProbeType99(items = listOf(99, 100, 101))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType99.serializer(), bytes) == CacheProbeType99(items = listOf(99, 100, 101)) },
        ),
    ),
    listOf(
        ProbeCase(
            "CacheProbeType100",
            { BluetoothFormat.encodeToByteArray(CacheProbeType100.serializer(), CacheProbeType100(100007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType100.serializer(), bytes) == CacheProbeType100(100007) },
        ),
        ProbeCase(
            "CacheProbeType101",
            { BluetoothFormat.encodeToByteArray(CacheProbeType101.serializer(), CacheProbeType101(101007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType101.serializer(), bytes) == CacheProbeType101(101007u) },
        ),
        ProbeCase(
            "CacheProbeType102",
            { BluetoothFormat.encodeToByteArray(CacheProbeType102.serializer(), CacheProbeType102(a = true, b = true, c = 102)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType102.serializer(), bytes) == CacheProbeType102(a = true, b = true, c = 102) },
        ),
        ProbeCase(
            "CacheProbeType103",
            { BluetoothFormat.encodeToByteArray(CacheProbeType103.serializer(), CacheProbeType103(items = listOf(103, 104, 105))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType103.serializer(), bytes) == CacheProbeType103(items = listOf(103, 104, 105)) },
        ),
        ProbeCase(
            "CacheProbeType104",
            { BluetoothFormat.encodeToByteArray(CacheProbeType104.serializer(), CacheProbeType104(104007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType104.serializer(), bytes) == CacheProbeType104(104007) },
        ),
        ProbeCase(
            "CacheProbeType105",
            { BluetoothFormat.encodeToByteArray(CacheProbeType105.serializer(), CacheProbeType105(105007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType105.serializer(), bytes) == CacheProbeType105(105007u) },
        ),
        ProbeCase(
            "CacheProbeType106",
            { BluetoothFormat.encodeToByteArray(CacheProbeType106.serializer(), CacheProbeType106(a = true, b = false, c = 106)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType106.serializer(), bytes) == CacheProbeType106(a = true, b = false, c = 106) },
        ),
        ProbeCase(
            "CacheProbeType107",
            { BluetoothFormat.encodeToByteArray(CacheProbeType107.serializer(), CacheProbeType107(items = listOf(107, 108, 109))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType107.serializer(), bytes) == CacheProbeType107(items = listOf(107, 108, 109)) },
        ),
        ProbeCase(
            "CacheProbeType108",
            { BluetoothFormat.encodeToByteArray(CacheProbeType108.serializer(), CacheProbeType108(108007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType108.serializer(), bytes) == CacheProbeType108(108007) },
        ),
        ProbeCase(
            "CacheProbeType109",
            { BluetoothFormat.encodeToByteArray(CacheProbeType109.serializer(), CacheProbeType109(109007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType109.serializer(), bytes) == CacheProbeType109(109007u) },
        ),
        ProbeCase(
            "CacheProbeType110",
            { BluetoothFormat.encodeToByteArray(CacheProbeType110.serializer(), CacheProbeType110(a = true, b = false, c = 110)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType110.serializer(), bytes) == CacheProbeType110(a = true, b = false, c = 110) },
        ),
        ProbeCase(
            "CacheProbeType111",
            { BluetoothFormat.encodeToByteArray(CacheProbeType111.serializer(), CacheProbeType111(items = listOf(111, 112, 113))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType111.serializer(), bytes) == CacheProbeType111(items = listOf(111, 112, 113)) },
        ),
        ProbeCase(
            "CacheProbeType112",
            { BluetoothFormat.encodeToByteArray(CacheProbeType112.serializer(), CacheProbeType112(112007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType112.serializer(), bytes) == CacheProbeType112(112007) },
        ),
        ProbeCase(
            "CacheProbeType113",
            { BluetoothFormat.encodeToByteArray(CacheProbeType113.serializer(), CacheProbeType113(113007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType113.serializer(), bytes) == CacheProbeType113(113007u) },
        ),
        ProbeCase(
            "CacheProbeType114",
            { BluetoothFormat.encodeToByteArray(CacheProbeType114.serializer(), CacheProbeType114(a = true, b = true, c = 114)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType114.serializer(), bytes) == CacheProbeType114(a = true, b = true, c = 114) },
        ),
        ProbeCase(
            "CacheProbeType115",
            { BluetoothFormat.encodeToByteArray(CacheProbeType115.serializer(), CacheProbeType115(items = listOf(115, 116, 117))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType115.serializer(), bytes) == CacheProbeType115(items = listOf(115, 116, 117)) },
        ),
        ProbeCase(
            "CacheProbeType116",
            { BluetoothFormat.encodeToByteArray(CacheProbeType116.serializer(), CacheProbeType116(116007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType116.serializer(), bytes) == CacheProbeType116(116007) },
        ),
        ProbeCase(
            "CacheProbeType117",
            { BluetoothFormat.encodeToByteArray(CacheProbeType117.serializer(), CacheProbeType117(117007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType117.serializer(), bytes) == CacheProbeType117(117007u) },
        ),
        ProbeCase(
            "CacheProbeType118",
            { BluetoothFormat.encodeToByteArray(CacheProbeType118.serializer(), CacheProbeType118(a = true, b = false, c = 118)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType118.serializer(), bytes) == CacheProbeType118(a = true, b = false, c = 118) },
        ),
        ProbeCase(
            "CacheProbeType119",
            { BluetoothFormat.encodeToByteArray(CacheProbeType119.serializer(), CacheProbeType119(items = listOf(119, 120, 121))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType119.serializer(), bytes) == CacheProbeType119(items = listOf(119, 120, 121)) },
        ),
        ProbeCase(
            "CacheProbeType120",
            { BluetoothFormat.encodeToByteArray(CacheProbeType120.serializer(), CacheProbeType120(120007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType120.serializer(), bytes) == CacheProbeType120(120007) },
        ),
        ProbeCase(
            "CacheProbeType121",
            { BluetoothFormat.encodeToByteArray(CacheProbeType121.serializer(), CacheProbeType121(121007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType121.serializer(), bytes) == CacheProbeType121(121007u) },
        ),
        ProbeCase(
            "CacheProbeType122",
            { BluetoothFormat.encodeToByteArray(CacheProbeType122.serializer(), CacheProbeType122(a = true, b = false, c = 122)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType122.serializer(), bytes) == CacheProbeType122(a = true, b = false, c = 122) },
        ),
        ProbeCase(
            "CacheProbeType123",
            { BluetoothFormat.encodeToByteArray(CacheProbeType123.serializer(), CacheProbeType123(items = listOf(123, 124, 125))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType123.serializer(), bytes) == CacheProbeType123(items = listOf(123, 124, 125)) },
        ),
        ProbeCase(
            "CacheProbeType124",
            { BluetoothFormat.encodeToByteArray(CacheProbeType124.serializer(), CacheProbeType124(124007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType124.serializer(), bytes) == CacheProbeType124(124007) },
        ),
        ProbeCase(
            "CacheProbeType125",
            { BluetoothFormat.encodeToByteArray(CacheProbeType125.serializer(), CacheProbeType125(125007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType125.serializer(), bytes) == CacheProbeType125(125007u) },
        ),
        ProbeCase(
            "CacheProbeType126",
            { BluetoothFormat.encodeToByteArray(CacheProbeType126.serializer(), CacheProbeType126(a = true, b = true, c = 126)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType126.serializer(), bytes) == CacheProbeType126(a = true, b = true, c = 126) },
        ),
        ProbeCase(
            "CacheProbeType127",
            { BluetoothFormat.encodeToByteArray(CacheProbeType127.serializer(), CacheProbeType127(items = listOf(127, 0, 1))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType127.serializer(), bytes) == CacheProbeType127(items = listOf(127, 0, 1)) },
        ),
        ProbeCase(
            "CacheProbeType128",
            { BluetoothFormat.encodeToByteArray(CacheProbeType128.serializer(), CacheProbeType128(128007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType128.serializer(), bytes) == CacheProbeType128(128007) },
        ),
        ProbeCase(
            "CacheProbeType129",
            { BluetoothFormat.encodeToByteArray(CacheProbeType129.serializer(), CacheProbeType129(129007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType129.serializer(), bytes) == CacheProbeType129(129007u) },
        ),
        ProbeCase(
            "CacheProbeType130",
            { BluetoothFormat.encodeToByteArray(CacheProbeType130.serializer(), CacheProbeType130(a = true, b = false, c = 2)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType130.serializer(), bytes) == CacheProbeType130(a = true, b = false, c = 2) },
        ),
        ProbeCase(
            "CacheProbeType131",
            { BluetoothFormat.encodeToByteArray(CacheProbeType131.serializer(), CacheProbeType131(items = listOf(3, 4, 5))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType131.serializer(), bytes) == CacheProbeType131(items = listOf(3, 4, 5)) },
        ),
        ProbeCase(
            "CacheProbeType132",
            { BluetoothFormat.encodeToByteArray(CacheProbeType132.serializer(), CacheProbeType132(132007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType132.serializer(), bytes) == CacheProbeType132(132007) },
        ),
        ProbeCase(
            "CacheProbeType133",
            { BluetoothFormat.encodeToByteArray(CacheProbeType133.serializer(), CacheProbeType133(133007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType133.serializer(), bytes) == CacheProbeType133(133007u) },
        ),
        ProbeCase(
            "CacheProbeType134",
            { BluetoothFormat.encodeToByteArray(CacheProbeType134.serializer(), CacheProbeType134(a = true, b = false, c = 6)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType134.serializer(), bytes) == CacheProbeType134(a = true, b = false, c = 6) },
        ),
        ProbeCase(
            "CacheProbeType135",
            { BluetoothFormat.encodeToByteArray(CacheProbeType135.serializer(), CacheProbeType135(items = listOf(7, 8, 9))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType135.serializer(), bytes) == CacheProbeType135(items = listOf(7, 8, 9)) },
        ),
        ProbeCase(
            "CacheProbeType136",
            { BluetoothFormat.encodeToByteArray(CacheProbeType136.serializer(), CacheProbeType136(136007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType136.serializer(), bytes) == CacheProbeType136(136007) },
        ),
        ProbeCase(
            "CacheProbeType137",
            { BluetoothFormat.encodeToByteArray(CacheProbeType137.serializer(), CacheProbeType137(137007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType137.serializer(), bytes) == CacheProbeType137(137007u) },
        ),
        ProbeCase(
            "CacheProbeType138",
            { BluetoothFormat.encodeToByteArray(CacheProbeType138.serializer(), CacheProbeType138(a = true, b = true, c = 10)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType138.serializer(), bytes) == CacheProbeType138(a = true, b = true, c = 10) },
        ),
        ProbeCase(
            "CacheProbeType139",
            { BluetoothFormat.encodeToByteArray(CacheProbeType139.serializer(), CacheProbeType139(items = listOf(11, 12, 13))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType139.serializer(), bytes) == CacheProbeType139(items = listOf(11, 12, 13)) },
        ),
        ProbeCase(
            "CacheProbeType140",
            { BluetoothFormat.encodeToByteArray(CacheProbeType140.serializer(), CacheProbeType140(140007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType140.serializer(), bytes) == CacheProbeType140(140007) },
        ),
        ProbeCase(
            "CacheProbeType141",
            { BluetoothFormat.encodeToByteArray(CacheProbeType141.serializer(), CacheProbeType141(141007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType141.serializer(), bytes) == CacheProbeType141(141007u) },
        ),
        ProbeCase(
            "CacheProbeType142",
            { BluetoothFormat.encodeToByteArray(CacheProbeType142.serializer(), CacheProbeType142(a = true, b = false, c = 14)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType142.serializer(), bytes) == CacheProbeType142(a = true, b = false, c = 14) },
        ),
        ProbeCase(
            "CacheProbeType143",
            { BluetoothFormat.encodeToByteArray(CacheProbeType143.serializer(), CacheProbeType143(items = listOf(15, 16, 17))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType143.serializer(), bytes) == CacheProbeType143(items = listOf(15, 16, 17)) },
        ),
        ProbeCase(
            "CacheProbeType144",
            { BluetoothFormat.encodeToByteArray(CacheProbeType144.serializer(), CacheProbeType144(144007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType144.serializer(), bytes) == CacheProbeType144(144007) },
        ),
        ProbeCase(
            "CacheProbeType145",
            { BluetoothFormat.encodeToByteArray(CacheProbeType145.serializer(), CacheProbeType145(145007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType145.serializer(), bytes) == CacheProbeType145(145007u) },
        ),
        ProbeCase(
            "CacheProbeType146",
            { BluetoothFormat.encodeToByteArray(CacheProbeType146.serializer(), CacheProbeType146(a = true, b = false, c = 18)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType146.serializer(), bytes) == CacheProbeType146(a = true, b = false, c = 18) },
        ),
        ProbeCase(
            "CacheProbeType147",
            { BluetoothFormat.encodeToByteArray(CacheProbeType147.serializer(), CacheProbeType147(items = listOf(19, 20, 21))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType147.serializer(), bytes) == CacheProbeType147(items = listOf(19, 20, 21)) },
        ),
        ProbeCase(
            "CacheProbeType148",
            { BluetoothFormat.encodeToByteArray(CacheProbeType148.serializer(), CacheProbeType148(148007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType148.serializer(), bytes) == CacheProbeType148(148007) },
        ),
        ProbeCase(
            "CacheProbeType149",
            { BluetoothFormat.encodeToByteArray(CacheProbeType149.serializer(), CacheProbeType149(149007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType149.serializer(), bytes) == CacheProbeType149(149007u) },
        ),
    ),
    listOf(
        ProbeCase(
            "CacheProbeType150",
            { BluetoothFormat.encodeToByteArray(CacheProbeType150.serializer(), CacheProbeType150(a = true, b = true, c = 22)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType150.serializer(), bytes) == CacheProbeType150(a = true, b = true, c = 22) },
        ),
        ProbeCase(
            "CacheProbeType151",
            { BluetoothFormat.encodeToByteArray(CacheProbeType151.serializer(), CacheProbeType151(items = listOf(23, 24, 25))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType151.serializer(), bytes) == CacheProbeType151(items = listOf(23, 24, 25)) },
        ),
        ProbeCase(
            "CacheProbeType152",
            { BluetoothFormat.encodeToByteArray(CacheProbeType152.serializer(), CacheProbeType152(152007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType152.serializer(), bytes) == CacheProbeType152(152007) },
        ),
        ProbeCase(
            "CacheProbeType153",
            { BluetoothFormat.encodeToByteArray(CacheProbeType153.serializer(), CacheProbeType153(153007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType153.serializer(), bytes) == CacheProbeType153(153007u) },
        ),
        ProbeCase(
            "CacheProbeType154",
            { BluetoothFormat.encodeToByteArray(CacheProbeType154.serializer(), CacheProbeType154(a = true, b = false, c = 26)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType154.serializer(), bytes) == CacheProbeType154(a = true, b = false, c = 26) },
        ),
        ProbeCase(
            "CacheProbeType155",
            { BluetoothFormat.encodeToByteArray(CacheProbeType155.serializer(), CacheProbeType155(items = listOf(27, 28, 29))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType155.serializer(), bytes) == CacheProbeType155(items = listOf(27, 28, 29)) },
        ),
        ProbeCase(
            "CacheProbeType156",
            { BluetoothFormat.encodeToByteArray(CacheProbeType156.serializer(), CacheProbeType156(156007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType156.serializer(), bytes) == CacheProbeType156(156007) },
        ),
        ProbeCase(
            "CacheProbeType157",
            { BluetoothFormat.encodeToByteArray(CacheProbeType157.serializer(), CacheProbeType157(157007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType157.serializer(), bytes) == CacheProbeType157(157007u) },
        ),
        ProbeCase(
            "CacheProbeType158",
            { BluetoothFormat.encodeToByteArray(CacheProbeType158.serializer(), CacheProbeType158(a = true, b = false, c = 30)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType158.serializer(), bytes) == CacheProbeType158(a = true, b = false, c = 30) },
        ),
        ProbeCase(
            "CacheProbeType159",
            { BluetoothFormat.encodeToByteArray(CacheProbeType159.serializer(), CacheProbeType159(items = listOf(31, 32, 33))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType159.serializer(), bytes) == CacheProbeType159(items = listOf(31, 32, 33)) },
        ),
        ProbeCase(
            "CacheProbeType160",
            { BluetoothFormat.encodeToByteArray(CacheProbeType160.serializer(), CacheProbeType160(160007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType160.serializer(), bytes) == CacheProbeType160(160007) },
        ),
        ProbeCase(
            "CacheProbeType161",
            { BluetoothFormat.encodeToByteArray(CacheProbeType161.serializer(), CacheProbeType161(161007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType161.serializer(), bytes) == CacheProbeType161(161007u) },
        ),
        ProbeCase(
            "CacheProbeType162",
            { BluetoothFormat.encodeToByteArray(CacheProbeType162.serializer(), CacheProbeType162(a = true, b = true, c = 34)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType162.serializer(), bytes) == CacheProbeType162(a = true, b = true, c = 34) },
        ),
        ProbeCase(
            "CacheProbeType163",
            { BluetoothFormat.encodeToByteArray(CacheProbeType163.serializer(), CacheProbeType163(items = listOf(35, 36, 37))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType163.serializer(), bytes) == CacheProbeType163(items = listOf(35, 36, 37)) },
        ),
        ProbeCase(
            "CacheProbeType164",
            { BluetoothFormat.encodeToByteArray(CacheProbeType164.serializer(), CacheProbeType164(164007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType164.serializer(), bytes) == CacheProbeType164(164007) },
        ),
        ProbeCase(
            "CacheProbeType165",
            { BluetoothFormat.encodeToByteArray(CacheProbeType165.serializer(), CacheProbeType165(165007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType165.serializer(), bytes) == CacheProbeType165(165007u) },
        ),
        ProbeCase(
            "CacheProbeType166",
            { BluetoothFormat.encodeToByteArray(CacheProbeType166.serializer(), CacheProbeType166(a = true, b = false, c = 38)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType166.serializer(), bytes) == CacheProbeType166(a = true, b = false, c = 38) },
        ),
        ProbeCase(
            "CacheProbeType167",
            { BluetoothFormat.encodeToByteArray(CacheProbeType167.serializer(), CacheProbeType167(items = listOf(39, 40, 41))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType167.serializer(), bytes) == CacheProbeType167(items = listOf(39, 40, 41)) },
        ),
        ProbeCase(
            "CacheProbeType168",
            { BluetoothFormat.encodeToByteArray(CacheProbeType168.serializer(), CacheProbeType168(168007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType168.serializer(), bytes) == CacheProbeType168(168007) },
        ),
        ProbeCase(
            "CacheProbeType169",
            { BluetoothFormat.encodeToByteArray(CacheProbeType169.serializer(), CacheProbeType169(169007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType169.serializer(), bytes) == CacheProbeType169(169007u) },
        ),
        ProbeCase(
            "CacheProbeType170",
            { BluetoothFormat.encodeToByteArray(CacheProbeType170.serializer(), CacheProbeType170(a = true, b = false, c = 42)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType170.serializer(), bytes) == CacheProbeType170(a = true, b = false, c = 42) },
        ),
        ProbeCase(
            "CacheProbeType171",
            { BluetoothFormat.encodeToByteArray(CacheProbeType171.serializer(), CacheProbeType171(items = listOf(43, 44, 45))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType171.serializer(), bytes) == CacheProbeType171(items = listOf(43, 44, 45)) },
        ),
        ProbeCase(
            "CacheProbeType172",
            { BluetoothFormat.encodeToByteArray(CacheProbeType172.serializer(), CacheProbeType172(172007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType172.serializer(), bytes) == CacheProbeType172(172007) },
        ),
        ProbeCase(
            "CacheProbeType173",
            { BluetoothFormat.encodeToByteArray(CacheProbeType173.serializer(), CacheProbeType173(173007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType173.serializer(), bytes) == CacheProbeType173(173007u) },
        ),
        ProbeCase(
            "CacheProbeType174",
            { BluetoothFormat.encodeToByteArray(CacheProbeType174.serializer(), CacheProbeType174(a = true, b = true, c = 46)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType174.serializer(), bytes) == CacheProbeType174(a = true, b = true, c = 46) },
        ),
        ProbeCase(
            "CacheProbeType175",
            { BluetoothFormat.encodeToByteArray(CacheProbeType175.serializer(), CacheProbeType175(items = listOf(47, 48, 49))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType175.serializer(), bytes) == CacheProbeType175(items = listOf(47, 48, 49)) },
        ),
        ProbeCase(
            "CacheProbeType176",
            { BluetoothFormat.encodeToByteArray(CacheProbeType176.serializer(), CacheProbeType176(176007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType176.serializer(), bytes) == CacheProbeType176(176007) },
        ),
        ProbeCase(
            "CacheProbeType177",
            { BluetoothFormat.encodeToByteArray(CacheProbeType177.serializer(), CacheProbeType177(177007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType177.serializer(), bytes) == CacheProbeType177(177007u) },
        ),
        ProbeCase(
            "CacheProbeType178",
            { BluetoothFormat.encodeToByteArray(CacheProbeType178.serializer(), CacheProbeType178(a = true, b = false, c = 50)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType178.serializer(), bytes) == CacheProbeType178(a = true, b = false, c = 50) },
        ),
        ProbeCase(
            "CacheProbeType179",
            { BluetoothFormat.encodeToByteArray(CacheProbeType179.serializer(), CacheProbeType179(items = listOf(51, 52, 53))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType179.serializer(), bytes) == CacheProbeType179(items = listOf(51, 52, 53)) },
        ),
        ProbeCase(
            "CacheProbeType180",
            { BluetoothFormat.encodeToByteArray(CacheProbeType180.serializer(), CacheProbeType180(180007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType180.serializer(), bytes) == CacheProbeType180(180007) },
        ),
        ProbeCase(
            "CacheProbeType181",
            { BluetoothFormat.encodeToByteArray(CacheProbeType181.serializer(), CacheProbeType181(181007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType181.serializer(), bytes) == CacheProbeType181(181007u) },
        ),
        ProbeCase(
            "CacheProbeType182",
            { BluetoothFormat.encodeToByteArray(CacheProbeType182.serializer(), CacheProbeType182(a = true, b = false, c = 54)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType182.serializer(), bytes) == CacheProbeType182(a = true, b = false, c = 54) },
        ),
        ProbeCase(
            "CacheProbeType183",
            { BluetoothFormat.encodeToByteArray(CacheProbeType183.serializer(), CacheProbeType183(items = listOf(55, 56, 57))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType183.serializer(), bytes) == CacheProbeType183(items = listOf(55, 56, 57)) },
        ),
        ProbeCase(
            "CacheProbeType184",
            { BluetoothFormat.encodeToByteArray(CacheProbeType184.serializer(), CacheProbeType184(184007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType184.serializer(), bytes) == CacheProbeType184(184007) },
        ),
        ProbeCase(
            "CacheProbeType185",
            { BluetoothFormat.encodeToByteArray(CacheProbeType185.serializer(), CacheProbeType185(185007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType185.serializer(), bytes) == CacheProbeType185(185007u) },
        ),
        ProbeCase(
            "CacheProbeType186",
            { BluetoothFormat.encodeToByteArray(CacheProbeType186.serializer(), CacheProbeType186(a = true, b = true, c = 58)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType186.serializer(), bytes) == CacheProbeType186(a = true, b = true, c = 58) },
        ),
        ProbeCase(
            "CacheProbeType187",
            { BluetoothFormat.encodeToByteArray(CacheProbeType187.serializer(), CacheProbeType187(items = listOf(59, 60, 61))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType187.serializer(), bytes) == CacheProbeType187(items = listOf(59, 60, 61)) },
        ),
        ProbeCase(
            "CacheProbeType188",
            { BluetoothFormat.encodeToByteArray(CacheProbeType188.serializer(), CacheProbeType188(188007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType188.serializer(), bytes) == CacheProbeType188(188007) },
        ),
        ProbeCase(
            "CacheProbeType189",
            { BluetoothFormat.encodeToByteArray(CacheProbeType189.serializer(), CacheProbeType189(189007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType189.serializer(), bytes) == CacheProbeType189(189007u) },
        ),
        ProbeCase(
            "CacheProbeType190",
            { BluetoothFormat.encodeToByteArray(CacheProbeType190.serializer(), CacheProbeType190(a = true, b = false, c = 62)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType190.serializer(), bytes) == CacheProbeType190(a = true, b = false, c = 62) },
        ),
        ProbeCase(
            "CacheProbeType191",
            { BluetoothFormat.encodeToByteArray(CacheProbeType191.serializer(), CacheProbeType191(items = listOf(63, 64, 65))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType191.serializer(), bytes) == CacheProbeType191(items = listOf(63, 64, 65)) },
        ),
        ProbeCase(
            "CacheProbeType192",
            { BluetoothFormat.encodeToByteArray(CacheProbeType192.serializer(), CacheProbeType192(192007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType192.serializer(), bytes) == CacheProbeType192(192007) },
        ),
        ProbeCase(
            "CacheProbeType193",
            { BluetoothFormat.encodeToByteArray(CacheProbeType193.serializer(), CacheProbeType193(193007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType193.serializer(), bytes) == CacheProbeType193(193007u) },
        ),
        ProbeCase(
            "CacheProbeType194",
            { BluetoothFormat.encodeToByteArray(CacheProbeType194.serializer(), CacheProbeType194(a = true, b = false, c = 66)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType194.serializer(), bytes) == CacheProbeType194(a = true, b = false, c = 66) },
        ),
        ProbeCase(
            "CacheProbeType195",
            { BluetoothFormat.encodeToByteArray(CacheProbeType195.serializer(), CacheProbeType195(items = listOf(67, 68, 69))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType195.serializer(), bytes) == CacheProbeType195(items = listOf(67, 68, 69)) },
        ),
        ProbeCase(
            "CacheProbeType196",
            { BluetoothFormat.encodeToByteArray(CacheProbeType196.serializer(), CacheProbeType196(196007)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType196.serializer(), bytes) == CacheProbeType196(196007) },
        ),
        ProbeCase(
            "CacheProbeType197",
            { BluetoothFormat.encodeToByteArray(CacheProbeType197.serializer(), CacheProbeType197(197007u)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType197.serializer(), bytes) == CacheProbeType197(197007u) },
        ),
        ProbeCase(
            "CacheProbeType198",
            { BluetoothFormat.encodeToByteArray(CacheProbeType198.serializer(), CacheProbeType198(a = true, b = true, c = 70)) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType198.serializer(), bytes) == CacheProbeType198(a = true, b = true, c = 70) },
        ),
        ProbeCase(
            "CacheProbeType199",
            { BluetoothFormat.encodeToByteArray(CacheProbeType199.serializer(), CacheProbeType199(items = listOf(71, 72, 73))) },
            { bytes -> BluetoothFormat.decodeFromByteArray(CacheProbeType199.serializer(), bytes) == CacheProbeType199(items = listOf(71, 72, 73)) },
        ),
    ),
)

/**
 * Stress test of first-time encodes from many threads against the unsynchronised descriptor cache in
 * BluetoothBinaryDescriptorRegistry. It has not reproduced a failure locally.
 *
 * The registry is a singleton with no reset hook, so a type is only cold on its first encode; each iteration
 * therefore uses its own disjoint block of generated types.
 */
class BluetoothFormatConcurrentCacheProbeTest {

    @Test
    fun concurrentFirstTimeEncodesOfManyDistinctTypesMatchSingleThreaded() {
        val threadCount = 16
        allIterations().forEachIndexed { iterationIndex, cases ->
            val executor = Executors.newFixedThreadPool(threadCount)
            val startLatch = CountDownLatch(1)
            val results = ConcurrentHashMap<Int, ByteArray>()
            val errors = ConcurrentLinkedQueue<Throwable>()

            val tasks = cases.mapIndexed { idx, case ->
                executor.submit {
                    startLatch.await()
                    try {
                        results[idx] = case.encode()
                    } catch (e: Throwable) {
                        errors += e
                    }
                }
            }
            startLatch.countDown()
            tasks.forEach { it.get(30, TimeUnit.SECONDS) }
            executor.shutdown()

            assertTrue(
                errors.isEmpty(),
                "iteration $iterationIndex: concurrent first-time encode threw: " +
                    errors.joinToString { "${it::class.simpleName}(${it.message})" },
            )
            assertEquals(
                cases.size,
                results.size,
                "iteration $iterationIndex: only ${results.size}/${cases.size} concurrent encodes completed - lost/overwritten entries",
            )

            // Reference: re-encode single-threaded now that the registry cache is warm for these types, and
            // confirm every concurrently-produced result matches it and decodes back to the expected value.
            cases.forEachIndexed { idx, case ->
                val concurrentBytes = results.getValue(idx)
                val singleThreaded = case.encode()
                assertTrue(
                    concurrentBytes.contentEquals(singleThreaded),
                    "iteration $iterationIndex case $idx (${case.label}): concurrent encode ${concurrentBytes.toList()} " +
                        "!= single-threaded re-encode ${singleThreaded.toList()}",
                )
                assertTrue(
                    case.decodeMatches(concurrentBytes),
                    "iteration $iterationIndex case $idx (${case.label}): concurrently-encoded bytes did not decode back to the original value",
                )
            }
        }
    }
}
