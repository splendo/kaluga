package com.splendo.kaluga.example.contacts.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Padding {
    val default = 8.dp
    val x2 = 16.dp
}

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.size(Padding.default))
}
