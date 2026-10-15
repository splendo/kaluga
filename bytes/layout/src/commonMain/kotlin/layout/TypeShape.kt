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

package com.splendo.kaluga.bytes.layout

/**
 * The kind of a [TypeShape], covering the distinctions [LayoutCompiler] makes.
 */
enum class TypeKind {
    BOOLEAN,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    CHAR,
    STRING,
    ENUM,

    /** A pass-through wrapper, e.g. the `value` element of a sealed hierarchy that holds its subtypes. */
    CONTEXTUAL,

    /** A class or object with named elements. */
    STRUCTURE,
    LIST,
    MAP,
    SEALED,
    OPEN,
    ;

    /** `true` for [STRUCTURE], [LIST] and [MAP]. */
    val isStructure: Boolean get() = this == STRUCTURE || this == LIST || this == MAP

    /** `true` for [SEALED] and [OPEN]. */
    val isPolymorphic: Boolean get() = this == SEALED || this == OPEN

    /** `true` for the integer and floating point kinds. */
    val isNumeric: Boolean get() = this == BYTE || this == SHORT || this == INT || this == LONG || this == FLOAT || this == DOUBLE
}

/**
 * The shape of a type as [LayoutCompiler] sees it.
 *
 * It follows the element structure kotlinx.serialization generates for a type, so that a shape derived from a
 * `SerialDescriptor` at runtime and one derived from source symbols at compile time compile to the same layout:
 * - an inline (value) class has a single element holding the wrapped type;
 * - a [TypeKind.SEALED] type has a `type` element and a `value` element of kind [TypeKind.CONTEXTUAL] whose
 *   elements are the subtypes;
 * - a [TypeKind.LIST] has one element (the item), a [TypeKind.MAP] two (key and value);
 * - a [TypeKind.ENUM] has one element per entry.
 */
@InternalBytesApi
interface TypeShape {
    val serialName: String
    val kind: TypeKind
    val isNullable: Boolean
    val isInline: Boolean
    val annotations: List<Annotation>
    val elementsCount: Int

    fun elementName(index: Int): String
    fun elementAnnotations(index: Int): List<Annotation>
    fun element(index: Int): TypeShape

    /**
     * The registered subtypes of a [TypeKind.OPEN] type, in registration order.
     * Sealed subtypes are reached through the elements instead.
     */
    fun openSubtypes(): List<TypeShape>
}
