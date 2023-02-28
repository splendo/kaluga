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

package com.splendo.kaluga.resources.stylable

/**
 * Alignment at which a text is located
 */
enum class KalugaTextAlignment {

    /**
     * Alignment at the left side of the view (independent of reading direction)
     */
    LEFT,

    /**
     * Alignment at the right side of the view (independent of reading direction)
     */
    RIGHT,

    /**
     * Alignment at the start side of the view according to reading direction
     */
    END,

    /**
     * Alignment at the end side of the view according to reading direction
     */
    START,

    /**
     * Alignment at the center of the view
     */
    CENTER
}
