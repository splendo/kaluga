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

import com.splendo.kaluga.collectionview.repository.CollectionItemRepository

class CollectionItem(val title: String)

class CollectionHeader(val title: String)

class CollectionFooter(val numberOfElements: Int)

class ItemsRepository : CollectionItemRepository<CollectionItem>() {

    override suspend fun updateItems(): Result<CollectionItem> {
        return Result.Success(listOf(
            CollectionItem("One"),
            CollectionItem("Two"),
            CollectionItem("3"),
            CollectionItem("4"),
            CollectionItem("Five")))
    }

}
