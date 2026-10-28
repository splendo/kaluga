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

package com.splendo.kaluga.bytes.annotations

/**
 * The code the `com.splendo.kaluga.bytes.plugin` KSP processor generates for a [BytesLayout]-annotated interface.
 */
enum class Generate {
    /** A `@Serializable` data class implementing the interface, encoded/decoded via `BytesFormat`. */
    DATA,

    /** A zero-copy `@JvmInline value class` implementing the interface directly over a [ByteArray]. */
    VIEW,
}

/**
 * Marks an interface as a byte-packed wire layout. The `com.splendo.kaluga.bytes.plugin` generates an
 * implementation for each entry in [generate] from the interface's properties and their `BytesFormat` annotations
 * ([Prefix], [Size], [ByteOrder], [Checksum], and the rest of this package).
 *
 * @property generate which implementations to generate for this interface. Defaults to the module-wide `bytes { }`
 *   DSL setting; set explicitly to override it for a single interface.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class BytesLayout(val generate: Array<Generate> = [Generate.DATA, Generate.VIEW])
