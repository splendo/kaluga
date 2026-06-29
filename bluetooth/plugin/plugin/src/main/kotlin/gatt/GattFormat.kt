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

package com.splendo.kaluga.bluetooth.plugin.gatt

/**
 * The Bluetooth SIG GATT field `<Format>` tokens — the closed set enumerated by the SIG schema (`formats.xsd`, which
 * `characteristic.xsd` includes; each definition references the latter via `xsi:noNamespaceSchemaLocation`). The full
 * 38-token list and order here mirror that `<xs:simpleType name="formats">` enumeration, so format handling is a
 * lookup over named tokens rather than guessing a width out of the token text (e.g. mistaking the `8` in `utf8s`).
 *
 * The schema enumerates tokens only; it carries no machine-readable widths. [bits] is the fixed wire width implied by
 * each token (per the GATT Specification Supplement / SIG Format Types), used at parse time to lay out multi-`Field`
 * flag regions and to rank flag-selected width alternatives. Variable-length and structural formats (the strings,
 * `uint8[]`, `characteristic`, `struct`, `gatt_uuid`, `reg-cert-data-list`, `variable`) have no fixed width and report 0.
 */
internal enum class GattFormat(val token: String, val bits: Int = 0) {
    BOOLEAN("boolean"),
    TWO_BIT("2bit", 2),
    FOUR_BIT("4bit", 4),
    NIBBLE("nibble", 4),
    EIGHT_BIT("8bit", 8),
    SIXTEEN_BIT("16bit", 16),
    TWENTY_FOUR_BIT("24bit", 24),
    THIRTY_TWO_BIT("32bit", 32),
    UINT8("uint8", 8),
    UINT8_ARRAY("uint8[]"),
    UINT12("uint12", 12),
    UINT16("uint16", 16),
    UINT24("uint24", 24),
    UINT32("uint32", 32),
    UINT40("uint40", 40),
    UINT48("uint48", 48),
    UINT64("uint64", 64),
    UINT128("uint128", 128),
    SINT8("sint8", 8),
    SINT12("sint12", 12),
    SINT16("sint16", 16),
    SINT24("sint24", 24),
    SINT32("sint32", 32),
    SINT48("sint48", 48),
    SINT64("sint64", 64),
    SINT128("sint128", 128),
    FLOAT32("float32", 32),
    FLOAT64("float64", 64),
    SFLOAT("SFLOAT", 16),
    FLOAT("FLOAT", 32),
    DUINT16("duint16", 16),
    UTF8S("utf8s"),
    UTF16S("utf16s"),
    CHARACTERISTIC("characteristic"),
    STRUCT("struct"),
    REG_CERT_DATA_LIST("reg-cert-data-list"),
    GATT_UUID("gatt_uuid"),
    VARIABLE("variable"),
    ;

    companion object {
        private val byToken = entries.associateBy { it.token }

        /** The [GattFormat] for a `<Format>` [token], or null if it is not a recognised SIG format. */
        fun of(token: String): GattFormat? = byToken[token]
    }
}
