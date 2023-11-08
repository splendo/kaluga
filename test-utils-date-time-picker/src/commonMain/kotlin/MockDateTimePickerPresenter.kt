/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.datetimepicker

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.datetimepicker.BaseDateTimePickerPresenter
import com.splendo.kaluga.datetimepicker.DateTimePicker
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope

/**
 * Mock implementation of [BaseKeyboardManager]
 * @param setupMocks If `true` configure mocks to display the keyboard
 */
class MockDateTimePickerPresenter(public override val dateTimePicker: DateTimePicker, setupMocks: Boolean = true) : BaseDateTimePickerPresenter(dateTimePicker) {

    /**
     * Mock implementation of [BaseKeyboardManager.Builder]
     * @param setupMocks If `true` sets up [createMock] to build [MockDateTimePicker]
     */
    class Builder(setupMocks: Boolean = true) : BaseDateTimePickerPresenter.Builder() {

        /**
         * List of created [MockDateTimePicker]
         */
        val builtDateTimePickerPresenters = concurrentMutableListOf<MockDateTimePickerPresenter>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (dateTimePicker, _) ->
                    MockDateTimePickerPresenter(dateTimePicker, setupMocks).also {
                        builtDateTimePickerPresenters.add(it)
                    }
                }
            }
        }

        override fun create(dateTimePicker: DateTimePicker, coroutineScope: CoroutineScope): MockDateTimePickerPresenter = createMock.call(dateTimePicker, coroutineScope)
    }

    /**
     * `true` if the [MockDateTimePickerPresenter] is currently being presented
     */
    var isPresented = false

    private var completionHandler: ((KalugaDate?) -> Unit)? = null

    val showAsyncMock = this::showAsync.mock()
    val dismissMock = this::dismiss.mock()

    init {
        if (setupMocks) {
            showAsyncMock.on().doExecute { (animated, completion) ->
                showDateTimePicker(animated, completion = completion)
            }
            dismissMock.on().doExecute { (animated) ->
                dismissDateTimePicker(animated)
            }
        }
    }

    /**
     * Closes the [MockDateTimePickerPresenter] by providing a [KalugaDate]
     * @param date The [KalugaDate] to provide.
     */
    fun closeWithDate(date: KalugaDate?) {
        completionHandler?.invoke(date)
        dismissDateTimePicker(false)
    }

    override fun showAsync(animated: Boolean, completion: (KalugaDate?) -> Unit): Unit = showAsyncMock.call(animated, completion)
    override fun dismiss(animated: Boolean): Unit = dismissMock.call(animated)

    override fun showDateTimePicker(animated: Boolean, completion: (KalugaDate?) -> Unit) {
        isPresented = true
        completionHandler = completion
    }

    override fun dismissDateTimePicker(animated: Boolean) {
        isPresented = false
        completionHandler = null
    }
}
