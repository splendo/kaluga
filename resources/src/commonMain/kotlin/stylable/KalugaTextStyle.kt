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

import com.splendo.kaluga.resources.KalugaFont
import com.splendo.kaluga.resources.KalugaColor

/**
 * The style to apply to a text
 * @property font the [KalugaFont] with which to display the text
 * @property color the [KalugaColor] with which to display the text
 * @property size the size of the text in points
 * @property alignment the [KalugaTextAlignment] of the text
 */
data class KalugaTextStyle(val font: KalugaFont, val color: KalugaColor, val size: Float, val alignment: KalugaTextAlignment = KalugaTextAlignment.START)
