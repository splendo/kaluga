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

package com.splendo.kaluga.example.shared.viewmodel.collectionview

import com.splendo.kaluga.collectionview.CollectionViewModel
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.DefaultCollectionItemViewModel
import kotlin.native.concurrent.ThreadLocal

class CollectionViewViewModel(
    repository: ItemsRepository
) : CollectionViewModel<CollectionItem, DefaultCollectionItemViewModel<CollectionItem>, CollectionHeader, CollectionFooter>(
    repository,
    { item -> DefaultCollectionItemViewModel(item) },
    { items ->
        val titlesWithNumbers = items.filter { it.item.title.toIntOrNull() != null }
        val titlesWithoutNumbers = items - titlesWithNumbers
        listOf(
            CollectionSection(CollectionHeader("Items With Numbers"), titlesWithNumbers, CollectionFooter(titlesWithNumbers.size)),
            CollectionSection(CollectionHeader("Items Without Numbers"), titlesWithoutNumbers, CollectionFooter(titlesWithoutNumbers.size))
        )
}) {

    @ThreadLocal
    companion object {
        fun create() =
            CollectionViewViewModel(
                ItemsRepository()
            )
    }
}
