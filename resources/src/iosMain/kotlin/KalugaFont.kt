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

package com.splendo.kaluga.resources

import platform.UIKit.UIFont
import platform.UIKit.UIFontWeightRegular
import platform.UIKit.labelFontSize

/**
 * Class describing a font
 */
actual typealias KalugaFont = UIFont

/**
 * The default system [KalugaFont]
 */
actual val defaultFont: KalugaFont get() = UIFont.systemFontOfSize(UIFont.labelFontSize)

/**
 * The default bold system [KalugaFont]
 */
actual val defaultBoldFont: KalugaFont get() = UIFont.boldSystemFontOfSize(UIFont.labelFontSize)

/**
 * The default italic system [KalugaFont]
 */
actual val defaultItalicFont: KalugaFont get() = UIFont.italicSystemFontOfSize(UIFont.labelFontSize)

/**
 * The default monospace system [KalugaFont]
 */
actual val defaultMonospaceFont: KalugaFont get() = UIFont.monospacedSystemFontOfSize(UIFont.labelFontSize, UIFontWeightRegular)
