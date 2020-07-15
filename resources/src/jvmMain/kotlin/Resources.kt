/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands
 
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

actual class StringLoader(private val transformer: (String) -> String) {
    actual constructor() : this({ it })
    actual fun loadString(identifier: String): String = transformer(identifier)
}

actual class ColorLoader(private val transformer: (String) -> Color?) {
    actual constructor() : this({ null })
    actual fun loadColor(identifier: String): Color? = transformer(identifier)
}

actual class ImageLoader(private val transformer: (String) -> Image?) {
    actual constructor() : this({ null })
    actual fun loadImage(identifier: String): Image? = transformer(identifier)
}

actual class FontLoader(private val transformer: suspend (String) -> Font?) {
    actual constructor() : this({ null })
    actual suspend fun loadFont(identifier: String): Font? = transformer(identifier)
}
