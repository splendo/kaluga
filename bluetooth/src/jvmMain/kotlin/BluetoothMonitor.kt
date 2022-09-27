/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.monitor.DefaultServiceMonitor
import kotlin.coroutines.CoroutineContext

actual interface BluetoothMonitor {
    actual class Builder {
        /**
         * Builder's create method.
         * @param coroutineContext [CoroutineContext] used to define the coroutine context where code will run.
         * @return [DefaultServiceMonitor]
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor = DefaultBluetoothMonitor(coroutineContext)
    }
}

class DefaultBluetoothMonitor(
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext), BluetoothMonitor
