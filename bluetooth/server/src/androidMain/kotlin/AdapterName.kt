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

package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothManager
import android.content.Context

/**
 * Persists the adapter name a [BluetoothServer] temporarily overrode while advertising a custom local name,
 * so it can be restored even if the process is killed before the server tears down gracefully.
 */
internal class AdapterNameOverrideStore(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    /** The name the adapter held before it was overridden, or `null` if none is recorded. */
    val original: String? get() = preferences.getString(KEY_ORIGINAL, null)

    /** The name the adapter was overridden to, or `null` if no override is recorded. */
    val override: String? get() = preferences.getString(KEY_OVERRIDE, null)

    fun record(original: String?, override: String) {
        preferences.edit().putString(KEY_ORIGINAL, original).putString(KEY_OVERRIDE, override).apply()
    }

    fun clear() {
        preferences.edit().remove(KEY_ORIGINAL).remove(KEY_OVERRIDE).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "com.splendo.kaluga.bluetooth.server.adapterName"
        const val KEY_ORIGINAL = "original"
        const val KEY_OVERRIDE = "override"
    }
}

/**
 * Restores the device's global Bluetooth adapter name if a previous [BluetoothServer] advertising session
 * overrode it (to advertise a custom local name) and was interrupted—e.g. by a process kill—before it could
 * restore it. [BluetoothServer] restoration is best-effort and runs on graceful teardown; call this to also
 * repair a leak from a prior process, for example from `Application.onCreate`, when the app does not build a
 * server on startup.
 *
 * The original name is only restored if the adapter still holds the overridden name, so a name the user or
 * another app has since changed is never clobbered. Any stale record is cleared either way.
 * @param context the [Context] used to access the Bluetooth adapter and the stored override
 * @return `true` if the adapter name was restored
 */
fun restoreBluetoothAdapterName(context: Context): Boolean {
    val store = AdapterNameOverrideStore(context)
    val override = store.override ?: return false
    val original = store.original
    val adapter = (context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
    val restored = try {
        original != null && adapter?.name == override && adapter.setName(original)
    } catch (_: SecurityException) {
        false
    }
    store.clear()
    return restored
}
