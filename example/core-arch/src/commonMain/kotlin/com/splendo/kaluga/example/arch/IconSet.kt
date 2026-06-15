/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.arch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import com.splendo.kaluga.example.arch.generated.resources.Res
import com.splendo.kaluga.example.arch.generated.resources.material_icons
import org.jetbrains.compose.resources.Font

/**
 * The glyphs and [FontFamily] used for the example's icon buttons. Defaults to plain Unicode symbols
 * rendered with the platform's default font ([DefaultIconSet]); a host whose default font lacks those
 * glyphs (notably web) can override [LocalIconSet] with a dedicated icon font and matching glyphs.
 */
data class IconSet(
    val back: String,
    val play: String,
    val pause: String,
    val stop: String,
    val repeat: String,
    val repeatOne: String,
    val rate: String,
    val volume: String,
    val fontFamily: FontFamily = FontFamily.Default,
)

val DefaultIconSet: IconSet = IconSet(
    back = "<",
    play = "▶",
    pause = "⏸",
    stop = "⏹",
    repeat = "↻",
    repeatOne = "↺",
    rate = "»",
    volume = "🔊",
)

val LocalIconSet = staticCompositionLocalOf { DefaultIconSet }

/**
 * An [IconSet] backed by the bundled Material Icons font, with the buttons' glyphs as Material ligatures
 * (e.g. `play_arrow`). Used to override [LocalIconSet] so the icon buttons render consistently on every
 * platform rather than relying on the default font's coverage of the Unicode symbols in [DefaultIconSet].
 */
@Composable
fun rememberMaterialIconSet(): IconSet {
    val fontFamily = FontFamily(Font(Res.font.material_icons))
    return remember(fontFamily) {
        IconSet(
            back = "arrow_back",
            play = "play_arrow",
            pause = "pause",
            stop = "stop",
            repeat = "repeat",
            repeatOne = "repeat_one",
            rate = "speed",
            volume = "volume_up",
            fontFamily = fontFamily,
        )
    }
}
