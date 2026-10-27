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

package com.splendo.kaluga.bytes.medfloat

// Whether the bytes from [index] are [pattern], which the caller has checked fit.
internal fun ByteArray.matchesAt(index: Int, pattern: ByteArray): Boolean {
    for (offset in pattern.indices) {
        if (this[index + offset] != pattern[offset]) return false
    }
    return true
}
