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

package com.splendo.kaluga.collectionView

import androidx.test.rule.ActivityTestRule
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

class CollectionViewTests {

    @get:Rule
    var activityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    fun testViewModelEmptyList() = runBlockingTest {
        activityRule.activity.items = emptyList()
        MainScope().launch(Dispatchers.Main) {
            activityRule.activity.viewModel.subscribe {
                assertTrue(it.isEmpty())
            }
        }
    }

    @Test
    fun testViewModelNonEmptyList() = runBlockingTest {
        activityRule.activity.items = listOf(
            CollectionViewItem("One"),
            CollectionViewItem("Two"),
            CollectionViewItem("Tri")
        )
        MainScope().launch(Dispatchers.Main) {
            activityRule.activity.viewModel.subscribe {
                assertEquals(it.count(), 3)
            }
        }
    }
}
