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

@file:JvmName("AndroidStyledString")
package com.splendo.kaluga.resources

import com.splendo.kaluga.resources.stylable.TextAlignment
import com.splendo.kaluga.resources.stylable.TextStyle
import kotlin.jvm.JvmName

expect class StyledString {
    val defaultTextStyle: TextStyle
}

expect class StyledStringBuilder {
    class Provider {
        fun provide(string: String, defaultTextStyle: TextStyle): StyledStringBuilder
    }
    fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange)
    fun create(): StyledString
}

expect val StyledString.rawString: String

fun String.styled(provider: StyledStringBuilder.Provider, defaultTextStyle: TextStyle, vararg attributes: StringStyleAttribute) = styled(
    provider,
    defaultTextStyle,
    *attributes.map<StringStyleAttribute, String.() -> Pair<StringStyleAttribute, IntRange>?> { attribute ->
        { attributeSubstring(this, attribute) }
    }.toTypedArray()
)
fun String.styled(provider: StyledStringBuilder.Provider, defaultTextStyle: TextStyle, vararg attributes: String.() -> Pair<StringStyleAttribute, IntRange>?) = provider.provide(this, defaultTextStyle).apply {
    attributes.forEach { attribute ->
        attribute()?.let {
            addStyleAttribute(it.first, it.second)
        }
    }
}.create()
fun String.attributeSubstring(substring: String, attribute: StringStyleAttribute): Pair<StringStyleAttribute, IntRange>? {
    return indexOf(substring).let {
        if (it >= 0) {
            val range = IntRange(it, it + substring.length - 1)
            Pair(attribute, range)
        } else {
            null
        }
    }
}

sealed class StringStyleAttribute {
    sealed class CharacterStyleAttribute : StringStyleAttribute() {
        data class BackgroundColor(val color: Color) : CharacterStyleAttribute()
        data class ForegroundColor(val color: Color) : CharacterStyleAttribute()
        data class TextStyle(val textStyle: com.splendo.kaluga.resources.stylable.TextStyle) : CharacterStyleAttribute()
        data class Font(val font: com.splendo.kaluga.resources.Font, val size: Float) : CharacterStyleAttribute()
        data class Stroke(val width: Float, val color: Color) : CharacterStyleAttribute()
        data class Shadow(val color: Color, val xOffset: Float, val yOffset: Float, val blurRadius: Float) : CharacterStyleAttribute()
        data class Kerning(val kern: Float) : CharacterStyleAttribute()
        object Strikethrough : CharacterStyleAttribute()
        object Underline : CharacterStyleAttribute()
        object SuperScript : CharacterStyleAttribute()
        object SubScript : CharacterStyleAttribute()
    }

    sealed class ParagraphStyleAttribute : StringStyleAttribute() {
        data class LeadingIndent(val indent: Float, val firstLineIndent: Float = indent) : ParagraphStyleAttribute()
        data class LineSpacing(val spacing: Float, val paragraphSpacing: Float = 0.0f, val paragraphSpacingBefore: Float = 0.0f) : ParagraphStyleAttribute()
        data class Alignment(val alignment: TextAlignment) : ParagraphStyleAttribute()
    }

    data class Link(val url: String) : StringStyleAttribute()
}