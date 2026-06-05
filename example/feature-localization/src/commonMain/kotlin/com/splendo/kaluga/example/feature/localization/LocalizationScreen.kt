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

package com.splendo.kaluga.example.feature.localization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.base.text.DateFormatStyle
import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LocalizationScreen(modifier: Modifier = Modifier, viewModel: LocalizationViewModel = koinViewModel()) {
    val selected by viewModel.selectedLocale.collectAsState()
    var showPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(modifier = Modifier.fillMaxWidth(), onClick = { showPicker = true }) {
            Text("Locale: $selected")
        }

        SectionTitle("Identifier")
        Field("Language code", selected.languageCode)
        Field("Country code", selected.countryCode)
        Field("Script code", selected.scriptCode.ifEmpty { "—" })
        Field("Variant code", selected.variantCode.ifEmpty { "—" })
        Field("Unit system", selected.unitSystem.name)

        SectionTitle("Display names (in this locale)")
        Field("Name", selected.name(selected))
        Field("Language", selected.languageName(selected))
        Field("Country", selected.countryName(selected))
        Field("Quotation", "${selected.quotationStart}quote${selected.quotationEnd}")
        Field("Alt. quotation", "${selected.alternateQuotationStart}quote${selected.alternateQuotationEnd}")

        SectionTitle("Numbers")
        Field("Integer", viewModel.integer(selected))
        Field("Decimal", viewModel.decimal(selected))
        Field("Percentage", viewModel.percent(selected))
        Field("Permillage", viewModel.permille(selected))
        Field("Currency", viewModel.currency(selected))
        Field("USD currency", viewModel.currency(selected, "USD"))
        Field("JPY currency", viewModel.currency(selected, "JPY"))

        SectionTitle("Date")
        val now = remember(selected) { DefaultKalugaDate.now(locale = selected) }
        DateFormatStyle.values().forEach { style ->
            Field(style.name, KalugaDateFormatter.dateFormat(style = style, locale = selected).format(now))
        }

        SectionTitle("Time")
        DateFormatStyle.values().forEach { style ->
            Field(style.name, KalugaDateFormatter.timeFormat(style = style, locale = selected).format(now))
        }
    }

    if (showPicker) {
        LocalePickerDialog(
            locales = viewModel.availableLocales,
            onDismiss = { showPicker = false },
            onSelect = {
                viewModel.selectLocale(it)
                showPicker = false
            },
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    Text(text, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun Field(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun LocalePickerDialog(locales: List<KalugaLocale>, onDismiss: () -> Unit, onSelect: (KalugaLocale) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, locales) {
        if (query.isBlank()) {
            locales
        } else {
            val q = query.trim().lowercase()
            locales.filter { locale ->
                locale.toString().lowercase().contains(q) ||
                    locale.name(locale).lowercase().contains(q) ||
                    locale.countryName(locale).lowercase().contains(q) ||
                    locale.languageName(locale).lowercase().contains(q)
            }
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Select Locale") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Search locale, language, or country") },
                )
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    // `BaseLocale.toString()` is `lang_country_variant` — it ignores `scriptCode`,
                    // so `az_Cyrl` and `az_Latn` would collide. Include the script + position so
                    // each row gets a unique key even when iCU exposes script-differentiated dupes.
                    itemsIndexed(
                        items = filtered,
                        key = { index, locale -> "$index:$locale:${locale.scriptCode}" },
                    ) { _, locale ->
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onSelect(locale) },
                        ) {
                            Text("$locale — ${locale.name(locale)}")
                        }
                    }
                }
            }
        },
    )
}
