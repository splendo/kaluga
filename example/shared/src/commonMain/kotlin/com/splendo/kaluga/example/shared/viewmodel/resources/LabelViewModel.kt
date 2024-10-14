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

package com.splendo.kaluga.example.shared.viewmodel.resources

import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.example.shared.stylable.TextStyles
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.StringStyleAttribute
import com.splendo.kaluga.resources.StyledStringBuilder
import com.splendo.kaluga.resources.attributeSubstring
import com.splendo.kaluga.resources.defaultBoldFont
import com.splendo.kaluga.resources.stylable.KalugaTextAlignment
import com.splendo.kaluga.resources.styled
import com.splendo.kaluga.resources.view.KalugaLabel

class LabelViewModel(styledStringBuilderProvider: StyledStringBuilder.Provider) : BaseLifecycleViewModel() {

    companion object {
        val loremIpsumParagraph0 = listOf(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            "Etiam et est quis lectus bibendum tempor.",
            "Interdum et malesuada fames ac ante ipsum primis in faucibus.",
            "Donec convallis metus efficitur mattis accumsan.",
            "Pellentesque efficitur elit felis, vitae dictum sem vehicula eu.",
            "In hac habitasse platea dictumst. Donec sollicitudin eleifend dui, sit amet viverra ex sollicitudin nec.",
            "Sed at faucibus orci.",
            "Vivamus ultricies purus eu nisi blandit, nec ullamcorper felis facilisis.",
            "Duis accumsan dolor dignissim massa malesuada, in blandit sapien vestibulum.",
        ).joinToString(" ")
        val loremIpsumParagraph1 = listOf(
            "Duis eget leo commodo, consequat felis sed, vehicula justo.",
            "Morbi facilisis facilisis varius.",
            "Donec efficitur pretium pharetra.",
            "Praesent ut lorem commodo, efficitur lectus quis, dapibus augue.",
            "Quisque sed maximus quam.",
            "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;" +
                "Suspendisse ullamcorper, augue id blandit lobortis, arcu enim pellentesque felis, quis ultrices urna nibh et nibh.",
            "Donec et velit faucibus, luctus massa congue, ornare odio.",
            "Vestibulum interdum nec metus at eleifend.",
            "Mauris finibus magna eu dolor dignissim elementum at vitae enim.",
            "Nunc posuere, enim eu hendrerit volutpat, sapien lacus blandit dolor, eu faucibus nisl ex sed enim.",
            "Praesent in ligula eu nulla semper posuere sollicitudin et nunc.",
        ).joinToString(" ")
        val loremIpsumParagraph2 = listOf(
            "Aenean eget quam eu diam volutpat tincidunt.",
            "Pellentesque maximus tristique mi, eget pellentesque risus volutpat et.",
            "Duis id ultrices mi.",
            "Praesent nec orci at nisl pulvinar lobortis ut sit amet purus.",
            "In id fermentum mi.",
            "Nunc vel nunc nisi.",
            "Vestibulum malesuada ultricies dui a blandit.",
        ).joinToString(" ")
        val loremIpsum = listOf(loremIpsumParagraph0, loremIpsumParagraph1, loremIpsumParagraph2).joinToString("\n")
    }

