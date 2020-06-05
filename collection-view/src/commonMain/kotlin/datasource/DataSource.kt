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

package com.splendo.kaluga.collectionview.datasource

import com.splendo.kaluga.collectionview.item.CollectionItem
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseDataSource<Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>> internal constructor(private val itemFlow: Flow<List<ViewModel>>, coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    private val itemsJob: Job
    protected val items: MutableList<ViewModel> = mutableListOf()

    init {
        itemsJob = launch {
            itemFlow.collect { items ->
                this@BaseDataSource.items.clear()
                this@BaseDataSource.items.addAll(items)
                notifyItemsChanged()
            }
        }
    }

    fun unbind() {
        itemsJob.cancel()
    }

    abstract fun notifyItemsChanged()

}

expect class DataSource<Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>> : BaseDataSource<Item, ViewModel>

expect class DataSourceBuilder<Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>> {
    fun build(itemFlow: Flow<List<ViewModel>>, coroutineScope: CoroutineScope): DataSource<Item, ViewModel>
}