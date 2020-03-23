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

import com.splendo.kaluga.collectionView.CollectionViewItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CollectionViewTests {

    companion object {
        fun makeViewModel(items: List<CollectionViewItem>) = TestViewModel(TestRepository(items))
    }

    @Test
    fun testViewModelEmptyList() {
        val viewModel = makeViewModel(emptyList())
        viewModel.subscribe {
            assertTrue(it.isEmpty())
        }
    }

    @Test
    fun testViewModelNonEmptyList()  {
        val viewModel = makeViewModel(listOf(
            CollectionViewItem("One"),
            CollectionViewItem("Two"),
            CollectionViewItem("Tri")
        ))
        viewModel.subscribe {
            assertEquals(it.count(), 3)
        }
    }
}
