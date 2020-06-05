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

import com.splendo.kaluga.collectionview.datasource.DataSource
import com.splendo.kaluga.collectionview.datasource.DataSourceBuilder
import com.splendo.kaluga.collectionview.item.CollectionItem
import com.splendo.kaluga.collectionview.item.CollectionItemViewModel
import kotlinx.coroutines.CoroutineScope

expect class CollectionView {
    var dataSource: DataSource<*, *>?
}

internal fun CollectionView.bind(dataSource: DataSource<*, *>) {
    this.dataSource = dataSource
}

internal fun CollectionView.unbind() {
    this.dataSource?.unbind()
    this.dataSource = null
}

expect class CollectionViewBindingResult(onUnbind: () -> Unit)

fun <Item : CollectionItem, ViewModel: CollectionItemViewModel<Item>> CollectionViewModel<Item, ViewModel>.bind(
    collectionView: CollectionView,
    dataSourceBuilder: DataSourceBuilder<Item, ViewModel>,
    coroutineScope: CoroutineScope = this.coroutineScope): CollectionViewBindingResult {
    val dataSource = dataSourceBuilder.build()
}