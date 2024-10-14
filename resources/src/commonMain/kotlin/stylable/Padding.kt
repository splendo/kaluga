/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.resources.stylable

/**
 * Padding to apply to a [com.splendo.kaluga.resources.view.KalugaButton]
 * Respects layouts based on reading direction
 * @property start the padding at the start of the view (left or right depending on reading direction)
 * @property end the padding at the end of the view (right or left depending on reading direction)
 * @property top the padding at the top of the view
 * @property bottom the padding at the bottom of the view
 */
data class Padding(val start: Float = 0.0f, val end: Float = 0.0f, val top: Float = 0.0f, val bottom: Float = 0.0f) {

    companion object {
        /**
         * The [Padding] that is applied by default to a [com.splendo.kaluga.resources.view.KalugaButton]
         */
        val defaultButtonPadding = Padding(horizontal = 16.0f, vertical = 8.0f)
    }

    /**
     * Creates a [Padding] where [Padding.start] and [Padding.end] are equal to [horizontal] and [Padding.top] and [Padding.bottom] are equal to [vertical]
     * @param horizontal the padding to apply for [Padding.start] and [Padding.end]
     * @param vertical the padding to apply for [Padding.top] and [Padding.bottom]
     */
    constructor(horizontal: Float = 0.0f, vertical: Float = 0.0f) : this(horizontal, horizontal, vertical, vertical)

    /**
     * Creates a [Padding] where all sides are equal to [padding]
     * @param padding the padding to apply to all sides
     */
    constructor(padding: Float) : this(padding, padding, padding, padding)
}
