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
 * lookup over named tokens rather than guessing a width or type out of the token text.
 *
 * The schema enumerates tokens only; it carries no machine-readable widths or types. [bits] is the fixed wire width
 * implied by each token (per the GATT Specification Supplement / SIG Format Types), used at parse time to lay out
 * multi-`Field` flag regions and to rank flag-selected width alternatives; [kind] is how the token maps to a generated
 * scalar field. Variable-length and structural formats have no fixed width (0) and no scalar mapping ([Kind.STRUCTURED]).
 */
internal enum class GattFormat(val token: String, val bits: Int = 0, val kind: Kind = Kind.STRUCTURED) {
    BOOLEAN("boolean", kind = Kind.BOOLEAN),
    TWO_BIT("2bit", 2, Kind.UNSIGNED_INTEGER),
    FOUR_BIT("4bit", 4, Kind.UNSIGNED_INTEGER),
    NIBBLE("nibble", 4, Kind.UNSIGNED_INTEGER),
    EIGHT_BIT("8bit", 8, Kind.UNSIGNED_INTEGER),
    SIXTEEN_BIT("16bit", 16, Kind.UNSIGNED_INTEGER),
    TWENTY_FOUR_BIT("24bit", 24, Kind.UNSIGNED_INTEGER),
    THIRTY_TWO_BIT("32bit", 32, Kind.UNSIGNED_INTEGER),
    UINT8("uint8", 8, Kind.UNSIGNED_INTEGER),
    UINT8_ARRAY("uint8[]"),
    UINT12("uint12", 12, Kind.UNSIGNED_INTEGER),
    UINT16("uint16", 16, Kind.UNSIGNED_INTEGER),
    UINT24("uint24", 24, Kind.UNSIGNED_INTEGER),
    UINT32("uint32", 32, Kind.UNSIGNED_INTEGER),
    UINT40("uint40", 40, Kind.UNSIGNED_INTEGER),
    UINT48("uint48", 48, Kind.UNSIGNED_INTEGER),
    UINT64("uint64", 64, Kind.UNSIGNED_INTEGER),
    UINT128("uint128", 128, Kind.UNSIGNED_INTEGER),
    SINT8("sint8", 8, Kind.SIGNED_INTEGER),
    SINT12("sint12", 12, Kind.SIGNED_INTEGER),
    SINT16("sint16", 16, Kind.SIGNED_INTEGER),
    SINT24("sint24", 24, Kind.SIGNED_INTEGER),
    SINT32("sint32", 32, Kind.SIGNED_INTEGER),
    SINT48("sint48", 48, Kind.SIGNED_INTEGER),
    SINT64("sint64", 64, Kind.SIGNED_INTEGER),
    SINT128("sint128", 128, Kind.SIGNED_INTEGER),
    FLOAT32("float32", 32, Kind.IEEE_FLOAT),
    FLOAT64("float64", 64, Kind.IEEE_FLOAT),
    SFLOAT("SFLOAT", 16, Kind.MEDICAL_FLOAT),
    FLOAT("FLOAT", 32, Kind.MEDICAL_FLOAT),
    DUINT16("duint16", 16),
    UTF8S("utf8s", kind = Kind.UTF8),
    UTF16S("utf16s", kind = Kind.UTF16),
    CHARACTERISTIC("characteristic"),
    STRUCT("struct"),
    REG_CERT_DATA_LIST("reg-cert-data-list"),
    GATT_UUID("gatt_uuid"),
    VARIABLE("variable"),
    ;

    /**
     * How a `<Format>` token maps to a generated scalar field. [STRUCTURED] covers the aggregate/opaque formats the
     * generator does not map to a scalar value (byte arrays, references, structs, and the otherwise-unsupported tokens).
     */
    enum class Kind { BOOLEAN, UTF8, UTF16, MEDICAL_FLOAT, IEEE_FLOAT, UNSIGNED_INTEGER, SIGNED_INTEGER, STRUCTURED }

    companion object {
        private val byToken = entries.associateBy { it.token }

        /** The [GattFormat] for a `<Format>` [token], or null if it is not a recognised SIG format. */
        fun of(token: String): GattFormat? = byToken[token]
    }
}
