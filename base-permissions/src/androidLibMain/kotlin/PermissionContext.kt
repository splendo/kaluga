/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.base

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder

/**
 * Android context for creating a [BasePermissionsBuilder]
 * @property context the [Context] in which the permission is to be granted
 */
actual data class PermissionContext(val context: Context)

actual val defaultPermissionContext get() = PermissionContext(ApplicationHolder.applicationContext)
