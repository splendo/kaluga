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

package com.splendo.kaluga.collectionview.repository

import com.splendo.kaluga.base.flow.HotFlowable

abstract class CollectionItemRepository<Item> {

    sealed class Result<Item> {
        data class Success<Item>(val items: List<Item>) : Result<Item>()
        data class Error<Item>(val throwable: Throwable) : Result<Item>()
    }

    val items = HotFlowable<List<Item>>(emptyList())

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
