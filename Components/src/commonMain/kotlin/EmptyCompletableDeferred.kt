package com.splendo.mpp

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job

typealias EmptyCompletableDeferred = CompletableDeferred<Unit>

@Suppress("FunctionName") // Kotlin convention to provide package methods that look like constructors
/**
 * Creates a [CompletableDeferred] in an _active_ state of typed [Unit].
 * In other words, it returns no value (either it will complete or throw an Exception).
 * It is optionally a child of a [parent] job.
 */
fun EmptyCompletableDeferred(parent: Job? = null): EmptyCompletableDeferred = CompletableDeferred(parent)
