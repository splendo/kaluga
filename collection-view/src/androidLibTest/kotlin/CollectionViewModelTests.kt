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

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.splendo.kaluga.collectionView.CollectionViewModel
import com.splendo.kaluga.logging.LogLevel
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.initLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CollectionViewModelTests {

    class PrintLogger : Logger {
        override fun log(level: LogLevel, tag: String?, message: String) {
            println(message)
        }

        override fun log(level: LogLevel, tag: String?, exception: Throwable) {
            println(exception.localizedMessage)
        }
    }

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        initLogger(PrintLogger())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    class TestViewModel : CollectionViewModel() {
        val _items: MutableLiveData<List<String>> = MutableLiveData()
        // val items: LiveData<List<String>> = _items.map { it.value }

        init {
            _items.postValue(listOf("OK"))
            coroutineScope.launch {
                // _items.offer("Hello")
                offerWithDelay()
            }
        }

        private suspend fun offerWithDelay() {
            delay(1_000)
            debug("Offered!")
            _items.postValue(listOf("New", "Data"))
        }
    }

    @Test
    fun testViewModelSubscribeUnsubscribe() = runBlockingTest {
        val activity = mock(AppCompatActivity::class.java)
        val fragmentManager = mock(FragmentManager::class.java)
        `when`(activity.supportFragmentManager).thenReturn(fragmentManager)

        val viewModel = TestViewModel()

        debug("Launch")
        MainScope().launch(Dispatchers.Main) {
            debug("Coroutine launched")
            viewModel._items.observe(activity, Observer {
                debug("Heheh $it")
            })
            debug("Coroutine finished")
        }
        delay(2000)
        debug("Finished")
    }
}
