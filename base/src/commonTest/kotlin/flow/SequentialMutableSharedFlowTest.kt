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

package flow

import com.splendo.kaluga.base.flow.SequentialMutableSharedFlow
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SequentialMutableSharedFlowTest : BaseTest() {

    @Test
    fun testSequentialMutableSharedFlow() = runBlocking {
        launch {
            val sequentialFlow = SequentialMutableSharedFlow<Int>(0, 2, this)
            val collectionProcessing = MutableList(4) { EmptyCompletableDeferred() }
            val collectionResult = MutableList(4) { CompletableDeferred<Int>() }

            launch {
                sequentialFlow.take(4).collectIndexed { index, value ->
                    collectionProcessing[index].await()
                    collectionResult[index].complete(value)
                }
            }

            yield()

            assertTrue(sequentialFlow.tryEmitOrLaunchAndEmit(10))
            assertTrue(sequentialFlow.tryEmitOrLaunchAndEmit(8))
            assertFalse(sequentialFlow.tryEmitOrLaunchAndEmit(42))
            assertFalse(sequentialFlow.tryEmitOrLaunchAndEmit(4))

            collectionProcessing[0].complete()
            collectionProcessing[1].complete()
            collectionProcessing[2].complete()
            collectionProcessing[3].complete()

            assertEquals(10, collectionResult[0].await())
            assertEquals(8, collectionResult[1].await())
            assertEquals(42, collectionResult[2].await())
            assertEquals(4, collectionResult[3].await())
            sequentialFlow.cancel()
        }.join()
    }
}
