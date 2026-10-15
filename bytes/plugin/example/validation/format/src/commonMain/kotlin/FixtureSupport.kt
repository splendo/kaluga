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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Size
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/** A plain inline wrapper, used across the numeric and collection layouts to exercise inline-class fields. */
@Serializable
@JvmInline
value class ValueContainer<T>(val value: T)

/** An inline wrapper whose numeric content picks a flexible width, used across the numeric and collection layouts. */
@Serializable
@JvmInline
value class NumberValueContainer<T>(@Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val value: T)
