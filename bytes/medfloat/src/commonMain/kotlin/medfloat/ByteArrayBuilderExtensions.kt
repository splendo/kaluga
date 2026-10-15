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

import com.splendo.kaluga.bytes.ByteArrayBuilder

/**
 * Adds a [MedFloat16] to the array.
 * @param medFloat16 The [MedFloat16] to add.
 */
fun ByteArrayBuilder.add(medFloat16: MedFloat16) = add(medFloat16.toByteArray())

/**
 * Adds a [MedFloat32] to the array.
 * @param medFloat32 The [MedFloat32] to add.
 */
fun ByteArrayBuilder.add(medFloat32: MedFloat32) = add(medFloat32.toByteArray())
