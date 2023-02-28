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

package com.splendo.kaluga.resources.uikit

import com.splendo.kaluga.resources.stylable.KalugaTextAlignment
import platform.UIKit.NSTextAlignment
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.NSTextAlignmentLeft
import platform.UIKit.NSTextAlignmentNatural
import platform.UIKit.NSTextAlignmentRight
import platform.UIKit.UIApplication
import platform.UIKit.UIControlContentHorizontalAlignment
import platform.UIKit.UIControlContentHorizontalAlignmentCenter
import platform.UIKit.UIControlContentHorizontalAlignmentLeading
import platform.UIKit.UIControlContentHorizontalAlignmentLeft
import platform.UIKit.UIControlContentHorizontalAlignmentRight
import platform.UIKit.UIControlContentHorizontalAlignmentTrailing
import platform.UIKit.UIUserInterfaceLayoutDirection

/**
 * Gets the [NSTextAlignment] of a [KalugaTextAlignment]
 */
val KalugaTextAlignment.nsTextAlignment: NSTextAlignment get() = when (this) {
    KalugaTextAlignment.LEFT -> NSTextAlignmentLeft
    KalugaTextAlignment.RIGHT -> NSTextAlignmentRight
    KalugaTextAlignment.CENTER -> NSTextAlignmentCenter
    KalugaTextAlignment.END -> if (UIApplication.sharedApplication.userInterfaceLayoutDirection == UIUserInterfaceLayoutDirection.UIUserInterfaceLayoutDirectionLeftToRight) {
        NSTextAlignmentRight
    } else {
        NSTextAlignmentLeft
    }
    KalugaTextAlignment.START -> NSTextAlignmentNatural
}

/**
 * Gets the [UIControlContentHorizontalAlignment] of a [KalugaTextAlignment]
 */
val KalugaTextAlignment.contentHorizontalAlignment: UIControlContentHorizontalAlignment get() = when (this) {
    KalugaTextAlignment.LEFT -> UIControlContentHorizontalAlignmentLeft
    KalugaTextAlignment.RIGHT -> UIControlContentHorizontalAlignmentRight
    KalugaTextAlignment.CENTER -> UIControlContentHorizontalAlignmentCenter
    KalugaTextAlignment.END -> UIControlContentHorizontalAlignmentTrailing
    KalugaTextAlignment.START -> UIControlContentHorizontalAlignmentLeading
}
