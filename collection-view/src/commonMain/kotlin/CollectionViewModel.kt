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
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.DefaultCollectionItemViewModel
import com.splendo.kaluga.collectionview.item.ItemsOnlyCollectionSection
import com.splendo.kaluga.collectionview.repository.CollectionItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class CollectionViewModel<Item, ViewModel : CollectionItemViewModel<Item>, Header, Footer, Section : CollectionSection<Header, ViewModel, Footer>>(
    private val repository: CollectionItemRepository<Item>,
    private val viewModelMapper: (Item) -> ViewModel,
    private val sorter: (List<ViewModel>) -> List<Section>
) : BaseViewModel() {

    private val currentItems = mutableListOf<Section>()
    private val _items = HotFlowable<List<Section>>(emptyList())
    val items = _items.toObservable(coroutineScope)

    init {
        coroutineScope.launch {
            repository.items.flow().collect { items ->
                clearCurrentItems()
                val viewModels = items.map { viewModelMapper(it) }
                val sortedViewModels = sorter(viewModels)
                currentItems.addAll(sortedViewModels)
                _items.set(sortedViewModels)
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
        clearCurrentItems()
    }

    private fun clearCurrentItems() {
        currentItems.forEach { section ->
            section.header?.let {header ->
                if (header is CollectionItemViewModel<*>) {
                    header.onCleared()
                }
            }
            section.items.forEach { it.onCleared() }
            section.footer?.let {footer ->
                if (footer is CollectionItemViewModel<*>) {
                    footer.onCleared()
                }
            }
        }
        currentItems.clear()
    }
}

open class SimpleCollectionViewModel<Item>(repository: CollectionItemRepository<Item>) : CollectionViewModel<
    Item,
    DefaultCollectionItemViewModel<Item>,
    Nothing,
    Nothing,
    CollectionSection<Nothing, DefaultCollectionItemViewModel<Item>, Nothing>>(
    repository,
    { item -> DefaultCollectionItemViewModel(item) },
    { items -> listOf(ItemsOnlyCollectionSection(items)) }
)
