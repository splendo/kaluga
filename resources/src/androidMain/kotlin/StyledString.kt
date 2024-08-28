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

@file:JvmName("StyledStringAndroidkt")

package com.splendo.kaluga.resources

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan
import android.text.style.LineHeightSpan
import android.text.style.MetricAffectingSpan
import android.text.style.ParagraphStyle
import android.text.style.StrikethroughSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.resources.stylable.KalugaTextStyle
import com.splendo.kaluga.resources.view.alignment
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * A text configured with [StringStyleAttribute]
 * @property spannable the [Spannable] styled according to some [StringStyleAttribute]
 * @property defaultTextStyle The [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range.
 * This may be partially overwritten (e.g. [StringStyleAttribute.CharacterStyleAttribute.ForegroundColor] may overwrite [KalugaTextStyle.color])
 * @property linkStyle The [LinkStyle] to apply when [StringStyleAttribute.Link] is applied.
 * When `null` the Theme default will be used
 */
actual class StyledString(val spannable: Spannable, actual val defaultTextStyle: KalugaTextStyle, actual val linkStyle: LinkStyle?)

/**
 * Gets the plain string of a [StyledString]
 */
actual val StyledString.rawString: String get() = spannable.toString()

/**
 * Builder for creating a [StyledString]
 * @param string the String to style
 * @param defaultTextStyle The [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range.
 * This may be partially overwritten (e.g. [StringStyleAttribute.CharacterStyleAttribute.ForegroundColor] may overwrite [KalugaTextStyle.color])
 * @param linkStyle The [LinkStyle] to apply when [StringStyleAttribute.Link] is applied.
 * When `null` the Theme default will be used
 * @param context the [Context] in which the [StyledString] will be displayed
 */
actual class StyledStringBuilder constructor(string: String, private val defaultTextStyle: KalugaTextStyle, private val linkStyle: LinkStyle?, private val context: Context) {

    /**
     * Provider for a [StyledStringBuilder]
     * @param context the [Context] in which any build [StyledString] will be displayed
     */
    actual class Provider(private val context: Context = ApplicationHolder.applicationContext) {

        /**
         * Provides a [StyledStringBuilder] to build a [StyledString] for a given text
         * @param string the text for which to build the [StyledString]
         * @param defaultTextStyle the [KalugaTextStyle] to apply when no [StringStyleAttribute] are set for a given range
         * @param linkStyle the [LinkStyle] to apply when [StringStyleAttribute.Link] is applied
         * @return the [StyledStringBuilder] to build a [StyledString] for [string]
         */
        actual fun provide(string: String, defaultTextStyle: KalugaTextStyle, linkStyle: LinkStyle?) = StyledStringBuilder(string, defaultTextStyle, linkStyle, context)
    }

    private class CustomCharacterStyle(val modify: TextPaint.() -> Unit) : CharacterStyle() {
        override fun updateDrawState(tp: TextPaint?) {
            tp?.modify()
        }
    }

    private val builder = SpannableString(string)

    /**
     * Adds a [StringStyleAttribute] for a given range
     * @param attribute the [StringStyleAttribute] to apply
     * @param range the [IntRange] at which to apply the style
     * @throws [IndexOutOfBoundsException] if [range] is out of bounds for the text to span
     */
    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        if (range.any { it !in builder.indices }) {
            throw IndexOutOfBoundsException("Attribute cannot be applied to $range")
        }
        builder.setSpan(
            when (attribute) {
                is StringStyleAttribute.CharacterStyleAttribute -> attribute.characterStyle(range)
                is StringStyleAttribute.ParagraphStyleAttribute -> attribute.paragraphStyle
                is StringStyleAttribute.Link -> CustomURLSpan(attribute.url, linkStyle)
            },
            range.first,
            range.last + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
    }

    private fun StringStyleAttribute.CharacterStyleAttribute.characterStyle(range: IntRange): CharacterStyle = when (this) {
        is StringStyleAttribute.CharacterStyleAttribute.BackgroundColor -> BackgroundColorSpan(
            color,
        )
        is StringStyleAttribute.CharacterStyleAttribute.ForegroundColor -> ForegroundColorSpan(
            color,
        )
        is StringStyleAttribute.CharacterStyleAttribute.Font -> CustomCharacterStyle {
            typeface = font
            textSize = size.spToPixel(context)
        }
        is StringStyleAttribute.CharacterStyleAttribute.Kerning -> CustomCharacterStyle {
            letterSpacing = kern
        }
        is StringStyleAttribute.CharacterStyleAttribute.TextStyle -> CustomCharacterStyle {
            typeface = textStyle.font
            textSize = textStyle.size.spToPixel(context)
            color = textStyle.color
        }
        is StringStyleAttribute.CharacterStyleAttribute.Shadow -> CustomCharacterStyle {
            setShadowLayer(
                blurRadius.spToPixel(context),
                xOffset.spToPixel(context),
                yOffset.spToPixel(context),
                color,
            )
        }
        is StringStyleAttribute.CharacterStyleAttribute.Strikethrough -> StrikethroughSpan()
        is StringStyleAttribute.CharacterStyleAttribute.SubScript -> SubscriptSpan()
        is StringStyleAttribute.CharacterStyleAttribute.SuperScript -> SuperscriptSpan()
        is StringStyleAttribute.CharacterStyleAttribute.Underline -> UnderlineSpan()
        is StringStyleAttribute.CharacterStyleAttribute.Stroke ->
            object : MetricAffectingSpan(), LineBackgroundSpan {
                override fun updateMeasureState(textPaint: TextPaint) {
                    textPaint.style = Paint.Style.FILL_AND_STROKE
                    textPaint.strokeWidth = width
                }

                override fun updateDrawState(tp: TextPaint?) {
                    val textPaint = tp ?: return
                    textPaint.style = Paint.Style.FILL
                    textPaint.strokeWidth = width
                }

                override fun drawBackground(
                    canvas: Canvas,
                    paint: Paint,
                    left: Int,
                    right: Int,
                    top: Int,
                    baseline: Int,
                    bottom: Int,
                    text: CharSequence,
                    start: Int,
                    end: Int,
                    lineNumber: Int,
                ) {
                    val strokePaint = TextPaint(paint).apply {
                        style = Paint.Style.STROKE
                        strokeWidth = width
                        color = this@characterStyle.color
                    }

                    if (start < range.first) {
                        val offset = paint.measureText(text, start, range.first)
                        canvas.drawText(
                            text,
                            range.first,
                            min(range.last + 1, end),
                            left.toFloat() + offset,
                            baseline.toFloat(),
                            strokePaint,
                        )
                    } else {
                        canvas.drawText(
                            text,
                            start,
                            min(range.last + 1, end),
                            left.toFloat(),
                            baseline.toFloat(),
                            strokePaint,
                        )
                    }
                }
            }
    }

    private val StringStyleAttribute.ParagraphStyleAttribute.paragraphStyle: ParagraphStyle
        get() = when (this) {
            is StringStyleAttribute.ParagraphStyleAttribute.LeadingIndent -> LeadingMarginSpan.Standard(
                firstLineIndent.spToPixel(context).toInt(),
                indent.spToPixel(context).toInt(),
            )
            is StringStyleAttribute.ParagraphStyleAttribute.LineSpacing -> object : LineHeightSpan {
                override fun chooseHeight(text: CharSequence, start: Int, end: Int, spanstartv: Int, lineHeight: Int, fm: Paint.FontMetricsInt?) {
                    val fontMetrics = fm ?: return
                    val halfLineSpacing = (spacing * 0.5f).spToPixel(context).roundToInt()
                    fontMetrics.descent = fontMetrics.bottom + halfLineSpacing
                    fontMetrics.ascent = fontMetrics.top - halfLineSpacing
                    if (paragraphSpacing > 0.0) {
                        if (text[end - 1] == '\n') {
                            fm.descent = fm.descent + paragraphSpacing.spToPixel(context).toInt()
                        }
                    }
                    if (paragraphSpacingBefore > 0.0) {
                        val sizedParagraphSpacing =
                            paragraphSpacingBefore.spToPixel(context).toInt()
                        if (start == 0) {
                            fm.ascent = fm.ascent - sizedParagraphSpacing
                        } else {
                            if (text[start - 1] == '\n') {
                                fm.ascent = fm.ascent - sizedParagraphSpacing
                            }
                        }
                    }
                }
            }
            is StringStyleAttribute.ParagraphStyleAttribute.Alignment -> AlignmentSpan.Standard(
                alignment.alignment(context),
            )
        }

    /**
     * Creates the [StyledString]
     * @return the created [StyledString]
     */
    actual fun create(): StyledString = StyledString(builder, defaultTextStyle, linkStyle)
}

private class CustomURLSpan(url: String, private val linkStyle: LinkStyle?) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        linkStyle?.let {
            ds.color = it.color
            ds.isUnderlineText = it.isUnderlined
        }
    }
}
