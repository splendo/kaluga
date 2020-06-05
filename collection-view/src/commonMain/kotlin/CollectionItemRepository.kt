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

import com.splendo.kaluga.collectionview.item.CollectionItem
import com.splendo.kaluga.flow.BaseFlowable

abstract class CollectionItemRepository<Item : CollectionItem> {

    sealed class Result<Item : CollectionItem> {
        data class Success<Item : CollectionItem>(val items: List<Item>) : Result<Item>()
        data class Error<Item : CollectionItem>(val throwable: Throwable) : Result<Item>()
    }

    val items = BaseFlowable<List<Item>>().apply { setBlocking(emptyList()) }

    suspend fun loadItems(): Result<Item> {
        return updateItems().also { result ->
            when (result) {
                is Result.Success -> items.set(result.items)
            }
        }
    }

    protected abstract suspend fun updateItems(): Result<Item>

    suspend fun clear() {
        items.set(emptyList())
    }
}
