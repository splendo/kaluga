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

expect class StringLoader {
    constructor()
    fun loadString(identifier: String): String
}

expect class ColorLoader {
    constructor()
    fun loadColor(identifier: String): Color?
}

expect class ImageLoader {
    constructor()
    fun loadImage(identifier: String): Image?
}

expect class FontLoader {
    constructor()
    suspend fun loadFont(identifier: String): Font?
}

fun String.localized(stringLoader: StringLoader = StringLoader()) = stringLoader.loadString(this)
fun String.asColor(colorLoader: ColorLoader = ColorLoader()): Color? = colorLoader.loadColor(this)
fun String.asImage(imageLoader: ImageLoader = ImageLoader()): Image? = imageLoader.loadImage(this)
suspend fun String.asFont(fontLoader: FontLoader = FontLoader()): Font? = fontLoader.loadFont(this)

fun colorFrom(hexString: String): Color? {
    return if (hexString.startsWith('#')) {
        val hexColor = hexString.substring(1).toLong(16)
        when (hexString.length) {
            9 -> {
                val alpha = hexColor ushr 24
                val red = (hexColor shr 16) and 0xFF
                val green = (hexColor shr 8) and 0xFF
                val blue = hexColor and 0xFF
                colorFrom(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())
            }
            7 -> {
                val red = hexColor ushr 16
                val green = (hexColor shr 8) and 0xFF
                val blue = hexColor and 0xFF
                colorFrom(red.toInt(), green.toInt(), blue.toInt())
            }
            else -> null
        }
    } else {
        null
    }
}
