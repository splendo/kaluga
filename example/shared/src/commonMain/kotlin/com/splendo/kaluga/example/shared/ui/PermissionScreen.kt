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

package com.splendo.kaluga.example.shared.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun PermissionScreen(permissionView: PermissionView, modifier: Modifier = Modifier) {
    val builder: PermissionsBuilder = koinInject()
    val composeScope = rememberCoroutineScope()
    val permissionScope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    DisposableEffect(permissionView) { onDispose { permissionScope.cancel() } }

    val permission = remember(permissionView) { permissionView.permission }
    val permissions = remember(permissionView) {
        Permissions(builder, permissionScope.coroutineContext)
    }
    val state by remember(permissionView) { permissions[permission] }
        .collectAsState(initial = null)

    var requestMessage by remember { mutableStateOf<String?>(null) }

    val message = when (state) {
        is PermissionState.Allowed -> "Allowed"
        is PermissionState.Denied.Requestable -> "Requestable"
        is PermissionState.Denied.Locked -> "Denied"
        else -> "Unknown"
    }
    val canRequest = state is PermissionState.Denied.Requestable

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(permissionView.title)
        Text("State: $message")
        if (canRequest) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    composeScope.launch {
                        requestMessage = if (permissions.request(permission)) {
                            "Permission granted"
                        } else {
                            "Permission denied"
                        }
                    }
                },
            ) {
                Text("Request Permission")
            }
        }
        requestMessage?.let { Text(it) }
    }
}
