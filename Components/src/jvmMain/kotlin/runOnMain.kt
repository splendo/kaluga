package com.splendo.mpp

import kotlinx.coroutines.CoroutineScope
import javax.swing.SwingUtilities

actual fun runOnMain(block: () -> Unit) {
    SwingUtilities.invokeLater(block)
}