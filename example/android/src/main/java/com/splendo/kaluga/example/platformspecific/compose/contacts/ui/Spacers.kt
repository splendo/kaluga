package com.splendo.kaluga.example.platformspecific.compose.contacts.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.DefaultSpacer() {
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun ColumnScope.DefaultSpacer() {
    Spacer(modifier = Modifier.height(8.dp))
}
