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
import platform.UIKit.UICollectionView

actual class CollectionView : UICollectionView() {

    private var _dataSource: DataSource<*, *>? = null

    actual fun bind(dataSource: DataSource<*, *>) {
        this._dataSource = dataSource
    }

    actual fun unbind() {
        _dataSource?.unbind()
    }

}
