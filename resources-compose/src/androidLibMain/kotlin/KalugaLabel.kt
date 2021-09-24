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
package com.splendo.kaluga.resources.compose

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.splendo.kaluga.resources.DefaultColors
import com.splendo.kaluga.resources.StringStyleAttribute
import com.splendo.kaluga.resources.StyledStringBuilder
import com.splendo.kaluga.resources.defaultBoldFont
import com.splendo.kaluga.resources.stylable.TextAlignment
import com.splendo.kaluga.resources.styled
import com.splendo.kaluga.resources.view.KalugaLabel
import com.splendo.kaluga.resources.view.bindLabel

@Composable
fun KalugaLabel<*>.composable(modifier: Modifier) {
    when (this) {
        is KalugaLabel.Plain -> {
            Text(
                modifier = modifier,
                text = text,
                color = style.color.composable,
                fontFamily = FontFamily(style.font),
                fontSize = style.size.sp,
                textAlign = style.alignment.composable
            )
        }
        is KalugaLabel.Styled -> {
            // Compose does not support SpannedString and AttributedString lacks features so use legacy view
            AndroidView(
                modifier = modifier,
                factory = { context ->
                    TextView(context).apply {
                        this.bindLabel(this@composable)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewKalugaLabel() {
    val textStyle = com.splendo.kaluga.resources.stylable.TextStyle(defaultBoldFont, DefaultColors.darkRed, 12.0f)
    Column(modifier = Modifier.size(100.dp)) {
        KalugaLabel.Plain("Plain Text", textStyle).composable(
            modifier = Modifier.fillMaxWidth()
        )
        KalugaLabel.Styled(
            "Styled Text".styled(
                StyledStringBuilder.Provider(LocalContext.current),
                {
                Pair(StringStyleAttribute.CharacterStyleAttribute.ForegroundColor(DefaultColors.darkBlue), IntRange(0, 5))
            }),
            textStyle
        ).composable(
            modifier = Modifier.fillMaxWidth()
        )
    }
}
