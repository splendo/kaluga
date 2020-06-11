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
import com.splendo.kaluga.collectionview.CollectionView

expect class DataSourceBindingResult

expect open class DataSource<Item, Cell : CollectionCellView> {
    fun bindTo(collectionView: CollectionView) : DataSourceBindingResult
}

interface BaseDataSourceBuilder<Item, Cell : CollectionCellView, B : DataSource<Item, Cell>> {
    fun create(items: Observable<List<Item>>, bindCell: (Item, Cell) -> Unit): B
}

expect class DataSourceBuilder<Item, Cell : CollectionCellView> : BaseDataSourceBuilder<Item, Cell, DataSource<Item, Cell>>
