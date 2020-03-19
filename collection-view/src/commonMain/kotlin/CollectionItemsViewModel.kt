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

package com.splendo.kaluga.collectionView

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

open class CollectionItemsViewModel<Item : CollectionViewItem>(
    private val repository: CollectionItemRepository<Item>
) : CollectionViewModel() {

    private val items = ConflatedBroadcastChannel<List<Item>>()

    init {
        coroutineScope.launch {
            items.offer(repository.getItems())
        }
    }

    fun subscribe(block: (List<Item>) -> Unit) {
        coroutineScope.launch {
            items.consumeEach {
                block(it)
            }
        }
    }
}
