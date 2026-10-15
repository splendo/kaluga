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

package com.splendo.kaluga.bluetooth.device

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class IdentifierTest {

    // RFC 4122 predefined namespaces.
    private val dnsNamespace = uuidBytes("6ba7b810-9dad-11d1-80b4-00c04fd430c8")
    private val urlNamespace = uuidBytes("6ba7b811-9dad-11d1-80b4-00c04fd430c8")

    @Test
    fun matchesRfc4122DnsVectorForWwwExample() {
        // uuid5(NAMESPACE_DNS, "www.example.com")
        val identifier = nameBasedIdentifier(dnsNamespace, "www.example.com")
        assertEquals("2ed6657d-e927-568b-95e1-2665a8aea6a2", identifier.stringValue.lowercase())
    }

    @Test
    fun matchesRfc4122DnsVectorForPythonOrg() {
        // uuid5(NAMESPACE_DNS, "python.org")
        val identifier = nameBasedIdentifier(dnsNamespace, "python.org")
        assertEquals("886313e1-3b8a-5372-9b90-0c9aee199e5d", identifier.stringValue.lowercase())
    }

    @Test
    fun isDeterministicForSameNamespaceAndName() {
        val first = nameBasedIdentifier(dnsNamespace, "www.example.com")
        val second = nameBasedIdentifier(dnsNamespace, "www.example.com")
        assertEquals(first.stringValue.lowercase(), second.stringValue.lowercase())
    }

    @Test
    fun differsByName() {
        val a = nameBasedIdentifier(dnsNamespace, "www.example.com")
        val b = nameBasedIdentifier(dnsNamespace, "www.example.org")
        assertNotEquals(a.stringValue.lowercase(), b.stringValue.lowercase())
    }

    @Test
    fun differsByNamespace() {
        val fromDns = nameBasedIdentifier(dnsNamespace, "www.example.com")
        val fromUrl = nameBasedIdentifier(urlNamespace, "www.example.com")
        assertNotEquals(fromDns.stringValue.lowercase(), fromUrl.stringValue.lowercase())
    }

    @Test
    fun stampsVersion5AndRfc4122Variant() {
        val value = nameBasedIdentifier(dnsNamespace, "www.example.com").stringValue.lowercase()
        // Layout: xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx
        assertEquals('5', value[14], "version nibble should be 5")
        assertTrue(value[19] in "89ab", "variant nibble should be one of 8, 9, a or b but was '${value[19]}'")
    }

    @Test
    fun handlesEmptyName() {
        val first = nameBasedIdentifier(dnsNamespace, "")
        val second = nameBasedIdentifier(dnsNamespace, "")
        assertEquals(first.stringValue.lowercase(), second.stringValue.lowercase())
        assertEquals('5', first.stringValue.lowercase()[14])
    }

    private fun uuidBytes(uuid: String): ByteArray {
        val hex = uuid.replace("-", "")
        return ByteArray(hex.length / 2) { i ->
            hex.substring(i * 2, i * 2 + 2).toInt(16).toByte()
        }
    }
}
