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

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.collectionview.CollectionCellView
import com.splendo.kaluga.collectionview.item.CollectionItem
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel

actual class ViewModelDataSource<Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>, Cell : CollectionCellView>(source: Observable<List<ViewModel>>, identifier: (ViewModel) -> String) : DataSource<ViewModel, Cell>(source, identifier) {

    override fun startDisplayingItem(item: ViewModel) {
        item.didResume()
    }

    override fun stopDisplayingItem(item: ViewModel) {
        item.didPause()
    }
}

actual class ViewModelDataSourceBuilder<Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>, Cell : CollectionCellView>(private val identifier: (ViewModel) -> String) :
    BaseDataSourceBuilder<ViewModel, Cell, ViewModelDataSource<Item, ViewModel, Cell>> {
    override fun create(items: Observable<List<ViewModel>>): ViewModelDataSource<Item, ViewModel, Cell> = ViewModelDataSource(items, identifier)
}
