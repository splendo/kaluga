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

@file:JvmName("AndroidFont")
package com.splendo.kaluga.resources

import android.graphics.Typeface

actual typealias Font = Typeface

actual val defaultFont: Font get() = Typeface.DEFAULT
actual val defaultBoldFont: Font get() = Typeface.DEFAULT_BOLD
actual val defaultItalicFont: Font get() = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
actual val defaultMonospaceFont: Font get() = Typeface.MONOSPACE
