package com.splendo.kaluga.resources

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
import android.text.style.LineHeightSpan
import android.text.style.ParagraphStyle
import android.text.style.ReplacementSpan
import android.text.style.StrikethroughSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import com.splendo.kaluga.resources.view.alignment
import kotlin.math.roundToInt

actual class StyledString(val spannable: Spannable)

actual val StyledString.rawString: String get() = spannable.toString()

actual class StyledStringBuilder actual constructor(string: String) {

    private class CustomCharacterStyle(val modify: TextPaint.() -> Unit) : CharacterStyle() {
        override fun updateDrawState(tp: TextPaint?) {
            tp?.modify()
        }
    }

    private val builder = SpannableString(string)

    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        builder.setSpan(
            when (attribute) {
                is StringStyleAttribute.CharacterStyleAttribute -> attribute.characterStyle
                is StringStyleAttribute.ParagraphStyleAttribute -> attribute.paragraphStyle
                is StringStyleAttribute.Link -> URLSpan(attribute.url)
            },
            range.start,
            range.endInclusive + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private val StringStyleAttribute.CharacterStyleAttribute.characterStyle: CharacterStyle get() = when(this) {
        is StringStyleAttribute.CharacterStyleAttribute.BackgroundColor -> BackgroundColorSpan(color)
        is StringStyleAttribute.CharacterStyleAttribute.ForegroundColor -> ForegroundColorSpan(color)
        is StringStyleAttribute.CharacterStyleAttribute.Font -> CustomCharacterStyle {
            typeface = font
            textSize = size
        }
        is StringStyleAttribute.CharacterStyleAttribute.Kerning -> CustomCharacterStyle { letterSpacing = kern }
        is StringStyleAttribute.CharacterStyleAttribute.TextStyle -> CustomCharacterStyle {
            typeface = textStyle.font
            textSize = textStyle.size
            color = textStyle.color
        }
        is StringStyleAttribute.CharacterStyleAttribute.Shadow -> CustomCharacterStyle {
            setShadowLayer(blurRadius, xOffset, yOffset, color)
        }
        is StringStyleAttribute.CharacterStyleAttribute.Strikethrough -> StrikethroughSpan()
        is StringStyleAttribute.CharacterStyleAttribute.SubScript -> SubscriptSpan()
        is StringStyleAttribute.CharacterStyleAttribute.SuperScript -> SuperscriptSpan()
        is StringStyleAttribute.CharacterStyleAttribute.Underline -> UnderlineSpan()
        is StringStyleAttribute.CharacterStyleAttribute.Stroke -> object : ReplacementSpan() {
            override fun draw(
                canvas: Canvas,
                text: CharSequence?,
                start: Int,
                end: Int,
                x: Float,
                top: Int,
                y: Int,
                bottom: Int,
                paint: Paint
            ) {
                text?.let {
                    canvas.drawText(
                        it,
                        start,
                        end,
                        x,
                        top.toFloat(),
                        Paint(paint).apply {
                            style = Paint.Style.FILL_AND_STROKE
                        }
                    )
                    canvas.drawText(
                        it,
                        start,
                        end,
                        x,
                        top.toFloat(),
                        Paint(paint).apply {
                            style = Paint.Style.STROKE
                            strokeWidth = width
                            color = this@characterStyle.color
                        }
                    )
                }
            }

            override fun getSize(
                paint: Paint,
                text: CharSequence?,
                start: Int,
                end: Int,
                fm: Paint.FontMetricsInt?
            ): Int = Paint(paint).apply {
                style = Paint.Style.FILL_AND_STROKE
            }
                .measureText(text, start, end)
                .roundToInt()
        }
    }

    private val StringStyleAttribute.ParagraphStyleAttribute.paragraphStyle: ParagraphStyle get() = when (this) {
        is StringStyleAttribute.ParagraphStyleAttribute.LeadingIndent -> LeadingMarginSpan.Standard(firstLineIndent.toInt(), indent.toInt())
        is StringStyleAttribute.ParagraphStyleAttribute.LineSpacing -> object : LineHeightSpan {
            override fun chooseHeight(
                text: CharSequence,
                start: Int,
                end: Int,
                spanstartv: Int,
                lineHeight: Int,
                fm: Paint.FontMetricsInt?
            ) {
                val originHeight = fm!!.descent - fm.ascent
                // If original height is not positive, do nothing.
                // If original height is not positive, do nothing.
                if (originHeight <= 0) {
                    return
                }
                val ratio: Float = spacing / originHeight
                fm.descent = (fm.descent * ratio).roundToInt()
                fm.ascent = fm.descent - spacing.toInt()
                if (paragraphSpacing > 0.0) {
                    val line = text.substring(start, end).toRegex()
                    if (line.findAll("\n").last().range.first == end - start - 1) {
                        fm.descent = fm.descent + paragraphSpacing.toInt()
                    }
                }
                if (paragraphSpacingBefore > 0.0) {
                    if (start == 0) {
                        fm.ascent = fm.ascent + paragraphSpacingBefore.toInt()
                    } else {
                        val line = text.substring(0, start).toRegex()
                        if (line.findAll("\n").last().range.first == end - start - 1) {
                            fm.ascent = fm.ascent + paragraphSpacingBefore.toInt()
                        }
                    }
                }
            }
        }
        is StringStyleAttribute.ParagraphStyleAttribute.Alignment -> AlignmentSpan.Standard(alignment.alignment)
    }

    actual fun create(): StyledString {
        return StyledString(builder)
    }
}
