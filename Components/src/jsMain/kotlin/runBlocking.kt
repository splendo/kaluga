package com.splendo.mpp

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.js.Promise

actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    return Promise.Companion.resolve(GlobalScope.promise { block(this) }).asDynamic()
}