/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.links

import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.links.manager.MockLinksStateRepoBuilder
import com.splendo.kaluga.links.state.LinksState
import com.splendo.kaluga.links.state.LinksStateRepo
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowableTest
import kotlin.test.assertTrue

open class BaseLinksTest : FlowableTest<LinksState>() {

    val linksStateRepoBuilder = MockLinksStateRepoBuilder()
    lateinit var linksStateRepo: LinksStateRepo

    override fun flowable(): Flowable<LinksState> = linksStateRepo.flowable

    suspend fun assertInitialState(testBlock: FlowTest<LinksState>) {
        testBlock.test {
            assertTrue { it is LinksState.Pending }
        }
    }
}