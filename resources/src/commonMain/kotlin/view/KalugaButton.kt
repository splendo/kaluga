/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.resources.stylable.ButtonStyle

sealed class KalugaButton<T> {
    abstract val text: T
    abstract val style: ButtonStyle
    abstract val isEnabled: Boolean
    abstract val action: () -> Unit
    data class Plain(
        override val text: String,
        override val style: ButtonStyle,
        override val isEnabled: Boolean = true,
        override val action: () -> Unit
    ) : KalugaButton<String>()

    data class Styled(
        override val text: StyledString,
        override val style: ButtonStyle,
        override val isEnabled: Boolean = true,
        override val action: () -> Unit
    ) : KalugaButton<StyledString>()
}
