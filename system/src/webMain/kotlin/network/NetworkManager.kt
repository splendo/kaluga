/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Default implementation of [NetworkManager] for the JS family (js + wasmJs).
 *
 * Connectivity is read from `navigator.onLine` and the `online`/`offline` window events. The connection
 * *type* uses the [Network Information API](https://developer.mozilla.org/en-US/docs/Web/API/Network_Information_API)
 * (`navigator.connection`), which is Chromium-only — where it is unavailable an online connection is
 * reported as [NetworkConnectionType.Known.Wifi].
 */
actual class DefaultNetworkManager internal constructor() : NetworkManager {

    /**
     * Builder for creating a [DefaultNetworkManager]
     */
    class Builder : NetworkManager.Builder {
        override fun create(): NetworkManager = DefaultNetworkManager()
    }

    private val networkChannel = Channel<NetworkConnectionType>(Channel.UNLIMITED)
    actual override val network: Flow<NetworkConnectionType> = networkChannel.receiveAsFlow()

    actual override suspend fun startMonitoring() {
        emitCurrentNetwork()
        registerNetworkListeners { emitCurrentNetwork() }
    }

    actual override suspend fun stopMonitoring() {
        unregisterNetworkListeners()
    }

    private fun emitCurrentNetwork() {
        networkChannel.trySend(currentNetworkConnectionType())
    }
}

private fun currentNetworkConnectionType(): NetworkConnectionType = when {
    !isNetworkOnline() -> NetworkConnectionType.Known.Absent
    networkConnectionTypeTag() == "cellular" -> NetworkConnectionType.Known.Cellular
    networkConnectionTypeTag() == "none" -> NetworkConnectionType.Known.Absent
    else -> NetworkConnectionType.Known.Wifi()
}

private fun isNetworkOnline(): Boolean = js("typeof navigator !== 'undefined' ? !!navigator.onLine : false")

// `navigator.connection.type` ("wifi"/"cellular"/"ethernet"/"none"/"unknown"); "" when the API is absent.
private fun networkConnectionTypeTag(): String = js("(typeof navigator !== 'undefined' && navigator.connection && navigator.connection.type) ? navigator.connection.type : ''")

private fun registerNetworkListeners(onChange: () -> Unit) {
    js(
        """
        if (typeof window === 'undefined') return;
        var handler = function () { onChange(); };
        globalThis.__kalugaNetworkHandler = handler;
        window.addEventListener('online', handler);
        window.addEventListener('offline', handler);
        if (navigator.connection && navigator.connection.addEventListener) {
            navigator.connection.addEventListener('change', handler);
        }
        """,
    )
}

private fun unregisterNetworkListeners() {
    js(
        """
        if (typeof window === 'undefined') return;
        var handler = globalThis.__kalugaNetworkHandler;
        if (!handler) return;
        window.removeEventListener('online', handler);
        window.removeEventListener('offline', handler);
        if (navigator.connection && navigator.connection.removeEventListener) {
            navigator.connection.removeEventListener('change', handler);
        }
        globalThis.__kalugaNetworkHandler = null;
        """,
    )
}
