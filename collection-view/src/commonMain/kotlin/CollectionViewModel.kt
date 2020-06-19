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
import com.splendo.kaluga.collectionview.datasource.CollectionDataSource
import com.splendo.kaluga.collectionview.datasource.CollectionHeaderFooterCellBinder
import com.splendo.kaluga.collectionview.datasource.CollectionItemCellBinder
import com.splendo.kaluga.collectionview.datasource.TableDataSource
import com.splendo.kaluga.collectionview.datasource.TableHeaderFooterCellBinder
import com.splendo.kaluga.collectionview.datasource.TableItemCellBinder
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.DefaultCollectionItemViewModel
import com.splendo.kaluga.collectionview.item.ItemsOnlyCollectionSection
import com.splendo.kaluga.collectionview.repository.CollectionItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class CollectionViewModel<Item, ViewModel : CollectionItemViewModel<Item>, Header, Footer>(
    private val repository: CollectionItemRepository<Item>,
    private val viewModelMapper: (Item) -> ViewModel,
    private val sorter: (List<ViewModel>) -> List<CollectionSection<Header, ViewModel, Footer>>
) : BaseViewModel() {

    private val currentItems = mutableListOf<CollectionSection<Header, ViewModel, Footer>>()
    private val _items = HotFlowable<List<CollectionSection<Header, ViewModel, Footer>>>(emptyList())
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

    fun <HeaderCell : CollectionHeaderFooterCellView,
        ItemCell : CollectionItemCellView,
        FooterCell : CollectionHeaderFooterCellView> toCollectionDataSource(
            headerBinder: CollectionHeaderFooterCellBinder<Header, HeaderCell>,
            itemBinder: CollectionItemCellBinder<ViewModel, ItemCell>,
            footerBinder: CollectionHeaderFooterCellBinder<Footer, FooterCell>
        ): CollectionDataSource<Header, ViewModel, Footer, HeaderCell, ItemCell, FooterCell> {
        return CollectionDataSource(items, headerBinder, itemBinder, footerBinder)
    }

    fun <HeaderCell : TableHeaderFooterCellView,
        ItemCell : TableItemCellView,
        FooterCell : TableHeaderFooterCellView> toTableDataSource(
            headerBinder: TableHeaderFooterCellBinder<Header, HeaderCell>,
            itemBinder: TableItemCellBinder<ViewModel, ItemCell>,
            footerBinder: TableHeaderFooterCellBinder<Footer, FooterCell>
        ): TableDataSource<Header, ViewModel, Footer, HeaderCell, ItemCell, FooterCell> {
        return TableDataSource(items, headerBinder, itemBinder, footerBinder)
    }

    override fun onCleared() {
        super.onCleared()
        clearCurrentItems()
    }

    private fun clearCurrentItems() {
        currentItems.forEach { section ->
            section.header?.let { header ->
                if (header is CollectionItemViewModel<*>) {
                    header.onCleared()
                }
            }
            section.items.forEach { it.onCleared() }
            section.footer?.let { footer ->
                if (footer is CollectionItemViewModel<*>) {
                    footer.onCleared()
                }
            }
        }
        currentItems.clear()
    }
}

open class SimpleCollectionViewModel<Item, ViewModel : CollectionItemViewModel<Item>>(
    repository: CollectionItemRepository<Item>,
    viewModelMapper: (Item) -> ViewModel
) : CollectionViewModel<
    Item,
    ViewModel,
    Nothing,
    Nothing>(
    repository,
    viewModelMapper,
    { items -> if (items.isEmpty()) emptyList() else listOf(ItemsOnlyCollectionSection(items)) }
) {
    fun <HeaderCell : CollectionHeaderFooterCellView,
        ItemCell : CollectionItemCellView,
        FooterCell : CollectionHeaderFooterCellView> toCollectionDataSource(
            itemBinder: CollectionItemCellBinder<ViewModel, ItemCell>
        ): CollectionDataSource<Nothing, ViewModel, Nothing, HeaderCell, ItemCell, FooterCell> {
        return CollectionDataSource(items, null, itemBinder, null)
    }

    fun <HeaderCell : TableHeaderFooterCellView,
        ItemCell : TableItemCellView,
        FooterCell : TableHeaderFooterCellView> toTableDataSource(
            itemBinder: TableItemCellBinder<ViewModel, ItemCell>
        ): TableDataSource<Nothing, ViewModel, Nothing, HeaderCell, ItemCell, FooterCell> {
        return TableDataSource(items, null, itemBinder, null)
    }
}

open class DefaultSimpleCollectionViewModel<Item>(
    repository: CollectionItemRepository<Item>
) : SimpleCollectionViewModel<Item, DefaultCollectionItemViewModel<Item>>(repository, { DefaultCollectionItemViewModel(it) })
