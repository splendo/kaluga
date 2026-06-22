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

package com.splendo.kaluga.base

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IOSVersionTest {

    @Test
    fun testMajorVersionDifference() {
        val newerVersion = IOSVersion(2, 3, 4)
        val olderVersion = IOSVersion(1, 9, 9)
        assertTrue(newerVersion > olderVersion)
        assertFalse(olderVersion > newerVersion)
    }

    @Test
    fun testMinorVersionDifference() {
        val newerVersion = IOSVersion(1, 3, 4)
        val olderVersion = IOSVersion(1, 2, 9)
        assertTrue(newerVersion > olderVersion)
        assertFalse(olderVersion > newerVersion)
    }

    @Test
    fun testPatchVersionDifference() {
        val newerVersion = IOSVersion(1, 2, 4)
        val olderVersion = IOSVersion(1, 2, 1)
        assertTrue(newerVersion > olderVersion)
        assertFalse(olderVersion > newerVersion)
    }

    @Test
    fun testEqualVersionDifference() {
        val version1 = IOSVersion(1, 2, 3)
        val version2 = IOSVersion(1, 2, 3)
        assertEquals(version1, version2)
        assertEquals(version2, version1)
    }
}
