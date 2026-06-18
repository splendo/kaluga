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

package com.splendo.kaluga.base.decimal

// Typed handle to a JavaScript [BigInt](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/BigInt),
// with each operation as a sole-body `js(...)` helper. This avoids `dynamic` (unavailable on Kotlin/Wasm)
// so [BigDecimal] can be shared by the whole JS family from `webMain`.
internal external interface JsBigInt

internal fun bigIntOf(value: String): JsBigInt = js("BigInt(value)")
internal fun bigIntOf(value: Int): JsBigInt = js("BigInt(value)")
internal fun bigIntAdd(a: JsBigInt, b: JsBigInt): JsBigInt = js("a + b")
internal fun bigIntSubtract(a: JsBigInt, b: JsBigInt): JsBigInt = js("a - b")
internal fun bigIntMultiply(a: JsBigInt, b: JsBigInt): JsBigInt = js("a * b")
internal fun bigIntDivide(a: JsBigInt, b: JsBigInt): JsBigInt = js("a / b")
internal fun bigIntRemainder(a: JsBigInt, b: JsBigInt): JsBigInt = js("a % b")
internal fun bigIntNegate(a: JsBigInt): JsBigInt = js("-a")
internal fun bigIntLessThan(a: JsBigInt, b: JsBigInt): Boolean = js("a < b")
internal fun bigIntGreaterThan(a: JsBigInt, b: JsBigInt): Boolean = js("a > b")
internal fun bigIntStrictEquals(a: JsBigInt, b: JsBigInt): Boolean = js("a === b")
internal fun bigIntToString(a: JsBigInt): String = js("a.toString()")
