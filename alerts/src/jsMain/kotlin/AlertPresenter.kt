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

package com.splendo.kaluga.alerts

import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import kotlinx.coroutines.CoroutineScope

/**
 * A [BaseAlertPresenter] for presenting an [Alert].
 *
 * This is not yet fully implemented on JavaScript
 *
 * @param alert The [Alert] being presented.
 */
actual class AlertPresenter(
    alert: Alert,
    logger: Logger,
) : BaseAlertPresenter(alert, logger) {

    /**
     * A [BaseAlertPresenter.Builder] for creating an [AlertPresenter]
     */
    actual class Builder(private val logger: Logger = RestrictedLogger(RestrictedLogLevel.None)) : BaseAlertPresenter.Builder() {

        /**
         * Creates an [AlertPresenter]
         *
         * @param alert The [Alert] to be presented with the built presenter.
         * @param coroutineScope The [CoroutineScope] managing the alert lifecycle.
         * @return The created [AlertPresenter]
         */
        actual override fun create(alert: Alert, coroutineScope: CoroutineScope): AlertPresenter {
            return AlertPresenter(alert, logger)
        }
    }

    override fun showAsync(animated: Boolean, completion: (() -> Unit)) {
        TODO("not implemented")
    }

    override suspend fun show(animated: Boolean): Alert.Action? {
        TODO("not implemented")
    }

    override fun dismiss(animated: Boolean) {
        TODO("not implemented")
    }

    override fun dismissAlert(animated: Boolean) {
        TODO("not implemented")
    }

    override fun showAlert(animated: Boolean, afterHandler: (Alert.Action?) -> Unit, completion: () -> Unit) {
        TODO("not implemented")
    }
}
