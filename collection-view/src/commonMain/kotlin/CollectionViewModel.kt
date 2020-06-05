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

import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.collectionview.item.CollectionItem
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class CollectionViewModel<Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>>(
    private val repository: CollectionItemRepository<Item>,
    private val viewModelMapper: (Item) -> ViewModel
) : BaseViewModel() {

    private val currentItems = mutableListOf<ViewModel>()
    private val _items = BaseFlowable<List<ViewModel>>().apply { setBlocking(emptyList()) }
    val items = _items.toObservable(coroutineScope)

    init {
        coroutineScope.launch {
            repository.items.flow().collect { items ->
                currentItems.forEach { it.onCleared() }
                currentItems.clear()
                val viewModels = items.map { viewModelMapper(it) }
                currentItems.addAll(viewModels)
                _items.set(viewModels)
            }
        }
    }

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch { repository.loadItems() }
    }

    fun reload() {
        coroutineScope.launch { repository.loadItems() }
    }

    override fun onCleared() {
        super.onCleared()
        currentItems.forEach { it.onCleared() }
    }
}


