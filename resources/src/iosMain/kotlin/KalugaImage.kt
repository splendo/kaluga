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

import platform.UIKit.UIImage
import platform.UIKit.UIImageRenderingMode

/**
 * Class describing an image.
 */
actual typealias KalugaImage = UIImage

/**
 * Gets the tinted [UIImage] of a [TintedImage]
 */
val TintedImage.uiImage: UIImage get() = image.imageWithTintColor(tint.uiColor, UIImageRenderingMode.UIImageRenderingModeAlwaysOriginal)
