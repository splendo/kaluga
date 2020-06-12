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

import androidx.lifecycle.Observer
import androidx.test.rule.ActivityTestRule
import com.splendo.kaluga.collectionview.item.CollectionSection
import com.splendo.kaluga.collectionview.item.DefaultCollectionItemViewModel
import kotlinx.coroutines.CompletableDeferred
import org.junit.Rule

class AndroidCollectionViewTests : AbstractCollectionViewTest() {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    override fun observe(viewModel: MockCollectionViewModel, deferredItems: List<CompletableDeferred<List<CollectionSection<Nothing, DefaultCollectionItemViewModel<CollectionItem>, Nothing>>>>) {
        activityRule.activity.runOnUiThread {
            viewModel.items.liveData.observe(activityRule.activity, Observer { newValue -> deferredItems.firstOrNull { !it.isCompleted }?.complete(newValue) })
        }
    }
}