    val labels = observableOf(
        listOf(
            KalugaLabel.Plain("Default Text", TextStyles.defaultText),
            KalugaLabel.Plain("Title Text", TextStyles.defaultTitle),
            KalugaLabel.Plain("Bold Text", TextStyles.defaultBoldText),
            KalugaLabel.Plain("Italic Text", TextStyles.defaultItalicText),
            KalugaLabel.Plain("Monospace Text", TextStyles.defaultMonospaceText),
            KalugaLabel.Plain("SemiBold Text", TextStyles.semiBoldText),
            KalugaLabel.Plain("Serif Text", TextStyles.serifText),
            KalugaLabel.Plain("Italic Bold Text", TextStyles.italicBoldText),
            KalugaLabel.Plain("Light Italic Monospace Text", TextStyles.lightItalicMonospaceText),
            KalugaLabel.Plain("Red Text", TextStyles.redText),
            KalugaLabel.Plain("Opposite Aligned Text", TextStyles.oppositeText),
            KalugaLabel.Styled(
                "Foreground Styled text".styled(
                    styledStringBuilderProvider,
                    TextStyles.defaultText,
                    StringStyleAttribute.CharacterStyleAttribute.ForegroundColor(DefaultColors.deepSkyBlue),
                ),
            ),
            KalugaLabel.Styled(
                "Background Styled text".styled(
                    styledStringBuilderProvider,
                    TextStyles.defaultText,
                    StringStyleAttribute.CharacterStyleAttribute.BackgroundColor(DefaultColors.deepSkyBlue),
                ),
            ),
            KalugaLabel.Styled(
                "Partial Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.TextStyle(TextStyles.redText), IntRange(0, 13))
                }),
            ),
            KalugaLabel.Styled(
                "Font Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.Font(defaultBoldFont, 12.0f), IntRange(0, 10))
                }),
            ),
            KalugaLabel.Styled(
                "Stroke Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.Stroke(2.0f, DefaultColors.limeGreen), IntRange(0, 12))
                }),
            ),
            KalugaLabel.Styled(
                "Subscript Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.SubScript, IntRange(0, 15))
                }),
            ),
            KalugaLabel.Styled(
                "Superscript Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.SuperScript, IntRange(0, 17))
                }),
            ),
            KalugaLabel.Styled(
                "Shadow Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.Shadow(DefaultColors.dimGray, 2.0f, 2.0f, 5.0f), IntRange(0, 12))
                }),
            ),
            KalugaLabel.Styled(
                "Kerning Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.Kerning(0.08f), IntRange(0, 13))
                }),
            ),
            KalugaLabel.Styled(
                "Underline Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.Underline, IntRange(0, 15))
                }),
            ),
            KalugaLabel.Styled(
                "Strikethrough Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, {
                    Pair(StringStyleAttribute.CharacterStyleAttribute.Strikethrough, IntRange(0, 20))
                }),
            ),
            KalugaLabel.Styled(
                "Link Styled text".styled(styledStringBuilderProvider, TextStyles.defaultText, DefaultColors.limeGreen, {
                    Pair(StringStyleAttribute.Link("https://kaluga.splendo.com"), IntRange(0, 10))
                }),
            ),
            KalugaLabel.Styled(loremIpsum.styled(styledStringBuilderProvider, TextStyles.defaultText, StringStyleAttribute.ParagraphStyleAttribute.LineSpacing(5.0f, 10.0f, 6.0f))),
            KalugaLabel.Styled(
                loremIpsum.styled(
                    styledStringBuilderProvider,
                    TextStyles.defaultText,
                    { attributeSubstring(loremIpsumParagraph1, StringStyleAttribute.ParagraphStyleAttribute.LeadingIndent(10.0f, 15.0f)) },
                ),
            ),
            KalugaLabel.Styled(
                loremIpsum.styled(
                    styledStringBuilderProvider,
                    TextStyles.defaultText,
                    { attributeSubstring(loremIpsumParagraph1, StringStyleAttribute.ParagraphStyleAttribute.Alignment(KalugaTextAlignment.END)) },
                ),
            ),
            KalugaLabel.Styled(
                loremIpsum.styled(
                    styledStringBuilderProvider,
                    TextStyles.defaultText,
                    { attributeSubstring(this, StringStyleAttribute.ParagraphStyleAttribute.LineSpacing(2.0f, 4.0f, 1.0f)) },
                    { attributeSubstring(loremIpsumParagraph0, StringStyleAttribute.CharacterStyleAttribute.BackgroundColor(DefaultColors.deepSkyBlue)) },
                    { attributeSubstring(loremIpsumParagraph1, StringStyleAttribute.ParagraphStyleAttribute.LeadingIndent(10.0f)) },
                    { attributeSubstring(loremIpsumParagraph1, StringStyleAttribute.ParagraphStyleAttribute.Alignment(KalugaTextAlignment.END)) },
                    { attributeSubstring(loremIpsumParagraph2, StringStyleAttribute.CharacterStyleAttribute.Kerning(0.08f)) },
                ),
            ),
        ),
    )
}
