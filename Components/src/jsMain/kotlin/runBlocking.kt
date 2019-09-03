package com.splendo.mpp

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): dynamic {
    // this does not wait for the result unfortunately
    return GlobalScope.launch { block(this) }
}