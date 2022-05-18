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
package com.splendo.kaluga.test.alerts

import co.touchlab.stately.collections.IsoMutableList
import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope

class MockAlertPresenter(val alert: Alert, setupMocks: Boolean = true) : BaseAlertPresenter(alert) {

    class Builder(setupMocks: Boolean = true) : BaseAlertPresenter.Builder() {

        val builtAlerts = IsoMutableList<MockAlertPresenter>()
        val createMock = ::createAlertFromAlert.mock()

        init {
            if (setupMocks) {
                val builtAlerts = builtAlerts
                createMock.on().doExecute { (alert) ->
                    MockAlertPresenter(alert, setupMocks).also {
                        builtAlerts.add(it)
                    }
                }
            }
        }

        override fun create(coroutineScope: CoroutineScope): BaseAlertPresenter = createAlertFromAlert(createAlert())
        private fun createAlertFromAlert(alert: Alert): MockAlertPresenter = createMock.call(alert)
    }

    private var _isPresented = AtomicBoolean(false)
    var isPresented
        get() = _isPresented.value
        private set(value) { _isPresented.value = value }

    private var _afterHandler = AtomicReference<((Alert.Action?) -> Unit)?>(null)
    private var afterHandler: ((Alert.Action?) -> Unit)?
        get() = _afterHandler.get()
        set(value) = _afterHandler.set(value)

    val closeWithActionMock = this::closeWithAction.mock()
    val showAsyncMock = this::showAsync.mock()
    val dismissMock = this::dismiss.mock()

    init {
        if (setupMocks) {
            closeWithActionMock.on().doExecute { (action) ->
                action?.let {
                    if (alert.actions.contains(it)) {
                        it.handler()
                        it
                    } else {
                        null
                    }
                }.apply {
                    afterHandler?.invoke(this)
                }
                dismissAlert(false)
            }
            showAsyncMock.on().doExecute { (animated, completion) ->
                showAlert(animated, completion = completion)
            }
            dismissMock.on().doExecute { (animated) ->
                dismissAlert(animated)
            }
        }
    }

    fun closeWithAction(action: Alert.Action?): Unit = closeWithActionMock.call(action)

    override fun showAsync(animated: Boolean, completion: () -> Unit): Unit = showAsyncMock.call(animated, completion)

    override fun dismiss(animated: Boolean): Unit = dismissMock.call(animated)

    override fun dismissAlert(animated: Boolean) {
        isPresented = false
        afterHandler = null
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit
    ) {
        isPresented = true
        this.afterHandler = afterHandler
        completion()
    }
}
