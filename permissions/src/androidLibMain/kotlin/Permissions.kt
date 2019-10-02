package com.splendo.kaluga.permissions
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import android.content.Context

actual class Permissions {

    internal lateinit var context: Context

    actual fun getBluetoothManager(): PermissionManager {
        return BluetoothPermissionManager(context)
    }

    actual open class Builder {
        private lateinit var context: Context

        fun context(context: Context) = apply { this.context = context }

        actual open fun build(): Permissions {
            val permissions = Permissions()

            with(permissions) {
                this.context = this@Builder.context
            }

            return permissions
        }
    }

}