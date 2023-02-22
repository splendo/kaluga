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

@file:JvmName("KalugaImageJVMKt")

package com.splendo.kaluga.resources

/**
 * Class describing an image.
 */
actual class KalugaImage

/**
 * Attempts to create a new [KalugaImage] that is tinted in a given [KalugaColor]
 * @param color The [KalugaColor] to use for tinting.
 * @return The tinted [KalugaImage] or `null` if tinting could not be applied.
 */
actual fun KalugaImage.tinted(color: KalugaColor): KalugaImage? = null
