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
import com.splendo.kaluga.resources.stylable.TextStyle
import com.splendo.kaluga.resources.view.alignment
import kotlin.math.min
import kotlin.math.roundToInt

actual class StyledString(
    val spannable: Spannable,
    actual val defaultTextStyle: TextStyle,
    actual val linkStyle: LinkStyle?
)

actual val StyledString.rawString: String get() = spannable.toString()

actual class StyledStringBuilder constructor(
    string: String,
    private val defaultTextStyle: TextStyle,
    private val linkStyle: LinkStyle?,
    private val context: Context
) {

    actual class Provider(private val context: Context = ApplicationHolder.applicationContext) {
        actual fun provide(string: String, defaultTextStyle: TextStyle, linkStyle: LinkStyle?) =
            StyledStringBuilder(string, defaultTextStyle, linkStyle, context)
    }

    private class CustomCharacterStyle(val modify: TextPaint.() -> Unit) : CharacterStyle() {
        override fun updateDrawState(tp: TextPaint?) {
            tp?.modify()
        }
    }

    private val builder = SpannableString(string)

    actual fun addStyleAttribute(attribute: StringStyleAttribute, range: IntRange) {
        builder.setSpan(
            when (attribute) {
                is StringStyleAttribute.CharacterStyleAttribute -> attribute.characterStyle(range)
                is StringStyleAttribute.ParagraphStyleAttribute -> attribute.paragraphStyle
                is StringStyleAttribute.Link -> CustomURLSpan(attribute.url, linkStyle)
            },
            range.first,
            range.last + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun StringStyleAttribute.CharacterStyleAttribute.characterStyle(range: IntRange): CharacterStyle =
        when (this) {
            is StringStyleAttribute.CharacterStyleAttribute.BackgroundColor -> BackgroundColorSpan(
                color
            )
            is StringStyleAttribute.CharacterStyleAttribute.ForegroundColor -> ForegroundColorSpan(
                color
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
                    color
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
                        lineNumber: Int
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
                                strokePaint
                            )
                        } else {
                            canvas.drawText(
                                text,
                                start,
                                min(range.last + 1, end),
                                left.toFloat(),
                                baseline.toFloat(),
                                strokePaint
                            )
                        }
                    }
                }
        }

    private val StringStyleAttribute.ParagraphStyleAttribute.paragraphStyle: ParagraphStyle
        get() = when (this) {
            is StringStyleAttribute.ParagraphStyleAttribute.LeadingIndent -> LeadingMarginSpan.Standard(
                firstLineIndent.toInt(),
                indent.toInt()
            )
            is StringStyleAttribute.ParagraphStyleAttribute.LineSpacing -> object : LineHeightSpan {
                override fun chooseHeight(
                    text: CharSequence,
                    start: Int,
                    end: Int,
                    spanstartv: Int,
                    lineHeight: Int,
                    fm: Paint.FontMetricsInt?
                ) {
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
                alignment.alignment(context)
            )
        }

    actual fun create(): StyledString {
        return StyledString(builder, defaultTextStyle, linkStyle)
    }
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
