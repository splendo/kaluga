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

package com.splendo.kaluga.example.feature.scientific

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.example.feature.scientific.converters.QuantityConverter
import com.splendo.kaluga.example.feature.scientific.model.QuantityDetails
import com.splendo.kaluga.example.feature.scientific.model.name
import com.splendo.kaluga.example.feature.scientific.model.quantityDetails
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScientificScreen(modifier: Modifier = Modifier, viewModel: ScientificViewModel = koinViewModel()) {
    val selectedQuantity by viewModel.selectedQuantity.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        QuantityPicker(
            quantities = ScientificViewModel.quantities,
            selected = selectedQuantity,
            onSelected = viewModel::selectQuantity,
        )
        selectedQuantity?.let { details ->
            QuantitySection(details, viewModel)
        }
    }
}

@Composable
private fun QuantityPicker(quantities: List<QuantityDetails<*>>, selected: QuantityDetails<*>?, onSelected: (QuantityDetails<*>) -> Unit) {
    SearchablePicker(
        label = "Quantity",
        items = quantities,
        selected = selected,
        labelOf = { it.quantity.name },
        keyOf = { it.quantity.name },
        onSelected = onSelected,
    )
}

@Composable
private fun <Q : PhysicalQuantity> QuantitySection(details: QuantityDetails<Q>, viewModel: ScientificViewModel) {
    val unitList = remember(details) { details.units.toList() }
    var fromUnit by remember(details) { mutableStateOf(unitList.firstOrNull()) }
    var toUnit by remember(details) { mutableStateOf(unitList.getOrNull(1) ?: unitList.firstOrNull()) }
    var rawValue by remember(details) { mutableStateOf("1") }

    UnitPicker(label = "From", units = unitList, selected = fromUnit) { fromUnit = it }
    OutlinedTextField(
        value = rawValue,
        onValueChange = { rawValue = it },
        label = { Text("Value") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
    )
    UnitPicker(label = "To", units = unitList, selected = toUnit) { toUnit = it }

    Text(viewModel.convert(details, rawValue, fromUnit, toUnit), modifier = Modifier.fillMaxWidth())

    val from = fromUnit
    if (details.converters.isNotEmpty() && from != null) {
        HorizontalDivider()
        Text(
            "Other conversions",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
        )
        details.converters.forEach { converter ->
            ConverterCard(converter = converter, leftRaw = rawValue, leftUnit = from, viewModel = viewModel)
        }
    }
}

@Composable
private fun ConverterCard(converter: QuantityConverter<*, *>, leftRaw: String, leftUnit: ScientificUnit<*>, viewModel: ScientificViewModel) {
    var expanded by remember(converter) { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { expanded = !expanded },
            ) {
                Text(converter.name, modifier = Modifier.fillMaxWidth())
            }
            if (expanded) {
                when (converter) {
                    is QuantityConverter.Single<*, *> -> SingleConverterBody(converter, leftRaw, leftUnit, viewModel)
                    is QuantityConverter.WithOperator<*, *, *> -> WithOperatorConverterBody(converter, leftRaw, leftUnit, viewModel)
                }
            }
        }
    }
}

@Composable
private fun SingleConverterBody(converter: QuantityConverter.Single<*, *>, leftRaw: String, leftUnit: ScientificUnit<*>, viewModel: ScientificViewModel) {
    Text(viewModel.convertSingle(converter, leftRaw, leftUnit))
}

@Composable
private fun WithOperatorConverterBody(converter: QuantityConverter.WithOperator<*, *, *>, leftRaw: String, leftUnit: ScientificUnit<*>, viewModel: ScientificViewModel) {
    val rightDetails = remember(converter) {
        (converter.rightQuantity as PhysicalQuantity).quantityDetails
    }
    if (rightDetails == null || rightDetails.units.isEmpty()) {
        Text("No units available for ${(converter.rightQuantity as PhysicalQuantity).name}")
        return
    }
    val rightUnitList = remember(rightDetails) { rightDetails.units.toList() }
    var rightUnit by remember(rightDetails) {
        mutableStateOf<AbstractScientificUnit<out PhysicalQuantity>?>(rightUnitList.firstOrNull())
    }
    var rightRaw by remember(rightDetails) { mutableStateOf("1") }

    Text(
        "${leftUnit.name} ${converter.type.operatorSymbol} ${(rightUnit as? ScientificUnit<*>)?.name.orEmpty()}",
    )
    Row2(
        left = {
            OutlinedTextField(
                value = rightRaw,
                onValueChange = { rightRaw = it },
                label = { Text("Value") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        right = {
            @Suppress("UNCHECKED_CAST")
            UnitPicker(
                label = "Unit",
                units = rightUnitList as List<AbstractScientificUnit<PhysicalQuantity>>,
                selected = rightUnit as AbstractScientificUnit<PhysicalQuantity>?,
                onSelected = { rightUnit = it },
            )
        },
    )

    Text(
        viewModel.convertWithOperator(converter, leftRaw, leftUnit, rightRaw, rightUnit as? ScientificUnit<*>),
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun Row2(left: @Composable () -> Unit, right: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        left()
        right()
    }
}

@Composable
private fun <Q : PhysicalQuantity> UnitPicker(
    label: String,
    units: List<AbstractScientificUnit<Q>>,
    selected: AbstractScientificUnit<Q>?,
    onSelected: (AbstractScientificUnit<Q>) -> Unit,
) {
    SearchablePicker(
        label = label,
        items = units,
        selected = selected,
        labelOf = { (it as ScientificUnit<*>).name },
        keyOf = { (it as ScientificUnit<*>).name },
        onSelected = onSelected,
    )
}

/**
 * Generic "tap to open a searchable list dialog" picker. The trigger is an [OutlinedButton]
 * styled as a labeled field; the dialog itself reuses the same search + [LazyColumn] pattern as
 * the locale and time-zone pickers.
 */
@Composable
private fun <T> SearchablePicker(label: String, items: List<T>, selected: T?, labelOf: (T) -> String, keyOf: (T) -> Any, onSelected: (T) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showDialog = true },
    ) {
        Text("$label: ${selected?.let(labelOf) ?: "—"}")
    }
    if (showDialog) {
        var query by remember { mutableStateOf("") }
        val filtered = remember(query, items) {
            if (query.isBlank()) {
                items
            } else {
                val q = query.trim().lowercase()
                items.filter { labelOf(it).lowercase().contains(q) }
            }
        }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select $label") },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Search") },
                    )
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(filtered, key = keyOf) { item ->
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onSelected(item)
                                    showDialog = false
                                },
                            ) { Text(labelOf(item)) }
                        }
                    }
                }
            },
        )
    }
}
