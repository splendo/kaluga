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

import platform.Foundation.NSIndexPath
import platform.UIKit.row
import platform.UIKit.section

fun <Header, Item, Footer> List<CollectionSection<Header, Item, Footer>>.sectionAt(indexPath: NSIndexPath) = this[indexPath.section.toInt()]

fun <Header, Item, Footer> List<CollectionSection<Header, Item, Footer>>.itemAt(indexPath: NSIndexPath) = this[indexPath.section.toInt()].itemAt(indexPath)

fun <Header, Item, Footer> CollectionSection<Header, Item, Footer>.itemAt(indexPath: NSIndexPath): Item = items[indexPath.row.toInt()]
