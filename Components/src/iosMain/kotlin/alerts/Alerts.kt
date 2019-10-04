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

import platform.UIKit.*

class UIAlertPresenter(
    private val parent: UIViewController
): AlertPresenter() {

    override fun show(alert: Alert, animated: Boolean, completion: (() -> Unit)?) {

        val uiAlert = UIAlertController.alertControllerWithTitle(
            alert.title,
            alert.message,
            UIAlertControllerStyleAlert
        )

        alert.actions.forEach { action ->
            uiAlert.addAction(
                UIAlertAction.actionWithTitle(
                    action.title,
                    UIAlertActionStyleDefault
                ) { action.handler() }
            )
        }

        parent.presentViewController(uiAlert, animated) { completion?.invoke() }
    }

    override fun dismiss(identifier: Alert.Identifier, animated: Boolean) {
        parent.dismissModalViewControllerAnimated(animated)
    }
}
