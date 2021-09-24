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

package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.resources.stylable.TextStyle
import com.splendo.kaluga.resources.view.KalugaLabel
import platform.UIKit.UILabel
import platform.UIKit.UITextField

fun UILabel.bindLabel(label: KalugaLabel<*>) {
    when (label) {
        is KalugaLabel.Plain -> text = label.text
        is KalugaLabel.Styled -> attributedText = label.text
    }
    applyTextStyle(label.style)
}

fun UILabel.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
}

fun UITextField.applyTextStyle(textStyle: TextStyle) {
    setFont(textStyle.font.fontWithSize(textStyle.size.toDouble()))
    textColor = textStyle.color.uiColor
    textAlignment = textStyle.alignment.nsTextAlignment
}
