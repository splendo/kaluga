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

package com.splendo.kaluga.collectionview.item

open class CollectionSection<Header, Item, Footer>(val header: Header?, val items: List<Item>, val footer: Footer?) {
    val hasHeader: Boolean get() = header != null
    val hasFooter: Boolean get() = footer != null
    val numberOfItems: Int get() = items.size

    val totalNumberOfElements: Int get() {
        var result = 0
        if (hasHeader)
            result++
        if (hasFooter)
            result++
        return result + numberOfItems
    }
}

class NoFooterCollectionSection<Header, Item>(header: Header?, items: List<Item>) : CollectionSection<Header, Item, Nothing>(header, items, null)

class NoHeaderCollectionSection<Item, Footer>(items: List<Item>, footer: Footer?) : CollectionSection<Nothing, Item, Footer>(null, items, footer)

class ItemsOnlyCollectionSection<Item>(items: List<Item>) : CollectionSection<Nothing, Item, Nothing>(null, items, null)
