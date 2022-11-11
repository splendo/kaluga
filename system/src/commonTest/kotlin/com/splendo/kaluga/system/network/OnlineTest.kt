/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.system.network

import com.splendo.kaluga.system.network.state.online
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OnlineTest : BaseNetworkStateTest<Boolean, Flow<Boolean>>() {

    override val flowFromTestContext: suspend Context.() -> Flow<Boolean> = { networkStateRepo.online() }

    @Test
    fun testIsOnOrOffline() = testNetworkState(NetworkConnectionType.Known.Wifi()) { networkStateRepo ->
        test {
            assertTrue(it)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Absent
        }

        test {
            assertFalse(it)
        }

        mainAction {
            networkManager.network.value = NetworkConnectionType.Known.Cellular()
        }
        test {
            assertTrue(it)
        }
    }
}
