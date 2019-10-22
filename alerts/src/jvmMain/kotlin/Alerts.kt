package com.splendo.kaluga.alerts

/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

actual class AlertBuilder: BaseAlertBuilder() {

    override fun create(): AlertInterface {
        return AlertInterface(createAlert())
    }
}

actual class AlertInterface(
    alert: Alert
): BaseAlertPresenter(alert) {

    override fun show(animated: Boolean, completion: (() -> Unit)) {
        TODO("not implemented")
    }

    override suspend fun show(animated: Boolean = true): Alert.Action? {
        TODO("not implemented")
    }

    override fun dismiss(animated: Boolean) {
        TODO("not implemented")
    }

    override fun dismissAlert(animated: Boolean) {
        TODO("not implemented")
    }

    override fun showAlert(
        animated: Boolean,
        afterHandler: (Alert.Action?) -> Unit,
        completion: () -> Unit
    ) {
        TODO("not implemented")
    }
}
