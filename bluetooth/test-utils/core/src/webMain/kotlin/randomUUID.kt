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

package com.splendo.kaluga.bluetooth.test

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidFrom

private val hexAlphabet = "0123456789abcdef"

private fun randomBlock(length: Int): String = buildString { repeat(length) { append(hexAlphabet.random()) } }

actual fun randomUUID(): UUID = uuidFrom("${randomBlock(8)}-${randomBlock(4)}-${randomBlock(4)}-${randomBlock(4)}-${randomBlock(12)}")
