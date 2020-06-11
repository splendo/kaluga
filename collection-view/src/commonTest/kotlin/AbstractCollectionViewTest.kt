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

package com.splendo.kaluga.collectionview

import com.splendo.kaluga.base.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.CompletableDeferred

abstract class AbstractCollectionViewTest {

    companion object {
        val items = listOf(
            CollectionItem("One"),
            CollectionItem("Two"),
            CollectionItem("Tri")
        )
    }

    @Test
    fun testUpdateList() = runBlocking {
        val repository = MockCollectionItemRepository(emptyList())
        val viewModel = MockCollectionViewModel(repository)
        val deferredItems = MutableList(3) { CompletableDeferred<List<CollectionItem>>() }
        observe(viewModel, deferredItems)
        assertEquals(emptyList(), deferredItems[0].await())
        viewModel.didResume()
        assertEquals(emptyList(), deferredItems[1].await())

        repository.itemsToLoad = items
        viewModel.reload()

        assertEquals(items, deferredItems[2].await())
    }

    abstract fun observe(viewModel: MockCollectionViewModel, deferredItems: List<CompletableDeferred<List<CollectionItem>>>)
}
