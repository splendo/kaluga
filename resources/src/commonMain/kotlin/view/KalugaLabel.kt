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

package com.splendo.kaluga.resources.view

import com.splendo.kaluga.resources.StyledString
import com.splendo.kaluga.resources.stylable.KalugaTextStyle

/**
 * A label a [KalugaTextStyle] applied
 */
sealed class KalugaLabel {

    /**
     * The [KalugaTextStyle] of the label
     */
    abstract val style: KalugaTextStyle

    /**
     * A [KalugaLabel] that displays a regular text
     * @property text the text to display on the label
     * @param style the [KalugaTextStyle] of the label
     */
    data class Plain(val text: String, override val style: KalugaTextStyle) : KalugaLabel()

    /**
     * A [KalugaLabel] that displays a [StyledString]
     * @property text the [StyledString] to display on the label
     * @param style the [KalugaTextStyle] of the label
     */
    data class Styled(val text: StyledString, override val style: KalugaTextStyle = text.defaultTextStyle) : KalugaLabel()
}
