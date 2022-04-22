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

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope

class MockAlertPresenter (val alert: Alert) : BaseAlertPresenter(alert) {

    class Builder : BaseAlertPresenter.Builder() {

        val builtAlerts = mutableListOf<MockAlertPresenter>()
        val createMock = this::createAlertFromAlert.mock()

        init {
            createMock.on().doExecute {
                MockAlertPresenter(it.value).also {
                    builtAlerts.add(it)
                }
            }
        }

        override fun create(coroutineScope: CoroutineScope): MockAlertPresenter = createAlertFromAlert(createAlert())
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

    val closeWithActionMock = this::closeWithAction.mock().apply {
        on().doExecute { parameters ->
            val action = parameters.value
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
    }
    fun closeWithAction(action: Alert.Action?): Unit = closeWithActionMock.call(action)

    val showAsyncMock = this::showAsync.mock().apply {
        on().doExecute {
            showAlert(it.first, completion = it.second)
        }
    }
    override fun showAsync(animated: Boolean, completion: () -> Unit): Unit  = showAsyncMock.call(animated, completion)

    val dismissMock = this::dismiss.mock().apply {
        on().doExecute {
            dismissAlert(it.value)
        }
    }
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