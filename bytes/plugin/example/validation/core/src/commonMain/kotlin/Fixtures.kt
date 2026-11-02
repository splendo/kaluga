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

package com.splendo.kaluga.bytes.example

import com.splendo.kaluga.bytes.annotations.BytesLayout

/**
 * Trivial fixture proving the `com.splendo.kaluga.bytes.plugin` wiring compiles with the default configuration: KSP
 * applies, finds this interface and generates its DATA class and view.
 */
@BytesLayout
interface CountFixture {
    val count: Int
}
