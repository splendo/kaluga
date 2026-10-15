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
 * Marks what the Kaluga bytes modules and the code the bytes plugin generates call among themselves: the layout
 * compiler and its dumps, the numeric codec, and the runtime of generated views. It follows the generated code, not
 * compatibility, so it may change in any release; `BytesFormat`, the annotations, the generated DATA classes and
 * views, and the exceptions they throw are the public API.
 */
@RequiresOptIn(message = "An internal API of the Kaluga bytes modules, called by generated code; it may change in any release", level = RequiresOptIn.Level.ERROR)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR)
annotation class InternalBytesApi
